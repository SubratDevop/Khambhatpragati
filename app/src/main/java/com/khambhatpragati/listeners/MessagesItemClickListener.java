package com.khambhatpragati.listeners;

import android.view.View;

import com.khambhatpragati.model.response.MessagesBean;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public interface MessagesItemClickListener {
    void onClick(View view, int position, MessagesBean allMessagesBean);
}
