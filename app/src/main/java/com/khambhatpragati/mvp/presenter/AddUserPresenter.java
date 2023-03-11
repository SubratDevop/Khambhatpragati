package com.khambhatpragati.mvp.presenter;

import android.content.Context;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public interface AddUserPresenter {
    void addUser(Context context, String in_userPhone, String in_userName, String in_adminPhone, String in_mandalId);
}