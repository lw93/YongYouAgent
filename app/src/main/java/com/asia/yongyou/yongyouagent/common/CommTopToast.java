package com.asia.yongyou.yongyouagent.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.asia.yongyou.yongyouagent.R;

public class CommTopToast {

	/**
	 * 顶部toast
	 * @param context
	 * @param text
	 */
	public  static void showToast(Context context, ViewGroup root,CharSequence text){
		View view = LayoutInflater.from(context).inflate(R.layout.top_toast,null);
		TextView toastText = (TextView) view.findViewById(R.id.toast_text);
		toastText.setText(text);
		Toast toast=new Toast(context);
		toast.setGravity(Gravity.TOP,0,0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
}
