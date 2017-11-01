package com.asia.yongyou.yongyouagent.utils;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
    
    public static String StringTrim(String text){
        if("".equals(text)) return "";
        else return text.trim();
    }

    /**
     * 获得resource string
     * @param activity
     * @param notLogin
     * @return
     */
    public static CharSequence getResource(Activity activity, int notLogin)
    {
    	if(activity!=null)
    		return activity.getResources().getString(notLogin);
    	else 
    		return "";
    }
    
    /**
     * 数字在字符串中的结束位置
     *  起始位置：start:(getIndex-getPubNum); end:getIndex();
     * @param str
     * @return
     */
    public static Integer getEndIndex(String str){
        int index = -1;
        for(int i=0; i<str.length(); i++){
            if('0'<=str.charAt(i) && str.charAt(i)<='9'){
                index=i;
            }
        }
        return index+1;
    }
    
    /**
     *  数字在字符串中的起始位置
     * @param str
     * @return
     */
    public static Integer getStartIndex(String str){
        System.out.println(">>>>>>>>>>> getPubNum:"+getPubNum(str));
        System.out.println(">>>>>>>>>>> getEndIndex:"+getEndIndex(str));
        return getEndIndex(str)-getPubNum(str);
    }
    
    /**
     * 截取字符串中的数字
     * @param num
     * @return
     */
    public static int getPubNum(String num){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(num);
        return m.replaceAll("").trim().length();
    }
    
    
    public static Integer getFirstIndex(String str){
        int index = -1;
        for(int i=0; i<str.length(); i++){
            if('0'<=str.charAt(i) && str.charAt(i)<='9'){
                index=i;
                return index;
            }
        }
        return index;
    }
    
    public static int getLastIndex(String str){
        int index = 0;
        for(int i=0; i<str.length(); i++){
            if('0'<=str.charAt(i) && str.charAt(i)<='9'){
                index=i;
            }
        }
        return index+1;
    }
    
    public static List<String> getNumbers(String content)
    {
        List<String> lists = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
        if (!"".equals(matcher.group()))
            lists.add(matcher.group());
        }
        return lists;
    }
    
    public static String getToday(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date());
    }
    
}
