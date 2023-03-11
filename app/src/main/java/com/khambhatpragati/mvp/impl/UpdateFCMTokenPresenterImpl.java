package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.UpdateFCMTokenPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

public class UpdateFCMTokenPresenterImpl implements UpdateFCMTokenPresenter {
    private static final String TAG = UpdateFCMTokenPresenterImpl.class.getName();

    private MVPView mvpView;

    public UpdateFCMTokenPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void updateFCMToken(Context context, String mobileNo, String deviceId) {
        Log.d(TAG, "---------verifyMobileNumber() Called--------");
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.UPDATE_FCM_TOKEN_URL, prepareRequest(mobileNo, deviceId));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String mobileNo, String deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("in_userPhone", mobileNo);
        hashMap.put("in_deviceID", deviceId);
        return hashMap;
    }
}
