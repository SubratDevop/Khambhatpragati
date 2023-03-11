package com.khambhatpragati.model.response;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class CategoryBean {
    private String CareTakerID;
    private String CategoryID;
    private String CareTakerName;
    private String MobileNumber;
    private String CategoryName;
    private String locationName;

    public String getCareTakerID() {
        return CareTakerID;
    }

    public void setCareTakerID(String careTakerID) {
        CareTakerID = careTakerID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCareTakerName() {
        return CareTakerName;
    }

    public void setCareTakerName(String careTakerName) {
        CareTakerName = careTakerName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
