package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.AddUserPresenter;
import com.khambhatpragati.mvp.presenter.SendMessagePresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class AddUserPresenterImpl implements AddUserPresenter {

    private static final String TAG = AddUserPresenterImpl.class.getName();

    private MVPView mvpView;

    public AddUserPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void addUser(Context context, String in_userPhone, String in_userName, String in_adminPhone, String in_mandalId) {
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.ADD_USER_URL, prepareRequest(in_userPhone, in_userName, in_adminPhone, in_mandalId));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest( String in_userPhone, String in_userName, String in_adminPhone, String in_mandalId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("in_userPhone", in_userPhone);
        hashMap.put("in_userName", in_userName);
        hashMap.put("in_adminPhone", in_adminPhone);
        hashMap.put("in_mandalId", in_mandalId);
        return hashMap;
    }
}
