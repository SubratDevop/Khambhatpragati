package com.khambhatpragati.mvp.presenter;

import android.content.Context;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public interface SendMessagePresenter {
    void sendMessage(Context context, String mandalId, String msgTitle, String msgDesc, String in_userPhone);
}