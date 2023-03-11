package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.MobileVerificationPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class MobileVerificationPresenterImpl implements MobileVerificationPresenter {

    private static final String TAG = MobileVerificationPresenterImpl.class.getName();

    private MVPView mvpView;

    public MobileVerificationPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void verifyMobileNumber(Context context, String mobileNo) {
        Log.d(TAG, "---------verifyMobileNumber() Called--------");
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.MOBILE_VERIFICATION_URL, prepareRequest(mobileNo));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String mobileNo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("in_userPhone", mobileNo);
        return hashMap;
    }
}
