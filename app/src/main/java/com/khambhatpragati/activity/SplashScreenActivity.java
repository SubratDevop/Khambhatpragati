package com.khambhatpragati.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import static com.khambhatpragati.constants.Keys.KEY_IS_LOGGED_IN;
import com.khambhatpragati.R;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.utils.DialogUtil;

import static com.khambhatpragati.constants.Keys.KEY_CATEGORY_RESPONSE;

public class SplashScreenActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = SplashScreenActivity.class.getName();
    private static int SPLASH_TIME_OUT = 1500;
    private RelativeLayout relativeLayout;
    public static String fcmToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initView();
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferencesManager.getBoolean(getApplicationContext(), KEY_IS_LOGGED_IN)) {
                    Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, MobileVerificationActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

        //if (PreferencesManager.getString(SplashScreenActivity.this, KEY_CATEGORY_RESPONSE) == null) {
//            CategoryPresenter presenter = new CategoryPresenterImpl(this);
//            presenter.getAllCategory(SplashScreenActivity.this);
//        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//        }
//        if (AppUtil.isOnline(SplashScreenActivity.this)) {
//            CategoryPresenter presenter = new CategoryPresenterImpl(this);
//            presenter.getAllCategory(SplashScreenActivity.this);
//        } else {
//            new Handler().postDelayed(new Runnable() {
//         <string name="hint_mobile_no">Mobile No.</string>       @Override
//                public void run() {
//                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//        }
    }

    @Override
    public void onStartProgress() {
    }

    @Override
    public void onSuccess(String response) {
        if (response != null) {
            PreferencesManager.put(SplashScreenActivity.this, KEY_CATEGORY_RESPONSE, response);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            DialogUtil.showSnackBar(relativeLayout, getString(R.string.msg_auto_sync_failed));
        }
    }

    @Override
    public void onError(String errorMessage) {
        DialogUtil.showSnackBarWithAction(relativeLayout, errorMessage);
    }

    @Override
    public void onInternetConnectionError(boolean isOnline) {
        DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_no_internet));
    }
}