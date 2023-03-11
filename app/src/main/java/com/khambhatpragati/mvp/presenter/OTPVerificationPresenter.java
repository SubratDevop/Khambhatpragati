package com.khambhatpragati.mvp.presenter;

import android.content.Context;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public interface OTPVerificationPresenter {
    void verifyOTP(Context context, String mobileNo, String otp, String deviceId);
}