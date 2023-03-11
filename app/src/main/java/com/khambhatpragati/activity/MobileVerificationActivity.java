package com.khambhatpragati.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import static com.khambhatpragati.activity.SplashScreenActivity.fcmToken;
import static com.khambhatpragati.constants.Keys.KEY_FCM_TOKEN;
import static com.khambhatpragati.constants.Keys.KEY_MOBILE_NO;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.khambhatpragati.R;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.response.GenericResponse;
import com.khambhatpragati.mvp.impl.MobileVerificationPresenterImpl;
import com.khambhatpragati.mvp.presenter.MobileVerificationPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

public class MobileVerificationActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = MobileVerificationActivity.class.getName();

    private RelativeLayout relativeLayout;
    private TextInputLayout inputLayoutMobileNo;
    private EditText etMobileNo;
    private Button btnOk;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        initToolBar();
        initView();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_mobile_verification);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_drawer));
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        inputLayoutMobileNo = (TextInputLayout) findViewById(R.id.inputLayoutMobileNo);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        btnOk = (Button) findViewById(R.id.btnOk);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MobileVerificationActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                //chetana
                if(fcmToken == null)
                    fcmToken="";

                fcmToken = instanceIdResult.getToken();
                Log.e("newToken",fcmToken);

                if(fcmToken.equalsIgnoreCase("")) {
                    fcmToken=PreferencesManager.getString(MobileVerificationActivity.this, KEY_FCM_TOKEN);
                }else {
                    PreferencesManager.put(MobileVerificationActivity.this, KEY_FCM_TOKEN, fcmToken);
                }
            }
        });

        addListenerOnWidgets();
    }

    private void addListenerOnWidgets() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide virtual keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager!=null)
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                attemptMobileVerification();
            }
        });
    }

    private void attemptMobileVerification() {
        boolean isLogin = true;

        if (etMobileNo.getText().length() == 10) {
            inputLayoutMobileNo.setErrorEnabled(false);
        } else {
            isLogin = false;
            inputLayoutMobileNo.setError(getString(R.string.msg_valid_mobile_no));
        }

        if (isLogin) {
            MobileVerificationPresenter presenter = new MobileVerificationPresenterImpl(this);
            presenter.verifyMobileNumber(MobileVerificationActivity.this, etMobileNo.getText().toString());
        }
    }

    @Override
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(MobileVerificationActivity.this);
    }

    @Override
    public void onSuccess(String response) {
        ProgressDialogView.hideProgressDialog();
        if (response != null) {
            GenericResponse genericResponse = JSONParser.parseGenericResponse(response);
//            Log.d(TAG, "genericResponse.isStatus() = " + genericResponse.isStatus());
//            Log.d(TAG, "genericResponse.getMessage() = " + genericResponse.getMessage());
            if (genericResponse.isStatus()) {
                Intent i = new Intent(MobileVerificationActivity.this, OTPVerificationActivity.class);
                PreferencesManager.put(MobileVerificationActivity.this, KEY_MOBILE_NO, etMobileNo.getText().toString());
                startActivity(i);
                overridePendingTransition(R.anim.slide_down, R.anim.stay);
                finish();
            } else {
                DialogUtil.showSnackBar(relativeLayout, genericResponse.getMessage());
            }
        } else {
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