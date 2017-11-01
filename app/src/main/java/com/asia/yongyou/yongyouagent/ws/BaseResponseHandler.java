package com.asia.yongyou.yongyouagent.ws;

import android.app.Activity;

import com.asia.yongyou.yongyouagent.common.BaseProgressBar;

import org.json.JSONObject;

public class BaseResponseHandler implements IWSResponseHandler {

    private Activity activity;
    private static BaseProgressBar baseProgressBar;

    public BaseResponseHandler(Activity activity, boolean showing, String text) {
        if (showing) {
            if (null == baseProgressBar) {
                baseProgressBar = new BaseProgressBar(activity, text);
            }
        }
    }


    @Override
    public void onStart() {
        if (baseProgressBar != null) baseProgressBar.setVisiable();
    }

    @Override
    public void onSuccess(int statusCode, JSONObject response) {

    }

    @Override
    public void onFailure(Throwable error, String content) {
        if (baseProgressBar != null) {
            baseProgressBar.setInVisiable();
            baseProgressBar = null;
        }
    }

    @Override
    public void onFinish() {
        if (baseProgressBar != null) {
            baseProgressBar.setInVisiable();
            baseProgressBar = null;
        }
    }

    @Override
    public void onSuccess(int statusCode, String response) {
    }

}
