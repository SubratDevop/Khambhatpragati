package com.khambhatpragati.application;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.khambhatpragati.fcm.SharedPrefManager;
import com.khambhatpragati.model.response.GroupsBean;

/**
 * Created by bhagvatee1.gupta on 22-12-2016.
 */

public class Globals extends MultiDexApplication {

    /**
     * Application object as a singleton object
     */
    private static Globals singleton;
    private String selectedCategoryName;
    private GroupsBean groupsBean;
    private boolean isMsgSent = false;
    private RequestQueue mRequestQueue;
    private SharedPrefManager sharedPrefManager;


    ///////////////////////Variables and Getter/Setter/////////////////////////////////////
    private boolean isUserAdded;

    /**
     * Below are all Global Variables.
     */

    public static Globals getInstance() {
        return singleton;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public SharedPrefManager getSharedPrefManager() {
        if (sharedPrefManager == null)
            sharedPrefManager = new SharedPrefManager(this);
        return sharedPrefManager;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public String getSelectedCategoryName() {
        return selectedCategoryName;
    }

    public void setSelectedCategoryName(String selectedCategoryName) {
        this.selectedCategoryName = selectedCategoryName;
    }

    public GroupsBean getGroupsBean() {
        return groupsBean;
    }

    public void setGroupsBean(GroupsBean groupsBean) {
        this.groupsBean = groupsBean;
    }

    public boolean isMsgSent() {
        return isMsgSent;
    }

    public void setMsgSent(boolean msgSent) {
        isMsgSent = msgSent;
    }

    public boolean isUserAdded() {
        return isUserAdded;
    }

    public void setUserAdded(boolean userAdded) {
        isUserAdded = userAdded;
    }
}