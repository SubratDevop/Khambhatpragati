package com.khambhatpragati.model.response;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class GroupsBean {

    private String validOTP;
    private String mandalID;
    private String mandalName;
    private String accessLevel;
    private String expiry;

    public String getValidOTP() {
        return validOTP;
    }

    public void setValidOTP(String validOTP) {
        this.validOTP = validOTP;
    }

    public String getMandalID() {
        return mandalID;
    }

    public void setMandalID(String mandalID) {
        this.mandalID = mandalID;
    }

    public String getMandalName() {
        return mandalName;
    }

    public void setMandalName(String mandalName) {
        this.mandalName = mandalName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}