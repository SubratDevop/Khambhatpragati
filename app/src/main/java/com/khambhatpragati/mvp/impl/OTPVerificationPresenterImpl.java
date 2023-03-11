package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.OTPVerificationPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class OTPVerificationPresenterImpl implements OTPVerificationPresenter {

    private static final String TAG = OTPVerificationPresenterImpl.class.getName();

    private MVPView mvpView;

    public OTPVerificationPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void verifyOTP(Context context, String mobileNo, String otp, String deviceId) {
        //Log.d(TAG, "---------verifyMobileNumber() Called--------");
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.OTP_VERIFICATION_URL, prepareRequest(mobileNo, otp, deviceId));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String mobileNo, String otp, String deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("in_userPhone", mobileNo);
        hashMap.put("in_otp", otp);
        hashMap.put("in_deviceID", deviceId);
        return hashMap;
    }
}
