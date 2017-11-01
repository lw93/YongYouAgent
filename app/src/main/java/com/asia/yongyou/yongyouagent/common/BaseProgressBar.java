package com.asia.yongyou.yongyouagent.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;


/**
 * 通用的ProgressBar
 *
 * @author : chengcs
 * @create_time : 2015年3月25日 下午4:00:38
 * @desc : TODO
 * @update_person:
 * @update_time :
 * @update_desc :
 */
public class BaseProgressBar {

    private ProgressBar progressBar;
    private TextView progressText;
    private LinearLayout progressContent;
    private View progressView;

    public BaseProgressBar(Activity activity, String text) {
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        progressView = LayoutInflater.from(activity).inflate(R.layout.progress_view, null);
        progressContent = (LinearLayout) progressView.findViewById(R.id.progress_content);
        progressBar = (ProgressBar) progressView.findViewById(R.id.progress_bar);
        progressText = (TextView) progressView.findViewById(R.id.progress_text);
        progressText.setText(text);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(260, 106);
//        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER;

//        progressBar = new ProgressBar(activity);
//        progressBar.setVisibility(View.GONE);
//        progressBar.setLayoutParams(lp);
//        progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress_white));
//        progressContent.setLayoutParams(lp);
        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        rootContainer.addView(progressView);
    }

    public void setVisiable() {
        if (progressView.getVisibility() == View.GONE) {
            progressView.setVisibility(View.VISIBLE);
        }
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (progressText.getVisibility() == View.GONE) {
            progressText.setVisibility(View.VISIBLE);
        }
    }

    public void setInVisiable() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
        if (progressText.getVisibility() == View.VISIBLE) {
            progressText.setVisibility(View.GONE);
        }
        if (progressView.getVisibility() == View.VISIBLE) {
            progressView.setVisibility(View.GONE);
        }
    }
}
