package com.asia.yongyou.yongyouagent.utils;

import android.text.TextUtils;

/**
 * 判断格式是否正确
 * @author ailk
 *
 */
public class NumUtil {
    
    
    /**
     * 判断手机号码格式是否正确
     */
    public static boolean isPhoneNum(String phoneNum) {
        if(!TextUtils.isEmpty(phoneNum)&&phoneNum.length()==11&&validateSeriesCode(phoneNum)){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 判断是否是数字
     * @param code
     * @return
     */
    private static boolean validateSeriesCode(String code){
        boolean flag=true;
        for(int i=0;i<code.length();i++){
            char char_code=code.charAt(i);
            if(char_code<'0'||char_code>'9'){
                flag=false;
                break;
            }
        }
        return flag;
    }
}
