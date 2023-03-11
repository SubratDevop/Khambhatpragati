package com.khambhatpragati.model.response;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Comparator;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class MessagesBean implements Comparable<MessagesBean> {

//    public static Comparator<MessagesBean> ModifiedDateComparator = new Comparator<MessagesBean>() {
//
//        public int compare(MessagesBean msgBean1, MessagesBean msgBean2) {
//
//            String lastModifiedDate1 = msgBean1.getModifiedDateTime()..toUpperCase();
//            String lastModifiedDate2 = msgBean2.getModifiedDateTime().toUpperCase();
//
//            //ascending order
//            //return lastModifiedDate1.compareTo(lastModifiedDate2);
//
//            //descending order
////            return lastModifiedDate2.compareTo(lastModifiedDate1);
//            return lastModifiedDate1.compareTo(lastModifiedDate2);
//        }
//    };

    private String PushnotificationId;
    private String Title;
    private String Description;
    private String MandalId;
    private String ModifiedDateTime;
    private String UserPhoneNumber;
    private String UserName;

    public String getPushnotificationId() {
        return PushnotificationId;
    }

    public void setPushnotificationId(String pushnotificationId) {
        PushnotificationId = pushnotificationId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMandalId() {
        return MandalId;
    }

    public void setMandalId(String mandalId) {
        MandalId = mandalId;
    }

    public String getModifiedDateTime() {
//        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(ModifiedDateTime);
        try {
            //current date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            Date objDate = dateFormat.parse(ModifiedDateTime);

            //Expected date format
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

             ModifiedDateTime = dateFormat2.format(objDate);

            Log.d("Final Date Format:", "Final Date:"+ModifiedDateTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ModifiedDateTime;
    }

    public void setModifiedDateTime(String modifiedDateTime) {
        ModifiedDateTime = modifiedDateTime;
    }


    public String getUserPhoneNumber() { return UserPhoneNumber; }

    public void setUserPhoneNumber(String userPhoneNumber) {
        UserPhoneNumber = userPhoneNumber;
    }

    public String getUserName() { return UserName; }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int compareTo(MessagesBean messageBean) {

//        int notificationId = ((MessagesBean) messageBean).getPushnotificationId();

        //ascending order
        return 0;

        //descending order
        //return compareQuantity - this.quantity;

    }
}
