package com.khambhatpragati.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;

import com.khambhatpragati.model.response.MessagesBean;
import com.google.gson.Gson;
import com.khambhatpragati.model.response.UserDirectoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by bhagvatee1.gupta on 24-11-2016.
 */

public class AppUtil {


    /**
     * Show the Toolbar of Activity
     *
     * @param activity
     */
    public static void showToolbar(Activity activity, String toolbarTitle) {
        ((AppCompatActivity) activity).getSupportActionBar().show();
        activity.setTitle(toolbarTitle);
    }

    /**
     * Hide the Toolbar of Activity
     *
     * @param activity
     */
    public static void hideToolbar(Activity activity) {
        ((AppCompatActivity) activity).getSupportActionBar().hide();
    }

    /**
     * Reading the Application version name from android manifest.xml file
     *
     * @param context
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Checking the Internet connection either available or not before calling the API and
     * returning the result as boolean.
     *
     * @param context
     * @return boolean
     */
    public static boolean isOnline(Context context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static int getRandomNumber() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }

    public static String convertArrayListToJsonString(ArrayList<MessagesBean> listOfMessages) {
        Gson gson = new Gson();
        String jsonMsgList = gson.toJson(listOfMessages);
        return jsonMsgList;
    }
    public static String convertToJsonString(Map<String, Object> listOfMessages) {
        Gson gson = new Gson();
        String jsonMsgList = gson.toJson(listOfMessages);
        return jsonMsgList;
    }

    public static ArrayList<MessagesBean> sortMsgListByModifiedDate(ArrayList<MessagesBean> listOfMsg) {
        MessagesBean arrMessages[] = listOfMsg.toArray(new MessagesBean[listOfMsg.size()]);
//        Arrays.sort(arrMessages, MessagesBean.ModifiedDateComparator);

        int i = 0;
        for (MessagesBean temp : arrMessages) {
//            System.out.println("Title: " + temp.getTitle() +
//                    ", MandalId : " + temp.getMandalId() + ", ModifiedDateTime : " + temp.getModifiedDateTime());
        }

        return new ArrayList<MessagesBean>(Arrays.asList(arrMessages));
    }

    public static ArrayList<UserDirectoryBean> sortUserDirectoryListByName(ArrayList<UserDirectoryBean> listOfUsers) {
        UserDirectoryBean arrUsers[] = listOfUsers.toArray(new UserDirectoryBean[listOfUsers.size()]);
        Arrays.sort(arrUsers, UserDirectoryBean.UserNameComparator);
        return new ArrayList<UserDirectoryBean>(Arrays.asList(arrUsers));
    }
}