package com.asia.yongyou.yongyouagent.activity.fandang.idcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.utils.BitmapToBase64;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.widget.RoundRectImage;

import java.net.URLEncoder;

/**
 * 证件照
 */
public class IdCardPicsActivity extends BaseActivity implements
        View.OnClickListener {
    private RoundRectImage positive_img, handle_img;
    private RoundRectImage negative_img;
    private Bitmap negativebitmap = null;
    private Bitmap positivebitmap = null;
    private Bitmap handlebitmap = null;
    private String positiveStr, negativeStr, handleStr;
    private TextView title;
    private ImageView headerBack;
    private DisableButton submitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_pic);
        initView();
        //setTitleBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonEnable(false);
    }

    private void setButtonEnable(boolean enable) {
        if (!TextUtils.isEmpty(positiveStr) && !TextUtils.isEmpty(negativeStr) && !TextUtils.isEmpty(handleStr)) {
            submitBtn.setDisableButtonClickable(true);
            submitBtn.setClickable(true);
            return;
        }
        submitBtn.setDisableButtonClickable(enable);
        submitBtn.setClickable(enable);
    }

    private void initView() {
        submitBtn = (DisableButton) findViewById(R.id.confirm_btn);
        submitBtn.setOnClickListener(this);
        positive_img = (RoundRectImage) findViewById(R.id.positive_img);
        positive_img.setOnClickListener(this);
        negative_img = (RoundRectImage) findViewById(R.id.negative_img);
        negative_img.setVisibility(View.VISIBLE);
        negative_img.setOnClickListener(this);
        handle_img = (RoundRectImage) findViewById(R.id.handle_img);
        handle_img.setVisibility(View.VISIBLE);
        handle_img.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.header_text);
        title.setText("上传证件照");
        headerBack = (ImageView) this.findViewById(R.id.header_left_image);
        headerBack.setVisibility(View.VISIBLE);
        headerBack.setOnClickListener(this);
    }

//	/**
//	 * 设置标题栏
//	 */
//	private void setTitleBar() {
//		titleBar.setTitle("身份证照片", Color.WHITE, 20);
//		titleBar.setBackResource(R.drawable.icon_arrow_back);
//	}

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.confirm_btn:
                if (positiveStr != null && negativeStr != null && handleStr != null) {
                        Intent idcardInfoIntent = new Intent(IdCardPicsActivity.this, IdCardInfoActivity.class);
                        setResult(IdCardInfoActivity.NEW_USER_STATE, idcardInfoIntent);
                        finish();
                }
                break;
            case R.id.negative_img:
                intent = new Intent(IdCardPicsActivity.this,
                        IdCardScanActivity.class);
                intent.putExtra("side", IdcardConstant.IDCARD_NEGATIVE);
                startActivityForResult(intent, IdcardConstant.IDCARD_REQUEST_NEGITICE_CODE);
                break;
            case R.id.positive_img:
                intent = new Intent(IdCardPicsActivity.this,
                        IdCardScanActivity.class);
                intent.putExtra("side", IdcardConstant.IDCARD_POSITIVE);
                startActivityForResult(intent, IdcardConstant.IDCARD_REQUEST_POSITIVE_CODE);
                break;
            case R.id.handle_img:
                intent = new Intent(IdCardPicsActivity.this,
                        IdCardScanActivity.class);
                intent.putExtra("side", IdcardConstant.IDCARD_HANDLE);
                startActivityForResult(intent, IdcardConstant.IDCARD_REQUEST_HANDLE_CODE);
                break;
            case R.id.header_left_image:
                this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IdcardConstant.IDCARD_REQUEST_NEGITICE_CODE
                && resultCode == IdcardConstant.IDCARD_RESPONSE_NEGATIVE_CODE) {
            negativebitmap = IdCardScanActivity.nagetiveBitmap;
            if (negativebitmap == null) {
                Toast.makeText(IdCardPicsActivity.this, "请上传身份证反面照", Toast.LENGTH_SHORT).show();
                return;
            }
            negativeStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(negativebitmap));
            negative_img.setImageBitmap(negativebitmap);
            return;
        }
        if (requestCode == IdcardConstant.IDCARD_REQUEST_POSITIVE_CODE
                && resultCode == IdcardConstant.IDCARD_RESPONSE_POSITIVE_CODE) {
            positivebitmap = IdCardScanActivity.positivebitmap;
            if (positivebitmap == null) {
                Toast.makeText(IdCardPicsActivity.this, "请上传身份证正面照", Toast.LENGTH_SHORT).show();
                return;
            }
            positiveStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(positivebitmap));
            positive_img.setImageBitmap(positivebitmap);
            return;
        }
        if (requestCode == IdcardConstant.IDCARD_REQUEST_HANDLE_CODE
                && resultCode == IdcardConstant.IDCARD_RESPONSE_HANDLE_CODE) {
            handlebitmap = IdCardScanActivity.handleBitmap;
            if (handlebitmap == null) {
                Toast.makeText(IdCardPicsActivity.this, "请上传手持身份證照", Toast.LENGTH_SHORT).show();
                return;
            }
            handleStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(handlebitmap));
            handle_img.setImageBitmap(handlebitmap);
        }
    }

    public void clear() {
        if (positivebitmap != null) {
            positivebitmap.recycle();
            positivebitmap = null;
        }
        if (negativebitmap != null) {
            negativebitmap.recycle();
            negativebitmap = null;
        }
        if (handlebitmap != null) {
            handlebitmap.recycle();
            handlebitmap = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }
}