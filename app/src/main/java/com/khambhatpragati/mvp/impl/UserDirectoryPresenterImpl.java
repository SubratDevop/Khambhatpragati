package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.GetGroupsPresenter;
import com.khambhatpragati.mvp.presenter.UserDirectoryPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class UserDirectoryPresenterImpl implements UserDirectoryPresenter {

    private static final String TAG = UserDirectoryPresenterImpl.class.getName();

    private MVPView mvpView;

    public UserDirectoryPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void getUserDirectory(Context context, String in_userPhone) {
        if (AppUtil.isOnline(context)) {
            //mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.USER_DIRECTORY_URL, prepareRequest(in_userPhone));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String in_userPhone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("in_userPhone", in_userPhone);
        return hashMap;
    }
}