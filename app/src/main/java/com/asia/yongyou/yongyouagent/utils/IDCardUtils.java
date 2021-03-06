package com.asia.yongyou.yongyouagent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.asia.yongyou.yongyouagent.constant.Constant;

public class IDCardUtils {
    public static String getIp(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("ip", Constant.ip);
    }

    public static int getPort(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getInt("port", Constant.port);
    }

    public static String getAccount(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("acc", Constant.acc);
    }

    public static String getPassword(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("pwd", Constant.pwd);
    }

    public static boolean getIsWss(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getBoolean("wss", true);
    }

    public static void setIsWss(Context context, boolean flag) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("wss", flag);
        editor.commit();
    }
}
