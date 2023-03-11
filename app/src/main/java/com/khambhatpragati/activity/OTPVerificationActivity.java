package com.khambhatpragati.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.khambhatpragati.R;
import com.khambhatpragati.helper.PreferencesManager;
import com.khambhatpragati.model.User;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.khambhatpragati.mvp.impl.OTPVerificationPresenterImpl;
import com.khambhatpragati.mvp.presenter.OTPVerificationPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.parser.JSONParser;
import com.khambhatpragati.preference.UserPreference;
import com.khambhatpragati.utils.DialogUtil;
import com.khambhatpragati.view.ProgressDialogView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.khambhatpragati.activity.SplashScreenActivity.fcmToken;
import static com.khambhatpragati.constants.Keys.KEY_IS_LOGGED_IN;
import static com.khambhatpragati.constants.Keys.KEY_MOBILE_NO;

//import com.google.firebase.iid.FirebaseInstanceId;

public class OTPVerificationActivity extends AppCompatActivity implements MVPView {

    private static final String TAG = OTPVerificationActivity.class.getName();

    private RelativeLayout relativeLayout;
    private TextView tvMobileNo;
    private TextInputLayout inputLayoutOTP;
    private EditText etOTP;
    private Button btnVerify;

    private String mobileNo;
    //private String fcmToken="bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1";
    private Toolbar toolbar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        initToolBar();

//        initFCMReceiver();
        initView();
    }

    //    public void initFCMReceiver() {
//        //Initializing our broadcast receiver
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//
//            //When the broadcast received
//            //We are sending the broadcast from GCMRegistrationIntentService
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //If the broadcast has received with success
//                //that means device is registered successfully
//                if (intent.getAction().equals(FCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
//                    //Getting the registration token from the intent
//                    fcmToken = intent.getStringExtra("token");
//                    //Displaying the token as toast
//                    Toast.makeText(getApplicationContext(), "Registration token:" + fcmToken, Toast.LENGTH_LONG).show();
//
//                    //if the intent is not with success then displaying error messages
//                } else if (intent.getAction().equals(FCMRegistrationIntentService.REGISTRATION_ERROR)) {
//                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        //Checking play service is available or not
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        //if play service is not available
//        if (ConnectionResult.SUCCESS != resultCode) {
//            //If play service is supported but not installed
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
//                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
//
//                //If play service is not supported
//                //Displaying an error message
//            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
//            }
//
//            //If play service is available
//        } else {
//            //Starting intent to register device
//            Intent itent = new Intent(this, FCMRegistrationIntentService.class);
//            startService(itent);
//        }
//    }
    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_title_otp_verification);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_drawer));
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tvMobileNo = (TextView) findViewById(R.id.tvMobileNo);
        inputLayoutOTP = (TextInputLayout) findViewById(R.id.inputLayoutOTP);
        etOTP = (EditText) findViewById(R.id.etOTP);
        btnVerify = (Button) findViewById(R.id.btnVerify);

        mobileNo = PreferencesManager.getString(OTPVerificationActivity.this, KEY_MOBILE_NO);

        tvMobileNo.setText(mobileNo);
        addListenerOnWidgets();
        //getGCMToken();
    }

    private void addListenerOnWidgets() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide virtual keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                attemptOTPVerification();
            }
        });
    }

//    private void getGCMToken() {
//        fcmToken = FirebaseInstanceId.getInstance().getToken(); //"dcWiQsBIIJA:APA91bFcWokd2YruIpEPM3T42ThhawkX03G95sS7yErIFNfLYv9ayhaTc8PrX5RIx8_9Xnsgh3dzlGkQuxGjxQzA9ujLY5YCJawwhF_i6_KUJ2dN2qLAuV6oE3ImqcLm5sBjq0roqf68";//
////        Toast.makeText(OTPVerificationActivity.this, "Current token [" + fcmToken + "]", Toast.LENGTH_LONG).show();
//        Log.d(TAG, "Token [" + fcmToken + "]");
//    }

    private void attemptOTPVerification() {
        boolean isValidation = true;

        inputLayoutOTP.setErrorEnabled(false);

        if (etOTP.getText().length() != 4) {
            inputLayoutOTP.setError(getString(R.string.msg_valid_otp));
            isValidation = false;
        }

//        if (fcmToken == null || fcmToken.length() <= 0) {
//            isValidation = false;
//            DialogUtil.showSnackBarWithAction(relativeLayout, getString(R.string.msg_failed_to_fetch_fcm_token));
//        }

        if (isValidation) {
            OTPVerificationPresenter presenter = new OTPVerificationPresenterImpl(this);
            presenter.verifyOTP(OTPVerificationActivity.this, mobileNo, etOTP.getText().toString(), fcmToken);
        }
    }

    @Override
    public void onStartProgress() {
        ProgressDialogView.showProgressDialog(OTPVerificationActivity.this);
    }

    @Override
    public void onSuccess(String response) {
        ProgressDialogView.hideProgressDialog();
        if (response != null) {
            OTPVerificationResponse otpVerificationResponse = JSONParser.parseOTPVerificationResponse(response);
            //Log.d(TAG, "otpVerificationResponse.isStatus() = " + otpVerificationResponse.isStatus());
            //Log.d(TAG, "otpVerificationResponse.getMessage() = " + otpVerificationResponse.getMessage());
            if (otpVerificationResponse.isStatus()) {
                PreferencesManager.put(getApplicationContext(), KEY_IS_LOGGED_IN, true);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if(jObj!=null){
                        User user = new User();
                        user.setRecordId(jObj.getJSONObject("profile").getString("recordId"));
                        user.setMemberID(jObj.getJSONObject("profile").getString("memberID"));
                        user.setFirstName(jObj.getJSONObject("profile").getString("firstName"));
                        user.setLastName(jObj.getJSONObject("profile").getString("middleName"));
                        user.setMiddleName(jObj.getJSONObject("profile").getString("lastName"));
                        user.setLastKnownName(jObj.getJSONObject("profile").getString("lastKnownName"));
                        user.setParentMemberId(jObj.getJSONObject("profile").getString("parentMemberId"));
                        user.setGrandFatherName(jObj.getJSONObject("profile").getString("grandFatherName"));
                        user.setGender(jObj.getJSONObject("profile").getString("gender"));
                        user.setDateOfBirth(jObj.getJSONObject("profile").getString("dateOfBirth"));
                        user.setMemberStatus(jObj.getJSONObject("profile").getString("memberStatus"));
                        user.setCommitteeMember(jObj.getJSONObject("profile").getString("committeeMember"));
                        user.setBloodGroup(jObj.getJSONObject("profile").getString("bloodGroup"));
                        user.setMaritalStatus(jObj.getJSONObject("profile").getString("maritalStatus"));
                        user.setQualification(jObj.getJSONObject("profile").getString("qualification"));
                        user.setProfession(jObj.getJSONObject("profile").getString("profession"));
                        user.setFlatNoBuilding(jObj.getJSONObject("profile").getString("flatNoBuilding"));
                        user.setResidenceArea(jObj.getJSONObject("profile").getString("residenceArea"));
                        user.setNearestStation(jObj.getJSONObject("profile").getString("nearestStation"));
                        user.setResidenceZone(jObj.getJSONObject("profile").getString("residenceZone"));
                        user.setCity(jObj.getJSONObject("profile").getString("city"));
                        user.setPincode(jObj.getJSONObject("profile").getString("pincode"));
                        user.setKhambhatAddress(jObj.getJSONObject("profile").getString("khambhatAddress"));
                        user.setEmail(jObj.getJSONObject("profile").getString("email"));
                        user.setMobileNo(jObj.getJSONObject("profile").getString("mobileNo"));
                        user.setMembershipStatus(jObj.getJSONObject("profile").getString("membershipStatus"));
                        user.setImageUrl(jObj.getJSONObject("profile").getString("imageUrl"));
                        user.setLastModifiedDate(jObj.getJSONObject("profile").getString("lastModifiedDate"));

                        UserPreference.getInstance(OTPVerificationActivity.this).savePreference(user);

                        Intent i = new Intent(OTPVerificationActivity.this, DashboardActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.stay);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogUtil.showSnackBar(relativeLayout, otpVerificationResponse.getMessage());
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