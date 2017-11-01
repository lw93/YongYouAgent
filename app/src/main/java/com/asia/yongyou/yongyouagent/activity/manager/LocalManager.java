package com.asia.yongyou.yongyouagent.activity.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 本地用户信息存储类
 *
 * @author : chengcs
 * @create_time : 2015年3月25日 下午2:59:58
 * @desc : TODO
 * @update_person:
 * @update_time :
 * @update_desc :
 */
public class LocalManager {

    private Context mContext;
    private static LocalManager localManager = null;

    public static final String packageName = "com.asia.yongyou.yongyouagent";

    private LocalManager(Context context) {
        this.mContext = context;
    }

    ;

    public static LocalManager getInstance(Context context) {
        if (null == localManager) {
            localManager = new LocalManager(context);
        }
        return localManager;
    }

    /**
     * login保存
     * 代理商工号和的密码
     *
     * @param agentNum
     * @param password
     */
    public void saveUserPreferences(String agentNum, String password) {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
        editor.putString("agent_num", agentNum);
        editor.putString("agent_password", password);
        editor.commit();
    }

    public void saveAgentNum(String agentNum) {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
        editor.putString("agent_num", agentNum);
        editor.commit();
    }

    public void saveAgentPassword(String password) {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
        editor.putString("agent_password", password);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(packageName, Context.MODE_PRIVATE);
    }


    public void clearPreferences() {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
        editor.putString("agent_num", "");
        editor.putString("agent_password", "");
        editor.commit();
    }

    /**
     * logout清空
     */
    public void clearUserPreferences() {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
//        editor.putString("agent_num", "");
        editor.putString("agent_password", "");
        editor.commit();
    }


    /**
     * 手机
     *
     * @return
     */
    public String getAgentNum() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString("agent_num", "");
    }

    /**
     * 密码
     *
     * @return
     */
    public String getAgentPassword() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString("agent_password", "");
    }

    /**
     * 保存校验手机号
     * @param archivePhone
     */
    public void saveArchivePhone(String archivePhone) {
        SharedPreferences preferences = getSharedPreferences();
        Editor editor = preferences.edit();
        editor.putString("archive_phone", archivePhone);
        editor.commit();
    }

    /**
     * 获取校验手机号
     * @return
     */
    public String getArchivePhone() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString("archive_phone", "");
    }


}
