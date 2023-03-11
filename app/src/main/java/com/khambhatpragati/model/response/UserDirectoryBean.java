package com.khambhatpragati.model.response;

import java.util.Comparator;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class UserDirectoryBean implements Comparable<UserDirectoryBean>{

    private String mandalID;
    private String userName;
    private String accessLevel;
    private String mobileNo;
    private String memberId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMandalID() {
        return mandalID;
    }

    public void setMandalID(String mandalID) {
        this.mandalID = mandalID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public static Comparator<UserDirectoryBean> UserNameComparator = new Comparator<UserDirectoryBean>() {

        public int compare(UserDirectoryBean userBean1, UserDirectoryBean userBean2) {

            String userName1 = userBean1.getUserName().toUpperCase();
            String userName2 = userBean2.getUserName().toUpperCase();

            //ascending order
            return userName1.compareTo(userName2);

            //descending order
            //return userName2.compareTo(userName1);
        }
    };

    public int compareTo(UserDirectoryBean userDirectoryBean) {
//        int notificationId = ((MessagesBean) messageBean).getPushnotificationId();
        //ascending order
        return 0;
        //descending order
        //return compareQuantity - this.quantity;
    }
}