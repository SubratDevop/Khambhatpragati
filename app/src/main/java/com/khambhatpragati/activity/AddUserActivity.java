package com.khambhatpragati.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.khambhatpragati.R;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.db.DBHelper;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.model.response.UserDirectoryResponse;
import com.khambhatpragati.mvp.impl.AddUserPresenterImpl;
import com.khambhatpragati.mvp.presenter.AddUserPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_GROUPS_ID;
import static com.khambhatpragati.constants.Keys.KEY_MOBILE_NO;

public class AddUserActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = AddUserActivity.class.getName();
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutMobileNo;
    private EditText etName;
    private EditText etMobileNo;
    private Button btnAdd;
    private String groupId;
    ImageView imageView;
    private ImageLoader imageLoader;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getString(KEY_GROUPS_ID);
        }

        initToolBar();
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_add_user);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        inputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        inputLayoutMobileNo = (TextInputLayout) findViewById(R.id.inputLayoutMobileNo);
        etName = (EditText) findViewById(R.id.etName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        btnAdd = (Button) findViewById(R.id.btnAdd);

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(AddUserActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    startActivity(i);
                }
            }
        }, 1500);*/
        if(!isAdsTicking ) {
            if(!isShownOnce) {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(AddUserActivity.this, FullScreenAdActivity.class);
                    i.putExtra("adURL", adsImageUrls.get(adCounter).getFull_path());
                    i.putExtra("adWebLink", adsImageUrls.get(adCounter).getWebsite_url());
                    i.putExtra("adTIMEOUT", adsImageUrls.get(adCounter).getDisplay_time());
                    startActivity(i);
                }
                isShownOnce=true;
                isAdsTicking=true;
            }
        }
        addListenerOnWidgets();
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strAdWeblink.equalsIgnoreCase("") && !strAdWeblink.equalsIgnoreCase("null")) {
                    Intent intent = new Intent(AddUserActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(AddUserActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","AddMember Screen");

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
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("AddMember Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(AddUserActivity.this).getImageLoader();
                                        if(!adsResponse.getData().get(i).getFull_path().equalsIgnoreCase("NULL")
                                                && adsResponse.getData().get(i).getFull_path().startsWith("http"))
                                        imageLoader.get(adsResponse.getData().get(i).getFull_path(), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
                                    }
                                }
                            }

                        } else {
                            imageView.setVisibility(View.GONE);
                            //ProgressDialogView.hideProgressDialog();
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


    private void addListenerOnWidgets() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide virtual keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                attemptAddUser();
            }
        });
    }

    private void attemptAddUser() {
        boolean isDataValid = true;

        if (etName.getText().toString().isEmpty()) {
            isDataValid = false;
            inputLayoutName.setError(getString(R.string.msg_enter_name));
        } else {
            inputLayoutName.setError(null);
        }

        if (etMobileNo.getText().toString().length() != 10) {
            isDataValid = false;
            inputLayoutMobileNo.setError(getString(R.string.msg_valid_mobile_no));
        } else {
            inputLayoutMobileNo.setError(null);
        }

        if (isDataValid) {
            AddUserPresenter presenter = new AddUserPresenterImpl(this);
            presenter.addUser(AddUserActivity.this, etMobileNo.getText().toString(), etName.getText().toString(),
                    PreferencesManager.getString(AddUserActivity.this, KEY_MOBILE_NO), groupId);
        }
    }

    @Override
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(AddUserActivity.this);
    }

    @Override
    public void onSuccess(String response) {
        ProgressDialogView.hideProgressDialog();
        if (response != null) {
            UserDirectoryResponse userDirectoryResponse = JSONParser.parseUserDirectoryResponse(response);
//            Log.d(TAG, "userDirectoryResponse.isStatus() = " + userDirectoryResponse.isStatus());
//            Log.d(TAG, "userDirectoryResponse.getMessage() = " + userDirectoryResponse.getMessage());

            if (userDirectoryResponse.isStatus()) {
                if (userDirectoryResponse.getUsers() != null && userDirectoryResponse.getUsers().size() > 0) {
                    for (int i = 0; i < userDirectoryResponse.getUsers().size(); i++) {
                        addNewUser(userDirectoryResponse.getUsers().get(i), userDirectoryResponse.getMessage());
                    }
                }
            } else {
                Globals.getInstance().setMsgSent(false);
                DialogUtil.showSnackBarWithAction(relativeLayout, userDirectoryResponse.getMessage());
            }
        } else {
            Globals.getInstance().setMsgSent(false);
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_server_error));
        }
    }

    private void addNewUser(UserDirectoryBean userDirectoryBean, String responseMsg) {
        DBHelper dbHelper = new DBHelper(AddUserActivity.this);
        ArrayList<UserDirectoryBean> listOfUserDirectory = JSONParser.parseUserDirectoryList(dbHelper.selectResponse(URLs.API_NAME_USER_DIRECTORY));
        if (listOfUserDirectory != null) {
            ArrayList<UserDirectoryBean> listOfUsers = listOfUserDirectory;
            //Log.d(TAG, "listOfUsers size before adding new user = " + listOfUsers.size());
            listOfUsers.add(userDirectoryBean);
            //Log.d(TAG, "listOfUsers size after adding new user = " + listOfUsers.size());
            Gson gson = new Gson();
            String jsonString = gson.toJson(listOfUsers);

            long insertedRows = dbHelper.insertApiResponse(URLs.API_NAME_USER_DIRECTORY, jsonString);
            //Log.d(TAG, insertedRows + " Rows inserted");
            DialogUtil.showToastShort(AddUserActivity.this, responseMsg);
            Globals.getInstance().setUserAdded(true);
            finish();
        } else {
            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_users_not_found));
        }
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