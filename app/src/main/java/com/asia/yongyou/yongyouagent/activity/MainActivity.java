package com.asia.yongyou.yongyouagent.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.fandang.PhoneCheckActivity;
import com.asia.yongyou.yongyouagent.activity.open.ChooseNumActivity;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;


public class MainActivity extends Activity implements View.OnClickListener {

	private TextView title;
	private ImageView rightImg,leftImg;
	private RelativeLayout archive,open;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		title = (TextView) this.findViewById(R.id.header_text);
		title.setText("用友通讯代理商");
		rightImg = (ImageView) this.findViewById(R.id.header_right_image);
		rightImg.setVisibility(View.VISIBLE);
		leftImg = (ImageView) this.findViewById(R.id.header_left_image);
		leftImg.setVisibility(View.INVISIBLE);
		rightImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, UserActivity.class);
				startActivity(intent);
			}
		});

		archive = (RelativeLayout) this.findViewById(R.id.archive);
		archive.setOnClickListener(this);

		open = (RelativeLayout) this.findViewById(R.id.open);
		open.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.archive:
				Intent intentArchive = new Intent(MainActivity.this, PhoneCheckActivity.class);
				startActivity(intentArchive);
			break;
			case R.id.open:
//				ViewUtils.showToast(MainActivity.this,"开发中...敬请期待~~");
				startActivity(new Intent(MainActivity.this, ChooseNumActivity.class));
			default:
				break;
		}
	}
}
