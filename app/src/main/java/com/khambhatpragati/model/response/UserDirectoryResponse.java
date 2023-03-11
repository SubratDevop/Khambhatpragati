package com.khambhatpragati.model.response;

import java.util.ArrayList;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class UserDirectoryResponse {

    private boolean status;
    private String message;
    private ArrayList<UserDirectoryBean> users;

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

    public ArrayList<UserDirectoryBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserDirectoryBean> users) {
        this.users = users;
    }
}