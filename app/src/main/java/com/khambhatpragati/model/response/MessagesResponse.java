package com.khambhatpragati.model.response;

import java.util.ArrayList;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class MessagesResponse {

    private boolean status;
    private String message;
    private ArrayList<MessagesBean> notificationMessages;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<MessagesBean> getNotificationMessages() {
        return notificationMessages;
    }

    public void setNotificationMessages(ArrayList<MessagesBean> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }
}