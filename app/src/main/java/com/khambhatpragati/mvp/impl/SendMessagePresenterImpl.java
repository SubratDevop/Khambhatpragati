package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.SendMessagePresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class SendMessagePresenterImpl implements SendMessagePresenter {

    private static final String TAG = SendMessagePresenterImpl.class.getName();

    private MVPView mvpView;

    public SendMessagePresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }


    @Override
    public void sendMessage(Context context, String mandalId, String msgTitle, String msgDesc, String in_userPhone) {
        //Log.d(TAG, "---------sendMessage() Called--------");
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.post(context, URLs.SEND_MESSAGES_URL, prepareRequest(mandalId, msgTitle, msgDesc, in_userPhone));
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }

    private HashMap<String, Object> prepareRequest(String mandalId, String msgTitle, String msgDesc, String in_userPhone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mandalID", mandalId);
        hashMap.put("msgTitle", msgTitle);
        hashMap.put("msgDesc", msgDesc);
        hashMap.put("in_userPhone", in_userPhone);
        return hashMap;
    }
}
