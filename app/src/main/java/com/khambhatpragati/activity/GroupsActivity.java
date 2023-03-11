package com.khambhatpragati.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.khambhatpragati.R;
import com.khambhatpragati.adapter.GroupDetailsAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.db.DBHelper;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.listeners.GroupsItemClickListener;
import com.khambhatpragati.model.response.AdvertismentBean;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.GroupsBean;
import com.khambhatpragati.model.response.GroupsResponse;
import com.khambhatpragati.model.response.MessagesBean;
import com.khambhatpragati.model.response.MessagesResponse;
import com.khambhatpragati.model.response.UserDirectoryResponse;
import com.khambhatpragati.mvp.impl.GetAllMessagesPresenterImpl;
import com.khambhatpragati.mvp.impl.GetGroupsPresenterImpl;
import com.khambhatpragati.mvp.impl.GetVersionUpdatePresenterImpl;
import com.khambhatpragati.mvp.impl.UserDirectoryPresenterImpl;
import com.khambhatpragati.mvp.presenter.GetAllMessagePresenter;
import com.khambhatpragati.mvp.presenter.GetGroupsPresenter;
import com.khambhatpragati.mvp.presenter.GetVersionUpdatePresenter;
import com.khambhatpragati.mvp.presenter.UserDirectoryPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.utils.AppUtil;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_ACCESS_LEVEL;
import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;
import static com.khambhatpragati.constants.Keys.KEY_DAY_TIMESTAMP;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_RESPONSE;
import static com.khambhatpragati.constants.Keys.KEY_IS_LOGGED_IN;
import static com.khambhatpragati.constants.Keys.KEY_LAST_SYNC_TIME;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_DESCRIPTION;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_LIST;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_POSTED_BY;
import static com.khambhatpragati.constants.Keys.KEY_MESSAGES_TITLE;
import static com.khambhatpragati.constants.Keys.KEY_MOBILE_NO;

//import com.google.firebase.iid.FirebaseInstanceId;

public class GroupsActivity extends AppCompatActivity implements GroupsItemClickListener, MVPView {

    private static final String TAG = GroupsActivity.class.getSimpleName();

    private RelativeLayout relativeLayout;


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GroupDetailsAdapter mAdapter;
    Boolean syncFlag = false;
    ImageView imageView;
    private ImageLoader imageLoader;

    private static int SPLASH_TIME_OUT = 1500;
    private HashMap<String, Integer> idMessagesCounterMapping;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        initToolBar();
        initView();

        if(!isAdsTicking ) {
            if(!isShownOnce) {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(GroupsActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isAdsTicking) {
                    if (adsImageUrls.size() > 0) {
                        if (adCounter >= adsImageUrls.size())
                            adCounter = 0;
                        Intent i = new Intent(GroupsActivity.this, FullScreenAdActivity.class);
                        i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                        startActivity(i);
                    }
                }
            }
        }, SPLASH_TIME_OUT);*/
     }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_groups);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

//        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_drawer));
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(GroupsActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        fetchNewGroupMessages();
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(GroupsActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","Mandal Screen");

        Log.d("Params", "Testing" + params);

        aQuery.ajax(URLs.GET_ADS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("Checking", "url: " + url + " , json: " + json);
                try {
                    if (json.toString().contains("data")) {
                        AdvertismentResponse adsResponse = JSONParser.parseAdsResponse(json.toString());
                        if (adsResponse.isStatus().equalsIgnoreCase("200")) {
                            Date todayDate = Calendar.getInstance().getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String todayString = formatter.format(todayDate);
                            for(int i=0;i<adsResponse.getData().size();i++){
                                if(formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                        || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                    if(adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("BottomAd")
                                      && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("Mandal Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(GroupsActivity.this).getImageLoader();
                                        if(!adsResponse.getData().get(i).getFull_path().equalsIgnoreCase("NULL")
                                                && adsResponse.getData().get(i).getFull_path().startsWith("http"))
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            //ProgressDialogView.hideProgressDialog();
                            imageView.setVisibility(View.GONE);
                            DialogUtil.showSnackBarWithAction(relativeLayout, adsResponse.getMessage());
                        }
                    }
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
            }
        }.method(AQuery.METHOD_POST));

    }
    private void fetchNewGroupMessages() {
        String groupsStringResponse = PreferencesManager.getString(GroupsActivity.this, KEY_GROUPS_RESPONSE);
        JSONObject jobj = new JSONObject();
        if (groupsStringResponse != null) {
            try {
                jobj = new JSONObject(groupsStringResponse);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        if (checkIfSyncIsNeeded(groupsStringResponse)) {
            syncGroupsAndMessages();
        } else {
            if (groupsStringResponse != null && jobj.has("groups")) {
                //Log.d(TAG, "--Groups data already saved and Notification not occur so only displaying the saved groups--");
                displayGroups();
            } else {
                //Log.d(TAG, "--First time user came on Groups Activity that is why calling groups api--");
                syncFlag = true;
                getGroups();
            }
        }
    }else{
        //Log.d(TAG, "--First time user came on Groups Activity that is why calling groups api--");
        syncFlag = true;
        getGroups();
    }
    }

//    private void updateFCMToken() {
//        String fcmToken = FirebaseInstanceId.getInstance().getToken();
//        String mobileNo = PreferencesManager.getString(GroupsActivity.this, KEY_MOBILE_NO);
//        UpdateFCMTokenPresenter presenter = new UpdateFCMTokenPresenterImpl(this);
//        presenter.updateFCMToken(GroupsActivity.this, mobileNo, fcmToken);
//    }

    private boolean checkIfSyncIsNeeded(String groupsStringResponse) {
        String lastSyncTimeStamp = PreferencesManager.getString(GroupsActivity.this, KEY_LAST_SYNC_TIME);
        if (lastSyncTimeStamp == null) {
            return true;
        }
        GroupsResponse groupsResponse = JSONParser.parseGroupsResponse(groupsStringResponse);
        if(groupsResponse!=null) {
            if (groupsResponse.getGroups() != null && groupsResponse.getGroups().size() > 0) {
                for (int i = 0; i < groupsResponse.getGroups().size(); i++) {
                    String expiry = groupsResponse.getGroups().get(i).getExpiry();
                    if (expiry != null && lastSyncTimeStamp != null) {
                        long timeStamp = Long.parseLong(lastSyncTimeStamp);
                        long currentTimeStamp = System.currentTimeMillis() / 1000;
                        double noOfDays = Double.longBitsToDouble(currentTimeStamp - timeStamp) / Double.longBitsToDouble(KEY_DAY_TIMESTAMP);
                        if (expiry != null && (noOfDays > (Double.parseDouble(expiry)))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void displayGroups() {
        idMessagesCounterMapping = new HashMap<String, Integer>();
        String groupsStringResponse = PreferencesManager.getString(GroupsActivity.this, KEY_GROUPS_RESPONSE);
        JSONObject jobj = new JSONObject();
        try {
            jobj = new JSONObject(groupsStringResponse);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        if (groupsStringResponse != null && jobj.has("groups")) {
            GroupsResponse groupsResponse = JSONParser.parseGroupsResponse(PreferencesManager.getString(GroupsActivity.this, KEY_GROUPS_RESPONSE));
            if (groupsResponse.getGroups() != null && groupsResponse.getGroups().size() > 0) {
                for (int i = 0; i < groupsResponse.getGroups().size(); i++) {
                    idMessagesCounterMapping.put(groupsResponse.getGroups().get(i).getMandalID(), 0);
                }

                mAdapter = new GroupDetailsAdapter(groupsResponse.getGroups(), idMessagesCounterMapping);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.setClickListener(this);

                getAllMessages();
            } else {
                DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_groups_not_found));
            }
        } else {
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_groups_not_found));
        }
    }

    private void getGroups() {
        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();
        PreferencesManager.put(GroupsActivity.this, KEY_LAST_SYNC_TIME, timeStamp);

        DialogUtil.showToastShort(GroupsActivity.this, "Sync in Progress...!");
        String mobileNo = PreferencesManager.getString(GroupsActivity.this, KEY_MOBILE_NO);
        GetGroupsPresenter presenter = new GetGroupsPresenterImpl(this);
        presenter.getGroups(GroupsActivity.this, mobileNo);
        //chetana added AQUERY request instead of volley

        //mandal(mobileNo);
        //userDirectory(mobileNo);
    }
    private void getAllMessages() {
        String msgJsonList = PreferencesManager.getString(GroupsActivity.this, KEY_MESSAGES_LIST);

        ArrayList mandalIds = new ArrayList(Arrays.asList(idMessagesCounterMapping.keySet().toArray()));
        String mobileNo = PreferencesManager.getString(GroupsActivity.this, KEY_MOBILE_NO);
        String lastModifiedDate = "";

        ArrayList<MessagesBean> listOfMessages = JSONParser.parseMessageList(msgJsonList);
        if (listOfMessages != null && listOfMessages.size() > 0) {
            listOfMessages = AppUtil.sortMsgListByModifiedDate(listOfMessages);
            lastModifiedDate = listOfMessages.get(0).getModifiedDateTime();
            GetAllMessagePresenter presenter = new GetAllMessagesPresenterImpl(this);
            presenter.getAllMessages(GroupsActivity.this, mandalIds, mobileNo, lastModifiedDate);
        } else {
            GetAllMessagePresenter presenter = new GetAllMessagesPresenterImpl(this);
            presenter.getAllMessages(GroupsActivity.this, mandalIds, mobileNo, lastModifiedDate);
        }
    }

    @Override
    public void onStartProgress() {
        Log.d(TAG, "-----showProgressDialog");
        ProgressDialogView.showProgressDialog(GroupsActivity.this);
    }
//    private void mandal(String mobile) {
//        final ProgressDialog progress = new ProgressDialog(GroupsActivity.this);
//        progress.setMessage("Please Wait....");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setCancelable(false);
//        progress.show();
//
//        AQuery aQuery = new AQuery(getApplicationContext());
//        Map<String, Object> params = new HashMap<>();
//        params.put("in_userPhone", mobile);
//
//        Log.d("Params", "Testing" + params);
//
//        aQuery.ajax(URLs.GET_GROUPS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.d("Checking", "url: " + url + " , json: " + json);
//                try {
//                        if (json.toString().contains("groups")) {
//                            GroupsResponse groupsResponse = JSONParser.parseGroupsResponse(json.toString());
//                            //Log.d(TAG, "groupsResponse.isStatus() = " + groupsResponse.isStatus());
//                            //Log.d(TAG, "groupsResponse.getMessage() = " + groupsResponse.getMessage());
//                            if (groupsResponse.isStatus()) {
//                                PreferencesManager.put(GroupsActivity.this, KEY_GROUPS_RESPONSE, json.toString());
//                                displayGroups();
//                            } else {
//                                //ProgressDialogView.hideProgressDialog();
//                                DialogUtil.showSnackBarWithAction(relativeLayout, groupsResponse.getMessage());
//                            }
//                        }
//
//                        /**Check All Messages Response*/
//                        if (json.toString().contains("notificationMessages")) {
//                            ProgressDialogView.hideProgressDialog();
//                            MessagesResponse allMessagesResponse = JSONParser.parseAllMessagesResponse(json.toString());
//                            //Log.d(TAG, "allMessagesResponse.isStatus() = " + allMessagesResponse.isStatus());
//                            //Log.d(TAG, "allMessagesResponse.getMessage() = " + allMessagesResponse.getMessage());
//                            if (allMessagesResponse.isStatus()) {
//                                processMsgResponse(allMessagesResponse);
//                            } else {
//                                DialogUtil.showSnackBar(relativeLayout, allMessagesResponse.getMessage());
//                            }
//                            //updateFCMToken();
//                        }
//                        progress.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    progress.dismiss();
//                }
//            }
//        }.method(AQuery.METHOD_POST));
//
//    }
//
//    private void userDirectory(String mobile) {
//        final ProgressDialog progress = new ProgressDialog(GroupsActivity.this);
//        progress.setMessage("Please Wait....");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setCancelable(false);
//        progress.show();
//
//        AQuery aQuery = new AQuery(getApplicationContext());
//        Map<String, Object> params = new HashMap<>();
//        params.put("in_userPhone", mobile);
//
//        Log.d("Params", "Testing" + params);
//
//        aQuery.ajax(URLs.USER_DIRECTORY_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
//                Log.d("Checking", "url: " + url + " , json: " + json);
//                try {
//                    if(json!=null){
//                    if (json.toString().contains("users")) {
//                        UserDirectoryResponse userDirectoryResponse = JSONParser.parseUserDirectoryResponse(json.toString());
//                        //Log.d(TAG, "userDirectoryResponse.isStatus() = " + userDirectoryResponse.isStatus());
//                        //Log.d(TAG, "userDirectoryResponse.getMessage() = " + userDirectoryResponse.getMessage());
//                        if (userDirectoryResponse.isStatus()) {
//                            DBHelper dbHelper = new DBHelper(GroupsActivity.this);
//
//                            Gson gson = new Gson();
//                            String jsonString = gson.toJson(userDirectoryResponse.getUsers());
//
//                            long insertedRows = dbHelper.insertApiResponse(URLs.API_NAME_USER_DIRECTORY, jsonString);
//                            //Log.d(TAG, insertedRows + " Rows inserted");
//                        } else {
//                            DialogUtil.showSnackBar(relativeLayout, userDirectoryResponse.getMessage());
//                        }
//                        progress.dismiss();
//
//                    }
//                        progress.dismiss();
//
//                    } else {
//                       Toast.makeText(GroupsActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        progress.dismiss();
//
//                    }
//                    progress.dismiss();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    progress.dismiss();
//                }
//            }
//        }.method(AQuery.METHOD_POST));
//
//    }

    @Override
    public void onSuccess(String response) {
        //ProgressDialogView.hideProgressDialog();
        JSONObject jobj = new JSONObject();
        try {
            jobj = new JSONObject(response);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        if (response != null) {
            /**Check Groups Response*/
            if (jobj.has("groups")) {
                GroupsResponse groupsResponse = JSONParser.parseGroupsResponse(response);
                //Log.d(TAG, "groupsResponse.isStatus() = " + groupsResponse.isStatus());
                //Log.d(TAG, "groupsResponse.getMessage() = " + groupsResponse.getMessage());
                if (groupsResponse.isStatus()) {
                    PreferencesManager.put(GroupsActivity.this, KEY_GROUPS_RESPONSE, response);
                    displayGroups();
                } else {
                    //ProgressDialogView.hideProgressDialog();
                    DialogUtil.showSnackBarWithAction(relativeLayout, groupsResponse.getMessage());
                }
            }
            else if (jobj.has("notificationMessages")) { /**Check All Messages Response*/
                ProgressDialogView.hideProgressDialog();
                MessagesResponse allMessagesResponse = JSONParser.parseAllMessagesResponse(response);
                //Log.d(TAG, "allMessagesResponse.isStatus() = " + allMessagesResponse.isStatus());
                //Log.d(TAG, "allMessagesResponse.getMessage() = " + allMessagesResponse.getMessage());
                if (allMessagesResponse.isStatus()) {
                    processMsgResponse(allMessagesResponse);
                } else {
                    DialogUtil.showSnackBar(relativeLayout, allMessagesResponse.getMessage());
                }

                if (syncFlag == true) {
                    getUserDirectoryData();
                }
                //updateFCMToken();
            }
            else if (jobj.has("users")) { /**Check User Directory Response*/
                UserDirectoryResponse userDirectoryResponse = JSONParser.parseUserDirectoryResponse(response);
                //Log.d(TAG, "userDirectoryResponse.isStatus() = " + userDirectoryResponse.isStatus());
                //Log.d(TAG, "userDirectoryResponse.getMessage() = " + userDirectoryResponse.getMessage());
                if (userDirectoryResponse.isStatus()) {
                    DBHelper dbHelper = new DBHelper(GroupsActivity.this);

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(userDirectoryResponse.getUsers());

                    long insertedRows = dbHelper.insertApiResponse(URLs.API_NAME_USER_DIRECTORY, jsonString);
                    syncFlag = false;
                    //Log.d(TAG, insertedRows + " Rows inserted");
                } else {
                    DialogUtil.showSnackBar(relativeLayout, userDirectoryResponse.getMessage());
                }
            }
            else if (jobj.has("data")) { //for version update
                /*try {
                    JSONObject j = new JSONObject(response);

                    if (j.getString("message").equalsIgnoreCase("Success")) {
                        JSONObject json = j.getJSONObject("data");
                        if (json.length() > 0) {
                            if (Float.valueOf(currentVersion) < Float.valueOf(json.getString("current_version"))) {
                                //Toast.makeText(LoginActivity.this,"Update available",Toast.LENGTH_LONG).show();
                                @SuppressLint("ResourceAsColor") final MaterialDialog dialog =
                                        new MaterialDialog.Builder(GroupsActivity.this)
                                                .customView(R.layout.dialog_version, true)
                                                .build();
                                Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
                                btnUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=" + GroupsActivity.this.getPackageName()));
                                        startActivity(intent1);
                                    }
                                });

                                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                fetchNewGroupMessages();
            }
            else {
                ProgressDialogView.hideProgressDialog();
            }
        } else {
            ProgressDialogView.hideProgressDialog();
            DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_server_error));
        }
    }

    private void processMsgResponse(MessagesResponse allMessagesResponse) {
        ArrayList<MessagesBean> listOfMessages = new ArrayList<>();

        if (allMessagesResponse.getNotificationMessages() != null && allMessagesResponse.getNotificationMessages().size() > 0) {
            calculateNewMessagesCount(allMessagesResponse.getNotificationMessages());
            String msgJsonList = PreferencesManager.getString(GroupsActivity.this, KEY_MESSAGES_LIST);
            if (msgJsonList != null && msgJsonList.length() > 0) {
                listOfMessages = JSONParser.parseMessageList(msgJsonList);
                for (int i = 0; i < allMessagesResponse.getNotificationMessages().size(); i++) {
                    listOfMessages.add(allMessagesResponse.getNotificationMessages().get(i));
                }
            } else {
                for (int i = 0; i < allMessagesResponse.getNotificationMessages().size(); i++) {
                    listOfMessages.add(allMessagesResponse.getNotificationMessages().get(i));
                }
            }

            String jsonMsgList = AppUtil.convertArrayListToJsonString(listOfMessages);
            PreferencesManager.put(GroupsActivity.this, KEY_MESSAGES_LIST, jsonMsgList);
        }
    }

    private void calculateNewMessagesCount(ArrayList<MessagesBean> listOfMessages) {
        //Log.d(TAG, "--calculateNewMessagesCount--size" + listOfMessages.size());
        ArrayList<String> mandalIds = new ArrayList(Arrays.asList(idMessagesCounterMapping.keySet().toArray()));
        for (MessagesBean msgBean : listOfMessages) {
            for (String mandalId : mandalIds) {
                if (mandalId.equals(msgBean.getMandalId())) {
                    Integer unReadCount = idMessagesCounterMapping.get(mandalId);
                    idMessagesCounterMapping.put(mandalId, ++unReadCount);
                }
            }
        }
        mAdapter.setUnReadCounter(idMessagesCounterMapping);
    }

    @Override
    public void onError(String errorMessage) {
        ProgressDialogView.hideProgressDialog();
        DialogUtil.showSnackBarWithAction(relativeLayout, errorMessage);
    }

    @Override
    public void onInternetConnectionError(boolean isOnline) {
        DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_no_internet));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_sync:
                syncFlag = true;
                syncGroupsAndMessages();
                return true;*/
            case R.id.action_user_directory:
                Intent i = new Intent(GroupsActivity.this, UserDirectoryActivity.class);
                i.putExtra(KEY_GROUPS_ID, "");
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                return true;
/*            case R.id.action_logout:
                showLogoutAlert();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void syncGroupsAndMessages() {
        //Log.d(TAG, "--Syncing the Groups and Messages, Calling APIs--");
        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_LIST);
        //PreferencesManager.removeString(GroupsActivity.this, KEY_CATEGORY_RESPONSE);
        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_TITLE);
        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_DESCRIPTION);
        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_POSTED_BY);
        PreferencesManager.removeString(GroupsActivity.this, KEY_GROUPS_ID);
        PreferencesManager.removeString(GroupsActivity.this, KEY_GROUPS_RESPONSE);
        //PreferencesManager.removeString(GroupsActivity.this, KEY_LAST_SYNC_TIME);

        getGroups();
    }

    private void getUserDirectoryData() {
        String mobileNo = PreferencesManager.getString(GroupsActivity.this, KEY_MOBILE_NO);
        UserDirectoryPresenter presenter = new UserDirectoryPresenterImpl(this);
        presenter.getUserDirectory(GroupsActivity.this, mobileNo);
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupsActivity.this);
        builder1.setMessage(R.string.msg_logout_alert);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // chetana PreferencesManager.clear(getApplicationContext());

                        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_LIST);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_CATEGORY_RESPONSE);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_MOBILE_NO);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_IS_LOGGED_IN);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_LIST);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_TITLE);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_DESCRIPTION);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_MESSAGES_POSTED_BY);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_GROUPS_ID);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_GROUPS_RESPONSE);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_ACCESS_LEVEL);
                        PreferencesManager.removeString(GroupsActivity.this, KEY_LAST_SYNC_TIME);
                        PreferencesManager.removeString(GroupsActivity.this, String.valueOf(KEY_DAY_TIMESTAMP));

                        PreferencesManager.put(getApplicationContext(), KEY_IS_LOGGED_IN, false);
                        Intent i = new Intent(GroupsActivity.this, MobileVerificationActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_down, R.anim.stay);
                        finish();
                        DialogUtil.showToastLong(getApplicationContext(), getString(R.string.msg_logout_success));
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onClick(View view, int position, GroupsBean groupsBean) {
        //Log.d(TAG, "position = " + position);
        //Log.d(TAG, "groupId = " + groupsBean.getMandalID());
        Globals.getInstance().setGroupsBean(groupsBean);

        idMessagesCounterMapping.put(groupsBean.getMandalID(), 0);
        mAdapter.setUnReadCounter(idMessagesCounterMapping);
        Intent i = new Intent(GroupsActivity.this, MessagesActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}