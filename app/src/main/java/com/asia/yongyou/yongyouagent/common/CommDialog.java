package com.asia.yongyou.yongyouagent.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;


/**
 * 定义Dialog样式
 * @author : chengcs
 * @create_time : 2015年3月24日 下午4:45:48
 * @desc : TODO
 * @update_person:
 * @update_time :
 * @update_desc :
 *
 */
public class CommDialog
{
   
    
    /**
     *  只包含确认按钮
     * @param context
     * @param msg 提示消息
     * @param sureText 确认按钮
     */
    public static void showDialog(Context context, String msg, String sureText){
        final Dialog alertDialog = new Dialog(context, R.style.dialog);
        View sampleView = LayoutInflater.from(context).inflate(R.layout.sample_dialog, null);
        TextView msgText = (TextView)sampleView.findViewById(R.id.dialog_sample_msg);
        msgText.setText(msg);
        Button button = (Button)sampleView.findViewById(R.id.dialog_sample_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.setContentView(sampleView);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    
    public static void showDialog(Context context, String msg, String sureText, final DialogOneCallBack callBack){
        final Dialog alertDialog = new Dialog(context, R.style.dialog);
        View sampleView = LayoutInflater.from(context).inflate(R.layout.sample_dialog, null);
        TextView msgText = (TextView)sampleView.findViewById(R.id.dialog_sample_msg);
        msgText.setText(msg);
        Button button = (Button)sampleView.findViewById(R.id.dialog_sample_btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                alertDialog.dismiss();
                callBack.callBack(alertDialog);
            }
        });
        button.setText(sureText);
        alertDialog.setContentView(sampleView);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    
    /**
     *  默认确认和取消
     * @param context
     * @param msg 提示内容
     */
    @SuppressLint("NewApi")
    public static void showDialog(Context context, String msg, final DialogCallBack dialogCallBack){
        final Dialog defaultDialog = new Dialog(context, R.style.dialog);
        View defaultView = LayoutInflater.from(context).inflate(R.layout.dialog_defalut, null);
        TextView msgText = (TextView)defaultView.findViewById(R.id.dialog_default_msg);
        msgText.setText(msg);
        Button leftBtn = (Button)defaultView.findViewById(R.id.dialog_default_left_btn);
        leftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.cancle(defaultDialog);
            }
        });
        Button rightBtn = (Button)defaultView.findViewById(R.id.dialog_default_right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.open(defaultDialog);
            }
        });
        
        defaultDialog.setCancelable(false);
        defaultDialog.setContentView(defaultView);
        defaultDialog.show();
        defaultDialog.getWindow().setGravity(Gravity.CENTER);
    }
    
    /**
     * @param context 上下文对象
     * @param msg 提示内容
     * @param leftBtnText 左按钮文字
     * @param rightBtnText 右按钮文字
     */
    public static void showDialog(Context context, String msg, String leftBtnText, String rightBtnText, final DialogCallBack dialogCallBack){
        final Dialog defaultDialog = new Dialog(context, R.style.dialog);
        View defaultView = LayoutInflater.from(context).inflate(R.layout.dialog_defalut, null);
        TextView msgText = (TextView)defaultView.findViewById(R.id.dialog_default_msg);
        msgText.setText(msg);
        Button leftBtn = (Button)defaultView.findViewById(R.id.dialog_default_left_btn);
        leftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.cancle(defaultDialog);
            }
        });
        leftBtn.setText(leftBtnText);
        Button rightBtn = (Button)defaultView.findViewById(R.id.dialog_default_right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.open(defaultDialog);
            }
        });
        rightBtn.setText(rightBtnText);
        defaultDialog.setCancelable(false);
        defaultDialog.setContentView(defaultView);
        defaultDialog.show();
        defaultDialog.getWindow().setGravity(Gravity.CENTER);
    }
    
    /**
     * 
     * 充值Dialog
     * @param context 上下文对象
     * @param msg0 充值号码
     * @param msg1 充值金额
     * @param msg 提示内容
     * @param leftBtnText 左按钮文字
     * @param rightBtnText 右按钮文字
     */
    public static void showDialog(Context context, String msg0, String msg1, String leftBtnText, String rightBtnText, final DialogCallBack dialogCallBack){
        final Dialog defaultDialog = new Dialog(context, R.style.dialog);
        View defaultView = LayoutInflater.from(context).inflate(R.layout.recharg_dialog, null);
        TextView phoneText = (TextView)defaultView.findViewById(R.id.recharg_number);
        phoneText.setText(msg0);
        TextView moneryText = (TextView)defaultView.findViewById(R.id.recharge_monery);
        moneryText.setText(msg1+context.getResources().getString(R.string.yuan));
        Button leftBtn = (Button)defaultView.findViewById(R.id.dialog_default_left_btn);
        leftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.cancle(defaultDialog);
            }
        });
        leftBtn.setText(leftBtnText);
        Button rightBtn = (Button)defaultView.findViewById(R.id.dialog_default_right_btn);
        rightBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogCallBack.open(defaultDialog);
            }
        });
        rightBtn.setText(rightBtnText);
        defaultDialog.setCancelable(false);
        defaultDialog.setContentView(defaultView);
        defaultDialog.show();
        defaultDialog.getWindow().setGravity(Gravity.CENTER);
    }

    
    public interface DialogCallBack{
        public void open(Dialog dialog);
        public void cancle(Dialog dialog);
    }
    
    public interface DialogOneCallBack{
        public void callBack(Dialog dialog);
    }
}
