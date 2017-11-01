/**
 * author :  lipan
 * filename :  CheckStatus.java
 * create_time : 2014年4月10日 上午11:26:46
 */
package com.asia.yongyou.yongyouagent.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.common.CommDialog;

/**
 * @create_time : 2014年4月10日 上午11:26:46
 * @desc : 检查手机状态
 * @update_time :
 * @update_desc :
 * 
 */
public class CheckPhoneStatus
{
    /**
     * 判断wifi是否连接
     * @param context
     * @return
     */
    public static boolean isWIFIConnection(Context context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != mobile && !mobile.isAvailable())
        {
            return false;
        }
        if (null != wifi && !wifi.isAvailable())
        {
            return false;
        }
        return true;
    }

    /**
     * 检查网络连接状态，Monitor network connections (Wi-Vi, GPRS, UMTS, etc.)
     * @param context
     * @return
     */
    public static boolean checkNetWorkStatus(Context context)
    {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected())
        {
            result = true;
        } else
        {
            result = false;
        }
        return result;
    }
    
    /**
     *  检查网络连接
     * @param activity
     * @return
     */
    public static boolean checkNetworkWithDialog(final Activity activity){
        
        if (!checkNetWorkStatus(activity))
        {
            CommDialog.showDialog(activity, StringUtils.getResource(activity, R.string.network_disable).toString(), new CommDialog.DialogCallBack()
            {
                @Override
                public void open(Dialog dialog)
                {
                    // TODO Auto-generated method stub
                    if(android.os.Build.VERSION.SDK_INT > 10 ){
                        //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                    dialog.dismiss();
                }
                
                @Override
                public void cancle(Dialog dialog)
                {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    activity.finish();
                }
            });
        } else{
            return true; 
        }
        return false;
    }

    
}
