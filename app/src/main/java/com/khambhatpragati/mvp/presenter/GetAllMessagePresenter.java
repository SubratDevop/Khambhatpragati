package com.khambhatpragati.mvp.presenter;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by bhagvatee1.gupta on 28-11-2016.
 */

public interface GetAllMessagePresenter {
    void getAllMessages(Context context, ArrayList<String> listOfMandalId, String in_userPhone, String lastModifiedTime);
}