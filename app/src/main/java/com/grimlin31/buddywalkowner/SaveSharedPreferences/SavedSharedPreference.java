package com.grimlin31.buddywalkowner.SaveSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.grimlin31.buddywalkowner.model.Walker;

public class SavedSharedPreference {
    static final String PREF_USER_NAME= "username";
    static final String PREF_EMAIL= "email";
    static final String PREF_PHONE= "phone";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setWalkerData(Context ctx, @NonNull Walker walker){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, walker.getUsername());
        editor.putString(PREF_EMAIL, walker.getEmail());
        editor.putString(PREF_PHONE, walker.getPhone());
        editor.commit();
    }

    @NonNull
    public static Walker getWalkerData(Context ctx){
        Walker walker = new Walker();
        walker.setEmail(getSharedPreferences(ctx).getString(PREF_EMAIL, ""));
        walker.setPhone(getSharedPreferences(ctx).getString(PREF_PHONE, ""));
        walker.setUsername(getSharedPreferences(ctx).getString(PREF_USER_NAME, ""));
        return walker;
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
