package com.asia.yongyou.yongyouagent.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.asia.yongyou.yongyouagent.R;


public class ViewUtils
{
    /**
     * 检查网络连接 Toast方式提醒
     * @param context
     * @return
     */
    public static boolean checkNetworkWithToast(final Context context)
    {
        //判断网络环境
        if(!CheckPhoneStatus.checkNetWorkStatus(context))
        {
            Toast.makeText(context, R.string.network_disable, Toast.LENGTH_LONG).show();
            return false;
        }else
        {
            return true;
        }
    }

    
    /**
     * 隐藏软键盘
     * @param context
     */
    public static void showOrHideSoftKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    public static void showToast(final Activity context, final CharSequence msg){
        context.getWindow().getDecorView().post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
        
//        Looper myLooper = Looper.myLooper();
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//        myLooper.loop();
    }

    /**
     *  将字符串中的数字变大
     * @param text
     */
    public static SpannableStringBuilder changeNumMax(String str)
    {
        // TODO Auto-generated method stub
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new AbsoluteSizeSpan(66), StringUtils.getFirstIndex(str),StringUtils.getEndIndex(str), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#00aa5c")),StringUtils.getFirstIndex(str),StringUtils.getEndIndex(str), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
    
    /**
     * 改变字符串中数字的颜色
     * @param str
     * @return
     */
    public static SpannableStringBuilder changeNumColor(String str)
    {
        // TODO Auto-generated method stub
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#00aa5c")),StringUtils.getFirstIndex(str),StringUtils.getFirstIndex(str)+StringUtils.getNumbers(str).get(0).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#00aa5c")),StringUtils.getLastIndex(str)-StringUtils.getNumbers(str).get(1).length(),StringUtils.getLastIndex(str), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
    
    /**
     * 给view添加触摸透明事件
     * 
     * @param viewToListener
     *            需要添加监听的view
     * @param viewToAlpha
     *            需要改变背景透明值的view
     * 
     *            使用场景：viewToListener为viewToAlpha的外一层layout，
     *            点击外层layout时改变里层Imageview的alpha，如果没有里外层，两个参数都传imageview即可
     *            Imageview包含background属性，而不是src!!!，触摸时改变图片的background透明值
     * 
     */
    public static void addViewTouchAlpha(View viewToListener,
            final View viewToAlpha)
    {
        final int alphaPress = 100;
        final int alphaNormal = 255;
        viewToListener.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:// PRESSED DOWN
                        Drawable backgroundTrans = viewToAlpha.getBackground();
                        if(null == backgroundTrans)
                        {
                            backgroundTrans = ((ImageView)viewToAlpha).getDrawable();
                            backgroundTrans.setAlpha(alphaPress);
                            ((ImageView)viewToAlpha).setImageDrawable(backgroundTrans);
                        }else
                        {
                            backgroundTrans.setAlpha(alphaPress);
                            viewToAlpha.setBackgroundDrawable(backgroundTrans);
                        }
                        break;
                    case MotionEvent.ACTION_UP: // PRESSED UP
                    case MotionEvent.ACTION_CANCEL: // PRESSED UP
                        Drawable backgroundNormal = viewToAlpha.getBackground();
                        if(null == backgroundNormal)
                        {
                            backgroundNormal = ((ImageView)viewToAlpha).getDrawable();
                            backgroundNormal.setAlpha(alphaNormal);
                            ((ImageView)viewToAlpha).setImageDrawable(backgroundNormal);
                        }else
                        {
                            backgroundNormal.setAlpha(alphaNormal);
                            viewToAlpha.setBackgroundDrawable(backgroundNormal);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    
    /**
     * 设置图片的背景资源，因为api兼容的原因使用不同的api
     * @param view 需要设置背景资源的view
     * @param drawable 背景资源id
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setBackground(View view , int drawable)
    {
        Drawable background = view.getContext().getResources().getDrawable(drawable);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackground(background);
        }else
        {
            view.setBackgroundDrawable(background);
        }
    }

}
