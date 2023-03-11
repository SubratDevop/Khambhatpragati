package com.khambhatpragati.parser;

import android.util.Log;

import com.khambhatpragati.model.response.AdvertismentResponse;
import com.khambhatpragati.model.response.CategoryBean;
import com.khambhatpragati.model.response.GroupsResponse;
import com.khambhatpragati.model.response.MessagesBean;
import com.khambhatpragati.model.response.MessagesResponse;
import com.khambhatpragati.model.response.GenericResponse;
import com.khambhatpragati.model.response.OTPVerificationResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khambhatpragati.model.response.UserDirectoryBean;
import com.khambhatpragati.model.response.UserDirectoryResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by bhagvatee1.gupta on 21-12-2016.
 */

public class JSONParser {

    private static final String TAG = JSONParser.class.getName();

    public static GenericResponse parseGenericResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<GenericResponse>() {
        }.getType();
        GenericResponse genericResponse = gson.fromJson(jsonResponse, type);
        return genericResponse;
    }

    public static OTPVerificationResponse parseOTPVerificationResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<OTPVerificationResponse>() {
        }.getType();
        OTPVerificationResponse otpVerificationResponse = gson.fromJson(jsonResponse, type);
        return otpVerificationResponse;
    }

    public static GroupsResponse parseGroupsResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<GroupsResponse>() {
        }.getType();
        GroupsResponse groupsResponse = gson.fromJson(jsonResponse, type);
        return groupsResponse;
    }

    public static AdvertismentResponse parseAdsResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<AdvertismentResponse>() {
        }.getType();
        AdvertismentResponse groupsResponse = gson.fromJson(jsonResponse, type);
        return groupsResponse;
    }
    public static MessagesResponse parseAllMessagesResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<MessagesResponse>() {
        }.getType();
        MessagesResponse messagesResponse = gson.fromJson(jsonResponse, type);
        return messagesResponse;
    }

    public static ArrayList<CategoryBean> parseCategoryResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CategoryBean>>() {
        }.getType();
        ArrayList<CategoryBean> categoryResponse = gson.fromJson(jsonResponse, type);
        return categoryResponse;
    }

    public static ArrayList<MessagesBean> parseMessageList(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MessagesBean>>() {
        }.getType();
        ArrayList<MessagesBean> msgList = gson.fromJson(jsonResponse, type);
        return msgList;
    }

    public static UserDirectoryResponse parseUserDirectoryResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserDirectoryResponse>() {
        }.getType();
        UserDirectoryResponse userDirectoryResponse = gson.fromJson(jsonResponse, type);
        return userDirectoryResponse;
    }

    public static ArrayList<UserDirectoryBean> parseUserDirectoryList(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserDirectoryBean>>() {
        }.getType();
        ArrayList<UserDirectoryBean> userList = gson.fromJson(jsonResponse, type);
        return userList;
    }
}