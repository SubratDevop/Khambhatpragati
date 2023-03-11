package com.khambhatpragati.mvp.impl;

import android.content.Context;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.GetGroupsPresenter;
import com.khambhatpragati.mvp.presenter.GetVersionUpdatePresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class GetVersionUpdatePresenterImpl implements GetVersionUpdatePresenter {

    private static final String TAG = GetVersionUpdatePresenterImpl.class.getName();

    private MVPView mvpView;

    public GetVersionUpdatePresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void getVersionUpdate(Context context, String current_version) {
        //Log.d(TAG, "---------current_version Called--------");
        if (AppUtil.isOnline(context)) {
            //mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.GET_VERSIONUPDATE_URL, prepareRequest(current_version));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String current_version) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("version", current_version);
        hashMap.put("platform", "Android");
        return hashMap;
    }
}