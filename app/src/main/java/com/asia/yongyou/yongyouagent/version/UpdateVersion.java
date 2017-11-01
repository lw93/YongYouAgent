package com.asia.yongyou.yongyouagent.version;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.asia.yongyou.yongyouagent.entity.Version;

/**
 *  版本更新
 * @author : chengcs
 * @create_time : 2015年3月23日 下午5:07:53
 * @desc : TODO
 * @update_person:
 * @update_time :
 * @update_desc :
 *
 */
public class UpdateVersion
{
    private static Context mContext ;
    private static UpdateVersion version ;
    private String version_code, apk_url, update_tag ;
    
    public static UpdateVersion instance(Context context) {
        mContext = context ;
        if(version==null) {
            version = new UpdateVersion() ;
        }
        return version ;
    }
    
    
    /**
     *  获取当前版本号
     * @return
     *      version code: 版本号
     */     
    public int getAppVersionCode()
    {
        int versionCode = 0;
        try
        {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }
    
    /**
     * 获取versionName
     * @return
     */
    public String getViersionName(){
        try
        {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    
    public void saveCurrentVersion(Version version) {
        SharedPreferences versionInfo = mContext.getSharedPreferences("version", 0);
        versionInfo.edit().putString("version_code",version.getVersion_code()).commit();  
        versionInfo.edit().putString("apk_url", version.getApk_url()).commit(); 
        versionInfo.edit().putString("update_tag", version.getUpdate_tag()).commit(); 
    }
    
    public UpdateVersion getCurrentVersion() {
        SharedPreferences versionInfo = mContext.getSharedPreferences("version", 0);
        version_code = versionInfo.getString("version_code", "");  
        apk_url = versionInfo.getString("apk_url", "");  
        update_tag = versionInfo.getString("update_tag", "");  
        return version ;
    }
    
    public boolean checkVersionCode() {
        UpdateVersion version = getCurrentVersion() ;
        if(!"".equals(version.getVersionCode())) {
            if(Float.valueOf(version.getVersionCode()) > version.getAppVersionCode()) {
                return true ;
            }
        }
        return false ;
    }
    
    /**
     *  打开浏览器下载
     * @param upk_url 
     *          下载地址
     */
    public void openWeb(String upk_url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");    
        Uri content_url = Uri.parse(upk_url);
        intent.setData(content_url);  
        mContext.startActivity(intent);
    }
    
    public String getVersionCode() {
        return version_code ;
    }
    
    public String getApkUrl() {
        return apk_url ;
    }
    
    public String getUpdateTag() {
        return update_tag ;
    }

}
