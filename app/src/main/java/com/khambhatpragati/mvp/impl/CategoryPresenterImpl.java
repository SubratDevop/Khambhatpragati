package com.khambhatpragati.mvp.impl;

import android.content.Context;
import android.util.Log;

import com.khambhatpragati.constants.URLs;
import com.khambhatpragati.mvp.presenter.CategoryPresenter;
import com.khambhatpragati.mvp.view.MVPView;
import com.khambhatpragati.service.ServiceConnector;
import com.khambhatpragati.utils.AppUtil;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public class CategoryPresenterImpl implements CategoryPresenter {

    private static final String TAG = CategoryPresenterImpl.class.getName();

    private MVPView mvpView;

    public CategoryPresenterImpl(MVPView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void getAllCategory(Context context) {
        Log.d(TAG, "---------getAllCategory() Called--------");
        if (AppUtil.isOnline(context)) {
            mvpView.onStartProgress();
            ServiceConnector svc = new ServiceConnector(mvpView);
            svc.get(context, URLs.CATEGORY_URL);
        } else {
            mvpView.onInternetConnectionError(false);
        }
    }
}
