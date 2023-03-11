package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.GetAllMessagePresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;
import com.khambhatpragati.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class GetAllMessagesPresenterImpl implements GetAllMessagePresenter {

    private static final String TAG = GetAllMessagesPresenterImpl.class.getName();

    private MVPView mvpView;

    public GetAllMessagesPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void getAllMessages(Context context, ArrayList<String> listOfMandalId, String in_userPhone, String lastModifiedTime) {
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.GET_ALL_MESSAGES_URL, prepareRequest(listOfMandalId, in_userPhone, lastModifiedTime));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(ArrayList<String> listOfMandalId, String in_userPhone, String lastModifiedTime) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mandalIDs", listOfMandalId);
        hashMap.put("in_userPhone", in_userPhone);
        hashMap.put("lastModifiedTime", lastModifiedTime);

        String versionName = BuildConfig.VERSION_NAME;
        hashMap.put("appVersion", versionName);
        return hashMap;
    }
}