package com.asia.yongyou.yongyouagent.utils;


import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Json转Object工具类
 */
public class ParseJsonUtils {

    private static ParseJsonUtils parseJsonUtils;
    public static ParseJsonUtils getInstance(){
        if(parseJsonUtils==null)
            parseJsonUtils = new ParseJsonUtils();
        return parseJsonUtils;
    }



    public List<?> getObjList(String arrayStr , Class clazz){
        try {
            JSONObject obj = new JSONObject(arrayStr);
            JSONArray jsonArray = obj.getJSONArray("data");
            return JSON.parseArray(jsonArray.toString(), clazz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getObject(String strObj, Class clazz){

        return JSON.parseObject(strObj, clazz);
    }

}
