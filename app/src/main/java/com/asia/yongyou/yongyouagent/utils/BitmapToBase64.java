package com.asia.yongyou.yongyouagent.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 把图片转换成Base64字符串
 */
public class BitmapToBase64 {

    public static String imgToBase64(String imgPath,Bitmap bitmap) {
        if (imgPath!=null && imgPath.length()>0) {
            bitmap = readBitmap(imgPath);
        }
        if (imgPath ==null) {}
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes,Base64.DEFAULT);

        } catch (IOException e) {
            return null;

        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        }catch(Exception e){
            return null;
        }
    }

    public static String toBase64Encode(Bitmap msg){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte b []= baos.toByteArray();

        return Base64.encodeToString(b,Base64.DEFAULT);

    }


    public static String BitmaptoBase64Encode(Bitmap msg) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        msg.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte b []=baos.toByteArray();
        return Base64.encodeToString(b,Base64.NO_WRAP);
    }
}