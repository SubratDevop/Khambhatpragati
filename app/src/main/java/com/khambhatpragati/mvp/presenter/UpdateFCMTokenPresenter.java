package com.khambhatpragati.mvp.presenter;

import android.content.Context;

public interface UpdateFCMTokenPresenter {
    void updateFCMToken(Context context, String mobileNo, String deviceId);
}
