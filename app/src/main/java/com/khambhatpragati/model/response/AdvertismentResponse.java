package com.khambhatpragati.model.response;

import java.util.ArrayList;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class AdvertismentResponse {

    private String status;
    private String response;
    private ArrayList<AdvertismentBean> data;

    public String getMessage() {
        return response;
    }

    public void setMessage(String message) {
        this.response = message;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<AdvertismentBean> getData() {
        return data;
    }

    public void setData(ArrayList<AdvertismentBean> groups) {
        this.data = groups;
    }
}