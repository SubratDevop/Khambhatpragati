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

import static com.khambhatpragati.activity.DashboardActivity.adCounter;
import static com.khambhatpragati.activity.DashboardActivity.adsImageUrls;
import static com.khambhatpragati.activity.DashboardActivity.isAdsTicking;
import static com.khambhatpragati.activity.DashboardActivity.isShownOnce;
import static com.khambhatpragati.constants.Keys.KEY_MOBILE_NO;

import com.khambhatpragati.R;
import com.khambhatpragati.application.Globals;
import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.GroupsBean;
import com.khambhatpragati.model.response.GenericResponse;
import com.khambhatpragati.mvp.impl.SendMessagePresenterImpl;
import com.khambhatpragati.mvp.presenter.SendMessagePresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = SendMessageActivity.class.getName();

    private RelativeLayout relativeLayout;
    private Toolbar toolbar;
    private TextInputLayout inputLayoutMsgTitle;
    private TextInputLayout inputLayoutMsgDesc;
    private EditText etMsgTitle;
    private EditText etMsgDesc;
    private Button btnSend;

    private ImageLoader imageLoader;
    ImageView imageView;
    private GroupsBean groupsBean;
    private String strAdWeblink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        initToolBar();
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_send_messages);
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
        inputLayoutMsgTitle = (TextInputLayout) findViewById(R.id.inputLayoutMsgTitle);
        inputLayoutMsgDesc = (TextInputLayout) findViewById(R.id.inputLayoutMsgDesc);
        etMsgTitle = (EditText) findViewById(R.id.etMsgTitle);
        etMsgDesc = (EditText) findViewById(R.id.etMsgDesc);
        btnSend = (Button) findViewById(R.id.btnSend);

        groupsBean = Globals.getInstance().getGroupsBean();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adsImageUrls.size() > 0) {
                    if (adCounter >= adsImageUrls.size())
                        adCounter = 0;
                    Intent i = new Intent(SendMessageActivity.this, FullScreenAdActivity.class);
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
                    Intent i = new Intent(SendMessageActivity.this, FullScreenAdActivity.class);
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
                    Intent intent = new Intent(SendMessageActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", strAdWeblink);
                    startActivity(intent);
                }
            }
        });
        getBottomAds();
    }
    private void getBottomAds() {
        final ProgressDialog progress = new ProgressDialog(SendMessageActivity.this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

        AQuery aQuery = new AQuery(getApplicationContext());
        Map<String, Object> params = new HashMap<>();
        params.put("advertisement_type","BottomAd");
        params.put("screen_type","MessageDetail Screen");

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
                                            && adsResponse.getData().get(i).getScreen_type().equalsIgnoreCase("MessageDetail Screen")) {
                                        imageView.setVisibility(View.VISIBLE);
                                        strAdWeblink=adsResponse.getData().get(i).getWebsite_url();
                                        imageLoader = CustomVolleyRequest.getInstance(SendMessageActivity.this).getImageLoader();
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
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide virtual keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                attemptSendMessage();
            }
        });
    }

    private void attemptSendMessage() {
        boolean isDataValid = true;

        if (etMsgTitle.getText().toString().isEmpty()) {
            isDataValid = false;
            inputLayoutMsgTitle.setError(getString(R.string.msg_title));
        } else {
            inputLayoutMsgTitle.setError(null);
        }

        if (etMsgDesc.getText().toString().isEmpty()) {
            isDataValid = false;
            inputLayoutMsgDesc.setError(getString(R.string.msg_description));
        } else {
            inputLayoutMsgDesc.setError(null);
        }

        if (isDataValid) {
            SendMessagePresenter presenter = new SendMessagePresenterImpl(this);
            presenter.sendMessage(SendMessageActivity.this, groupsBean.getMandalID(), etMsgTitle.getText().toString(),
                    etMsgDesc.getText().toString(), PreferencesManager.getString(SendMessageActivity.this, KEY_MOBILE_NO));
        }
    }

    @Override
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(SendMessageActivity.this);
    }

    @Override
    public void onSuccess(String response) {
        ProgressDialogView.hideProgressDialog();
        if (response != null) {
            GenericResponse genericResponse = JSONParser.parseGenericResponse(response);
            //Log.d(TAG, "genericResponse.isStatus() = " + genericResponse.isStatus());
            //Log.d(TAG, "genericResponse.getMessage() = " + genericResponse.getMessage());
            if (genericResponse.isStatus()) {
                DialogUtil.showToastLong(SendMessageActivity.this, genericResponse.getMessage());
                Globals.getInstance().setMsgSent(true);
                finish();
            } else {
                Globals.getInstance().setMsgSent(false);
                DialogUtil.showSnackBar(relativeLayout, genericResponse.getMessage());
            }
        } else {
            Globals.getInstance().setMsgSent(false);
            DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_server_error));
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