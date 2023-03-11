package com.khambhatpragati.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.snackbar.Snackbar;
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
import com.khambhatpragati.adapter.ViewPagerAdapter;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.db.DBHelper;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.response.AdvertismentBean;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.GroupsBean;
import com.khambhatpragati.model.response.GroupsResponse;
import com.khambhatpragati.model.response.MessagesResponse;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.model.response.UserDirectoryResponse;
import com.khambhatpragati.mvp.impl.GetGroupsPresenterImpl;
import com.khambhatpragati.mvp.impl.GetVersionUpdatePresenterImpl;
import com.khambhatpragati.mvp.impl.UserDirectoryPresenterImpl;
import com.khambhatpragati.mvp.presenter.GetGroupsPresenter;
import com.khambhatpragati.mvp.presenter.GetVersionUpdatePresenter;
import com.khambhatpragati.mvp.presenter.UserDirectoryPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.AppUtil;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
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
import static com.khambhatpragati.constants.URLs.GET_MEMBERPROFILE_URL;

public class DashboardActivity extends AppCompatActivity implements MVPView {

    private RelativeLayout relativeLayout;
    ViewPager mPager;
    LinearLayout imageSync;
    CircleImageView imgProfile;
    TextView txtUsername;
    CirclePageIndicator indicator;
    LinearLayout layoutMessage,layoutContacts,layoutVasti,layoutProfile;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<AdvertismentBean> imageUrls = new ArrayList<AdvertismentBean>();
    Boolean syncFlag = false;
    public static int adCounter=0;
    public static boolean isAdsTicking=false;
    public static boolean isShownOnce=false;
    public static CountDownTimer adTimer;
    public static ArrayList<AdvertismentBean> adsImageUrls = new ArrayList<AdvertismentBean>();
    AppUpdateManager appUpdateManager;
    private static final int REQUEST_CODE_FLEXI_UPDATE = 17362;
    String currentVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
        addListenerOnWidgets();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        getMemberProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Globals.getInstance().getSelectedCategoryName() != null) {
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mPager = findViewById(R.id.view_pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        txtUsername = findViewById(R.id.txtUsername);
        imgProfile = findViewById(R.id.imgProfile);

        imageSync = findViewById(R.id.imgsync);
        layoutContacts = (LinearLayout) findViewById(R.id.layoutContacts);
        layoutMessage = (LinearLayout) findViewById(R.id.layoutMessages);
        layoutVasti = (LinearLayout) findViewById(R.id.layoutVasti);
        layoutProfile = (LinearLayout) findViewById(R.id.layoutProfile);

        imageSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncFlag = true;
                syncGroupsAndMessages();
            }
        });
        //checkUpdate();
        syncFlag=true;
        getVersionUpdate();

        adTimer= new CountDownTimer(2*60000, 1000) {

            public void onTick(long millisUntilFinished) {
                isAdsTicking = false;
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isAdsTicking = false;
                isShownOnce=false;
                adTimer.start();
                //mTextField.setText("done!");
                //start new activity
            }
        };

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_logout:
                                showLogoutAlert();
                                return true;
                            case R.id.action_edit:
                                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_up, R.anim.stay);
                                return true;
                        }

                        return false;
                    }
                }
        );
        popupMenu.inflate(R.menu.menu_dashboard);
        popupMenu.show();
            }
        });

        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, GroupsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        layoutContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GroupsBean groupsBean = Globals.getInstance().getGroupsBean();
                Intent i = new Intent(DashboardActivity.this, UserDirectoryActivity.class);
                i.putExtra(KEY_GROUPS_ID, "");
  //              i.putExtra(KEY_GROUPS_ID, groupsBean.getMandalID());
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        layoutVasti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, VastiPatraActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });
    }
    private void getVersionUpdate() {
        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();
        PreferencesManager.put(DashboardActivity.this, KEY_LAST_SYNC_TIME, timeStamp);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DialogUtil.showToastShort(DashboardActivity.this, "Checking Version Update...!");
        GetVersionUpdatePresenter presenter = new GetVersionUpdatePresenterImpl(this);
        presenter.getVersionUpdate(DashboardActivity.this, currentVersion);
    }
/*    private void checkUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(listener);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                Log.d("appUpdateInfo :", "packageName :" + appUpdateInfo.packageName() + ", " + "availableVersionCode :" + appUpdateInfo.availableVersionCode() + ", " + "updateAvailability :" + appUpdateInfo.updateAvailability() + ", " + "installStatus :" + appUpdateInfo.installStatus());

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    requestUpdate(appUpdateInfo);
                    Log.d("UpdateAvailable", "update is there ");
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    Log.d("Update", "3");
                    notifyUser();
                } else {
//                    Toast.makeText(DashboardActivity.this, "No Update Available", Toast.LENGTH_SHORT).show();
                    Log.d("NoUpdateAvailable", "update is not there ");
                }
            }
        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, FLEXIBLE, DashboardActivity.this, REQUEST_CODE_FLEXI_UPDATE);
            resume();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FLEXI_UPDATE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (resultCode != RESULT_OK) {
                        Toast.makeText(this, "RESULT_OK" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_OK  :", "" + resultCode);
                    }
                    break;
                case Activity.RESULT_CANCELED:

                    if (resultCode != RESULT_CANCELED) {
                        Toast.makeText(this, "RESULT_CANCELED" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_CANCELED  :", "" + resultCode);
                    }
                    break;
                case RESULT_IN_APP_UPDATE_FAILED:

                    if (resultCode != RESULT_IN_APP_UPDATE_FAILED) {

                        Toast.makeText(this, "RESULT_IN_APP_UPDATE_FAILED" + resultCode, Toast.LENGTH_LONG).show();
                        Log.d("RESULT_IN_APP_FAILED:", "" + resultCode);
                    }
            }
        }
    }

    InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                Log.d("InstallDownloded", "InstallStatus sucsses");
                notifyUser();
            }
        }
    };

    private void notifyUser() {

        Snackbar snackbar =
                Snackbar.make(findViewById(R.id.message),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorBlack));
        snackbar.show();
    }*/
/*

    private void resume() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    notifyUser();

                }

            }
        });
    }
*/

    @Override
    protected void onDestroy() {
        adTimer.cancel();
//        appUpdateManager.unregisterListener(listener);
        super.onDestroy();
    }

    private void getUserDirectoryData() {
        String mobileNo = PreferencesManager.getString(DashboardActivity.this, KEY_MOBILE_NO);
        UserDirectoryPresenter presenter = new UserDirectoryPresenterImpl(this);
        presenter.getUserDirectory(DashboardActivity.this, mobileNo);
    }

    @Override
    public void onSuccess(String response) {
        //ProgressDialogView.hideProgressDialog();
        try {
            JSONObject jobj = new JSONObject();
            try {
                jobj = new JSONObject(response);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
        if (response != null) {
            if (jobj.has("users")) { /**Check User Directory Response*/
                UserDirectoryResponse userDirectoryResponse = JSONParser.parseUserDirectoryResponse(response);
                if (userDirectoryResponse.isStatus()) {
                    DBHelper dbHelper = new DBHelper(DashboardActivity.this);

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(userDirectoryResponse.getUsers());

                    long insertedRows = dbHelper.insertApiResponse(URLs.API_NAME_USER_DIRECTORY, jsonString);
                    syncFlag = false;
                    //Log.d(TAG, insertedRows + " Rows inserted");
                } else {
                    DialogUtil.showSnackBar(relativeLayout, userDirectoryResponse.getMessage());
                }
                getMemberProfile();
            }
            else if (jobj.has("data")) { //for version update
                try {
                    JSONObject j = new JSONObject(response);

                    if (j.getString("message").equalsIgnoreCase("Success")) {
                        JSONObject json = j.getJSONObject("data");
                        if (json.length() > 0) {
                            if (Float.valueOf(currentVersion) < Float.valueOf(json.getString("current_version"))) {
                                //Toast.makeText(LoginActivity.this,"Update available",Toast.LENGTH_LONG).show();
                                @SuppressLint("ResourceAsColor") final MaterialDialog dialog =
                                        new MaterialDialog.Builder(DashboardActivity.this)
                                                .cancelable(false)
                                                .customView(R.layout.dialog_version, true)
                                                .build();
                                Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
                                btnUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=" + DashboardActivity.this.getPackageName()));
                                        startActivity(intent1);
                                    }
                                });

                                /*Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });*/
                                dialog.show();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (syncFlag == true) {
                    getUserDirectoryData();
                }

             //   fetchNewGroupMessages();
            }
            else {
                ProgressDialogView.hideProgressDialog();
                if (syncFlag == true) {
                    getUserDirectoryData();
                }

            }
        } else {
            if (syncFlag == true) {
                getUserDirectoryData();
            }

            ProgressDialogView.hideProgressDialog();
            DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_server_error));
        }
        }catch (Exception e){

        }
        ProgressDialogView.hideProgressDialog();
    }

    private void syncGroupsAndMessages() {
        //Log.d(TAG, "--Syncing the Groups and Messages, Calling APIs--");
        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_LIST);
       // PreferencesManager.removeString(DashboardActivity.this, KEY_CATEGORY_RESPONSE);
        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_TITLE);
        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_DESCRIPTION);
        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_POSTED_BY);
        PreferencesManager.removeString(DashboardActivity.this, KEY_GROUPS_ID);
        PreferencesManager.removeString(DashboardActivity.this, KEY_GROUPS_RESPONSE);
        //PreferencesManager.removeString(DashboardActivity.this, KEY_LAST_SYNC_TIME);
        getGroups();
    }

    private void getGroups() {
        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();
        PreferencesManager.put(DashboardActivity.this, KEY_LAST_SYNC_TIME, timeStamp);

        DialogUtil.showToastShort(DashboardActivity.this, "Sync in Progress...!");
        String mobileNo = PreferencesManager.getString(DashboardActivity.this, KEY_MOBILE_NO);
        GetGroupsPresenter presenter = new GetGroupsPresenterImpl(this);
        presenter.getGroups(DashboardActivity.this, mobileNo);
        //chetana added AQUERY request instead of volley

        //mandal(mobileNo);
        //userDirectory(mobileNo);
    }
    private void getFullScreenAds() {
        final ProgressDialog progress = new ProgressDialog(DashboardActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","FullScreenAd");
        params.put("screen_type","");

        Log.d("Params", "Testing" + params);

        aQuery.ajax(URLs.GET_ADS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("Checking", "url: " + url + " , json: " + json);
                try {
                    if(json!=null) {
                        if (json.toString().contains("data")) {
                            AdvertismentResponse adsResponse = JSONParser.parseAdsResponse(json.toString());
                            if (adsResponse.isStatus().equalsIgnoreCase("200")) {
                                adsImageUrls.clear();
                                Date todayDate = Calendar.getInstance().getTime();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String todayString = formatter.format(todayDate);
                                for (int i = 0; i < adsResponse.getData().size(); i++) {
                                    if (formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                            || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                        if (adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("FullScreenAd"))
                                            adsImageUrls.add(adsResponse.getData().get(i));
                                    }
                                }
                            } else {
                                //ProgressDialogView.hideProgressDialog();
                                //DialogUtil.showSnackBarWithAction(relativeLayout, adsResponse.getMessage());
                            }
                        }
                    }else {
                    }
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
            }
        }.method(AQuery.METHOD_POST));
    }
    private void getBannerAds() {
        final ProgressDialog progress = new ProgressDialog(DashboardActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BannerAd");
        params.put("screen_type","Dashboard Screen");

        Log.d("Params", "Testing" + params);

        aQuery.ajax(URLs.GET_ADS_URL, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                Log.d("Checking", "url: " + url + " , json: " + json);
                try {
                    if(json!=null){
                        if (json.toString().contains("data")) {
                            AdvertismentResponse adsResponse = JSONParser.parseAdsResponse(json.toString());
                            if (adsResponse.isStatus().equalsIgnoreCase("200")) {
                                imageUrls.clear();
                                Date todayDate = Calendar.getInstance().getTime();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String todayString = formatter.format(todayDate);
                                for (int i = 0; i < adsResponse.getData().size(); i++) {
                                    if (formatter.parse(adsResponse.getData().get(i).getEnd_date()).after(formatter.parse(todayString))
                                            || formatter.parse(adsResponse.getData().get(i).getEnd_date()).equals(formatter.parse(todayString))) {
                                        if (adsResponse.getData().get(i).getAdvertisement_type().equalsIgnoreCase("BannerAd"))
                                            imageUrls.add(adsResponse.getData().get(i));
                                    }
                                }
                                init();
                            } else {
                                //ProgressDialogView.hideProgressDialog();
                                //DialogUtil.showSnackBarWithAction(relativeLayout, adsResponse.getMessage());
                            }
                        }
                        }
                        progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
                getFullScreenAds();
            }
        }.method(AQuery.METHOD_POST));

    }
    public void getMemberProfile() {
        final ProgressDialog progress = new ProgressDialog(DashboardActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        Map<String, Object> params = new HashMap<>();
        params.put("member_id", UserPreference.getInstance(DashboardActivity.this).getPreference().getMemberID());
        params.put("mobile_no", UserPreference.getInstance(DashboardActivity.this).getPreference().getMobileNo());

        Log.d("Params", "Testing" + params);

        RequestQueue queue = Volley.newRequestQueue(DashboardActivity.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, GET_MEMBERPROFILE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            if(json!=null) {
                                Log.d("TAG", "JSON POST Response = " + json.toString());

                                if (json.toString().contains("data")) {
                                    OTPVerificationResponse otpResponse = JSONParser.parseOTPVerificationResponse(json.toString());
                                    if (!otpResponse.isStatus()) {

                                        JSONArray j = json.getJSONArray("data");
                                        JSONObject jObj = j.getJSONObject(0);
                                        ImageLoader imageLoader = CustomVolleyRequest.getInstance(DashboardActivity.this).getImageLoader();
                                        if (jObj.getString("imageUrl").trim().equalsIgnoreCase("NULL")) {
                                            ;
                                        } else {
                                            if (jObj.getString("imageUrl").trim().startsWith("http://"))
                                                imageLoader.get(jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                            else
                                                imageLoader.get("http://khambhatpragati.com/uploads/member_images/" + jObj.getString("imageUrl"), ImageLoader.getImageListener(imgProfile, R.mipmap.noimage, R.mipmap.profile));
                                        }

                                        txtUsername.setText(jObj.getString("firstName"));
                                    } else {
                                        //ProgressDialogView.hideProgressDialog();
                                        DialogUtil.showSnackBarWithAction(relativeLayout, otpResponse.getMessage());
                                    }
                                }
                            }
                            getBannerAds();
                            progress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Volley Error in Response = " + error.getMessage());
                        DialogUtil.showSnackBarWithAction(relativeLayout, error.getMessage());
                        progress.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "Network timeout error");
                        } else if (error instanceof AuthFailureError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "There was an authentication failure while performing a Request.");
                        } else if (error instanceof ServerError) {
                     //       DialogUtil.showSnackBarWithAction(relativeLayout, "ServerError");
                        } else if (error instanceof NetworkError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "NetworkError");
                        } else if (error instanceof ParseError) {
                            DialogUtil.showSnackBarWithAction(relativeLayout, "ParseError, Server's response could not be parsed.");
                        }
                        getBannerAds();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonRequest);
    }

    private void init() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(DashboardActivity.this, imageUrls);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        NUM_PAGES = imageUrls.size();
        //Set circle indicator radius
        indicator.setRadius(5 * density);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }

    private void addListenerOnWidgets() {
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu); //your file name

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            *//*case R.id.action_sync:
                syncFlag = true;
                syncGroupsAndMessages();
                return true;*//*
            case R.id.action_logout:
                showLogoutAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    private void showLogoutAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
        builder1.setMessage(R.string.msg_logout_alert);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_LIST);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_CATEGORY_RESPONSE);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MOBILE_NO);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_IS_LOGGED_IN);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_LIST);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_TITLE);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_DESCRIPTION);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_MESSAGES_POSTED_BY);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_GROUPS_ID);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_GROUPS_RESPONSE);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_ACCESS_LEVEL);
                        PreferencesManager.removeString(DashboardActivity.this, KEY_LAST_SYNC_TIME);
                        PreferencesManager.removeString(DashboardActivity.this, String.valueOf(KEY_DAY_TIMESTAMP));


                        PreferencesManager.put(getApplicationContext(), KEY_IS_LOGGED_IN, false);
                        Intent i = new Intent(DashboardActivity.this, MobileVerificationActivity.class);
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
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(DashboardActivity.this);
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
}