package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.GetAllMessagePresenter;
import com.khambhatpragati.mvp.presenter.GetGroupsPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class GetGroupsPresenterImpl implements GetGroupsPresenter {

    private static final String TAG = GetGroupsPresenterImpl.class.getName();

    private MVPView mvpView;

    public GetGroupsPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void getGroups(Context context, String in_userPhone) {
        //Log.d(TAG, "---------getGroups() Called--------");
        if (AppUtil.isOnline(context)) {
            //mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.GET_GROUPS_URL, prepareRequest(in_userPhone));
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