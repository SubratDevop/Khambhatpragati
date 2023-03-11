package com.khambhatpragati.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bhagvatee1.gupta on 24-01-2017.
 */

public class PreferencesManager {

    private static final String PREFS_NAME = "CareTaker";

    public static void put(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static void put(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static void put(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static void removeString(Context context, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.commit();
    }
}