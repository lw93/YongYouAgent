package com.asia.yongyou.yongyouagent.utils;

import android.util.Log;

import java.util.StringTokenizer;

/**
 * 日志类
 * Created by admin on 2017/8/31.
 */
public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    private static final int LEVEL = VERBOSE;// control the output info level
    private static final String logContentFilter = "logUtil";

    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void logWithMethod(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) {
            i("error", "log: get trace info failed");
        }
        String class_name = getSimpleClassName(trace[0].getClassName());
        i(class_name, logContentFilter + ": " + trace[0].getMethodName()
                + ":" + trace[0].getLineNumber());
    }

    public static void logWithMethod(Exception e, String msg) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) {
            i("error", "log: get trace info failed");
        }
        String class_name = getSimpleClassName(trace[0].getClassName());
        i(class_name, logContentFilter + ": " + trace[0].getMethodName()
                + ":" + trace[0].getLineNumber() + ": " + msg);
    }

    public static String getSimpleClassName(String fullClassName) {
        String split = ".";
        String class_name = "";
        StringTokenizer token = new StringTokenizer(fullClassName, split);
        while (token.hasMoreTokens()) {
            class_name = token.nextToken();
        }
        return class_name;
    }

}
