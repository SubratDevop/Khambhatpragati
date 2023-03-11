package com.khambhatpragati.mvp.view;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 * Following MVP Design pattern - View
 */

public interface MVPView {

    public void onStartProgress();

    public void onSuccess(String response);

    public void onError(String errorMessage);

    public void onInternetConnectionError(boolean isOnline);
}
