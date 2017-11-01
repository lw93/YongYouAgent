package com.asia.yongyou.yongyouagent.activity.fandang.idcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.utils.BitmapToBase64;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.widget.RoundRectImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 证件照
 */
public class IdCardPicsNewActivity extends BaseActivity implements
		View.OnClickListener {
	private RoundRectImage positive_img,handle_img;
	private RoundRectImage negative_img;
	private Bitmap negativebitmap = null;
	private Bitmap positivebitmap = null;
	private Bitmap handlebitmap = null;
	private String positiveStr, negativeStr,handleStr;
	private View toolBarView;
	private TextView toolBarTitleTv;
    private DisableButton confirm_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idcard_pic);
		initView();
	}

	private void initView() {
		confirm_btn = (DisableButton)findViewById(R.id.confirm_btn);
		confirm_btn.setOnClickListener(this);
		positive_img = (RoundRectImage) findViewById(R.id.positive_img);
		positive_img.setOnClickListener(this);
		negative_img = (RoundRectImage) findViewById(R.id.negative_img);
		negative_img.setVisibility(View.VISIBLE);
		negative_img.setOnClickListener(this);
		handle_img = (RoundRectImage) findViewById(R.id.handle_img);
		handle_img.setVisibility(View.VISIBLE);
		handle_img.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.confirm_btn:
			if (positivebitmap != null && negativebitmap != null && handlebitmap !=null) {
				//TODO 编码后回传图片文件 positiveStr negativeStr handleStr
//				intent.putExtra("positiveStr",positiveStr);
//				intent.putExtra("negativeStr",negativeStr);
//				intent.putExtra("handleStr",handleStr);
//				setResult(IdcardConstant.IDCARD_CODE, intent);
//				finish();
			} else {
				confirm_btn.setDisableButtonClickable(false);
				if (positivebitmap == null) {
					Toast.makeText(IdCardPicsNewActivity.this,"请上传身份证正面照",Toast.LENGTH_SHORT).show();
					return;
				}
				if (negativebitmap == null) {
					Toast.makeText(IdCardPicsNewActivity.this,"请上传身份证反面照",Toast.LENGTH_SHORT).show();
					return;
				}
				if (handlebitmap == null) {
					Toast.makeText(IdCardPicsNewActivity.this,"请上传手持身份证照",Toast.LENGTH_SHORT).show();
					return;
				}
			}
			break;
		case R.id.negative_img:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, IdcardConstant.IDCARD_REQUEST_NEGITICE_CODE);
			break;
		case R.id.positive_img:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent,  IdcardConstant.IDCARD_REQUEST_POSITIVE_CODE);
			break;
		case R.id.handle_img:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent,  IdcardConstant.IDCARD_REQUEST_HANDLE_CODE);
			break;
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		if (null ==data){
			return;
		}
		Bundle bundle = data.getExtras();
		if (null ==bundle){
			return;
		}
		Bitmap bitmap = (Bitmap) bundle.get("data");
		String imgPath = "photo_" + requestCode+".jpg";
		String path = saveImage(bitmap, imgPath)+"/"+imgPath;
		String base64 = BitmapToBase64.imgToBase64(path, bitmap);

		//如果是竖屏拍照，回显时为竖直显示，这时将图片旋转90°
		if(bitmap.getWidth()<bitmap.getHeight()){
			bitmap = remoteMatrix(bitmap);
		}


		switch (requestCode){
			case IdcardConstant.IDCARD_REQUEST_POSITIVE_CODE:
				if(resultCode==Activity.RESULT_OK){
					positivebitmap = bitmap;
					positive_img.setImageBitmap(bitmap);
					try {
						positiveStr = URLEncoder.encode(base64, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				break;
			case IdcardConstant.IDCARD_REQUEST_NEGITICE_CODE:
				if(resultCode== Activity.RESULT_OK){
					negativebitmap = bitmap;
					negative_img.setImageBitmap(bitmap);
					try {
						negativeStr = URLEncoder.encode(base64, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				break;
			case IdcardConstant.IDCARD_REQUEST_HANDLE_CODE:
				if(resultCode==Activity.RESULT_OK){
					handlebitmap = bitmap;
					handle_img.setImageBitmap(bitmap);
					try {
						handleStr = URLEncoder.encode(base64, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				break;
		}

		clickable();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void clickable(){
		if (positivebitmap != null && negativebitmap != null && handlebitmap !=null) {
			confirm_btn.setDisableButtonClickable(true);
		}else{
			confirm_btn.setDisableButtonClickable(false);
		}
	}

	//图片旋转
	private Bitmap remoteMatrix(Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(90);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		if (bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return newBitmap;
	}

	public static String saveImage(Bitmap bmp,String picName) {
		FileOutputStream b = null;
		File file = new File("/sdcard/yongyou/");
		if(!file.exists()){
			file.mkdirs();// 创建文件夹
		}
		String fileName = "/sdcard/yongyou/"+picName;
		try {
			b = new FileOutputStream(fileName);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file.getAbsolutePath();
	}

	public void clear(){
		if(positivebitmap!=null){
			positivebitmap.recycle();
			positivebitmap=null;
		}
		if(negativebitmap!=null){
			negativebitmap.recycle();
			negativebitmap=null;
		}
		if(handlebitmap!=null){
			handlebitmap.recycle();
			handlebitmap=null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		clear();
	}
}