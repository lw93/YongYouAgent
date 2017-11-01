package com.asia.yongyou.yongyouagent.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.asia.yongyou.yongyouagent.R;


/**
 * 可动态颜色按钮
 */
public class DisableButton extends Button {
    private int pressedResId = R.mipmap.button_down;//按下 背景id
    private int resId = R.mipmap.button_nor;//正常背景
    private int disableResId = R.mipmap.button_disable;//不可点击背景
    private int left, top, right, bottom;


    public DisableButton(Context context) {
        this(context,null);
    }


    public DisableButton(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DisableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setGravity(Gravity.CENTER);
        left = getPaddingLeft();
        top = getPaddingTop();
        right = getPaddingRight();
        bottom = getPaddingBottom();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DisableButton);
        pressedResId = a.getResourceId(R.styleable.DisableButton_pressedResId, pressedResId);
        resId = a.getResourceId(R.styleable.DisableButton_resId, pressedResId);
        disableResId = a.getResourceId(R.styleable.DisableButton_disableResId, pressedResId);
        a.recycle();
    }


    /**
     * 根据不同的点击属性设置不同的背景图片
     * @param click
     */
    public void setDisableButtonClickable(boolean click){
        if(click){
            setBackgroundDrawable(setPressViewResource());
        }  else{
            setBackgroundResource(disableResId);
        }
        setPadding(left,top,right,bottom);
        super.setClickable(click);
    }


    public StateListDrawable setPressViewResource() {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = getContext().getResources().getDrawable(resId);
        Drawable pressed = getContext().getResources().getDrawable(pressedResId);
        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
        bg.addState(View.ENABLED_STATE_SET, normal);
        bg.addState(View.EMPTY_STATE_SET, normal);
        return bg;

    }

    public int getPressedResId() {
        return pressedResId;
    }

    public void setPressedResId(int pressedResId) {
        this.pressedResId = pressedResId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getDisableResId() {
        return disableResId;
    }

    public void setDisableResId(int disableResId) {
        this.disableResId = disableResId;
    }
}
