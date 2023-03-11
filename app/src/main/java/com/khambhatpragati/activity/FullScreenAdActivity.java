package com.khambhatpragati.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.ImageLoader;
import com.khambhatpragati.R;
import com.khambhatpragati.helper.CustomVolleyRequest;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.utils.DialogUtil;

import static com.khambhatpragati.activity.DashboardActivity.adTimer;
import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;
import static com.khambhatpragati.constants.Keys.KEY_IS_LOGGED_IN;

public class FullScreenAdActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = FullScreenAdActivity.class.getName();
    private static int SPLASH_TIME_OUT = 5000;
    private TextView txtClose;
    ImageView imageView;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_layout);
        initView();
    }

    private void initView() {
        txtClose = findViewById(R.id.txtClose);
        imageView = findViewById(R.id.imageView);
        imageLoader = CustomVolleyRequest.getInstance(FullScreenAdActivity.this).getImageLoader();
        if(!getIntent().getStringExtra("adURL").equalsIgnoreCase("NULL")
                && getIntent().getStringExtra("adURL").startsWith("http"))
        imageLoader.get(getIntent().getStringExtra("adURL"), ImageLoader.getImageListener(imageView, R.mipmap.noimage, android.R.drawable.ic_dialog_alert));
        DashboardActivity.adCounter++;
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                adTimer.start();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    finish();
                    adTimer.start();
            }
        }, Integer.parseInt(getIntent().getStringExtra("adTIMEOUT"))*1000);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!getIntent().getStringExtra("adWebLink").equalsIgnoreCase("")
                    && !getIntent().getStringExtra("adWebLink").equalsIgnoreCase("")) {
                    Intent intent = new Intent(FullScreenAdActivity.this, WebviewActivity.class);
                    intent.putExtra("strValue", getIntent().getStringExtra("adWebLink"));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onStartProgress() {
    }

    @Override
    public void onSuccess(String response) {

    }

    @Override
    public void onError(String errorMessage) {
        //DialogUtil.showSnackBarWithAction(relativeLayout, errorMessage);
    }

    @Override
    public void onInternetConnectionError(boolean isOnline) {
        //DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_no_internet));
    }
}