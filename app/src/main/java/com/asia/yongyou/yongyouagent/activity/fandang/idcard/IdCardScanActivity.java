package com.asia.yongyou.yongyouagent.activity.fandang.idcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.utils.BitmapConfig;
import com.asia.yongyou.yongyouagent.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 照片拍摄
 */
public class IdCardScanActivity extends FragmentActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private SurfaceHolder surfaceHolder = null;
    private boolean isPreview = false;
    private boolean hasSurface = false;
    private SurfaceView surfaceView = null; //预览SurfaceView
    private Camera.AutoFocusCallback autoFocusCallback = null;
    private Camera.AutoFocusCallback autoFocusCallback1 = null;
    private Camera camera;
    private int issuccessfocus = 0;
    private final static int only_auto_focus = 101;
    private final static int auto_focus = 102;
    private final static int take_pic_ok = 103;
    private final static int decode_succeeded = 104;


    private int side;
    private TextView warm_text;
    private RelativeLayout mContainer = null;
    private RelativeLayout mCropLayout = null;
    private ImageView btn;

    private LinearLayout linearLayout;
    private int width;
    private int height;
    private DisplayMetrics displaysMetrics;

    public static Bitmap positivebitmap;//正面照
    public static Bitmap handleBitmap;//手持照片
    public static Bitmap nagetiveBitmap;//反面照

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idcard_scan);
        side = getIntent().getIntExtra("side", 1);
        initView();
    }


    private void initView() {
        mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
        mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
        linearLayout = new LinearLayout(this);
        warm_text = (TextView) findViewById(R.id.warm_text);
        surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        surfaceView.setZOrderOnTop(false);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btn =  (ImageView) findViewById(R.id.btn);
        btn.setOnClickListener(this);

        displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        width = displaysMetrics.widthPixels;
        height = displaysMetrics.heightPixels;
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        linearLayout.setBackgroundResource(R.mipmap.camera_take_bg);
        mCropLayout.addView(linearLayout);

        switch (side) {
            case IdcardConstant.IDCARD_POSITIVE:
                warm_text.setText(getString(R.string.idcard_scan_warm, getString(R.string.idcard_positive_warm)));
                break;
            case IdcardConstant.IDCARD_NEGATIVE:
                warm_text.setText(getString(R.string.idcard_scan_warm, getString(R.string.idcard_nagetive_warm)));
                break;
            case IdcardConstant.IDCARD_HANDLE:
                warm_text.setText(getString(R.string.idcard_handle_warm_text));
                break;
        }

        // check Android 6 permission打开权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("TEST","Granted");
            //init(barcodeScannerView, getIntent(), null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }

        autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                if (success)//success表示对焦成功
                {
                    handler.sendEmptyMessageDelayed(take_pic_ok, 300);
                }
                camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦
            }
        };
        autoFocusCallback1 = new Camera.AutoFocusCallback() {

            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if (success)//success表示对焦成功
                {
                    issuccessfocus++;
                    if (issuccessfocus <= 1)
                        handler.sendEmptyMessage(R.id.only_auto_focus);
                } else {
                    handler.sendEmptyMessage(R.id.only_auto_focus);
                }
                camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦

            }
        };

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                if (isPreview && camera != null) {
                    isPreview = false;
                    handler.sendEmptyMessage(auto_focus);
                }
                break;
//            case R.id.title_left_linear:
//                finish();
//                break;
        }
    }


    private void initCamera() {
        if (isPreview) {
            camera.stopPreview();
        }
        if (null != camera) {
            Camera.Parameters param = camera.getParameters();
            param.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            param.setJpegQuality(100);
            param.setPreviewSize(width, height);
            Camera.Size size = getSizeForPreviewSize(param);
            if (size != null) {
                param.setPreviewSize(size.width, size.height);
            } else {
                param.setPreviewSize(width, height);
            }
            param.set("rotation", 0);
            camera.setDisplayOrientation(90);
            //camera.setDisplayOrientation(0);
            try {
                camera.setParameters(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            camera.startPreview();
            handler.sendEmptyMessageDelayed(only_auto_focus, 100);
            isPreview = true;
            camera.cancelAutoFocus();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case only_auto_focus:
                    if (camera != null)
                        camera.autoFocus(autoFocusCallback1);
                    break;
                case auto_focus:
                    if (camera != null)
                        camera.autoFocus(autoFocusCallback);
                    break;
                case take_pic_ok:
                    if (camera != null)
                        camera.takePicture(null, null, picJpegCallback);
                    break;
                case decode_succeeded:
                    byte[] data = (byte[]) msg.obj;
                    if (data == null) {
                        Toast.makeText(getApplicationContext(), "拍照数据获取失败，请手动对焦", Toast.LENGTH_SHORT).show();
                        camera.startPreview();
                        camera.autoFocus(autoFocusCallback1);
                        return;
                    }
                    Bitmap bitmap = BitmapUtils.decodeCameraImage(data, 90);
                    Bitmap mBitmap = new BitmapConfig().ratio(bitmap, 800, 400);
                    Intent intent = new Intent();
                    if(side == IdcardConstant.IDCARD_NEGATIVE){
                        nagetiveBitmap = mBitmap;
                        setResult(IdcardConstant.IDCARD_RESPONSE_NEGATIVE_CODE, intent);
                        finish();
                    }else if(side == IdcardConstant.IDCARD_POSITIVE){
                        positivebitmap = mBitmap;
                        setResult(IdcardConstant.IDCARD_RESPONSE_POSITIVE_CODE, intent);
                        finish();
                    }else if(side == IdcardConstant.IDCARD_HANDLE){
                        handleBitmap = mBitmap;
                        setResult(IdcardConstant.IDCARD_RESPONSE_HANDLE_CODE, intent);
                        finish();
                    }else{

                    }

                    if (null != camera) {
                        camera.setPreviewCallback(null); /*在启动PreviewCallback时这个必须在前不然�?出出错�?
                        这里实际上注释掉也没关系*/
                        camera.stopPreview();
                        isPreview = false;
                        camera.release();
                        camera = null;
                    }
                    break;

            }
        }
    };


    Camera.PictureCallback picJpegCallback = new Camera.PictureCallback()
            //对jpeg图像数据的回调函数使用
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (null != data) {
                //mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位�?
                camera.stopPreview();
                Message msg_data = handler.obtainMessage();
                msg_data.what = decode_succeeded;
                msg_data.obj = data;
                handler.sendMessage(msg_data);
                isPreview = false;
            }

        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (camera != null) {
            camera.cancelAutoFocus();
            camera.autoFocus(autoFocusCallback1);
        }
        return true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!hasSurface) {
            hasSurface = true;

            try {
                camera = Camera.open();
                camera.setPreviewDisplay(surfaceHolder);
            } catch (Exception e) {
                Toast.makeText(IdCardScanActivity.this,"请检查摄像头权限",Toast.LENGTH_SHORT).show();
                if (null != camera) {
                    try {
                        camera.release();
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }

                    camera = null;
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        hasSurface = false;
        if (null != camera) {
            camera.setPreviewCallback(null); /*在启动PreviewCallback时这个必须在前不然�?出出错�?
            这里实际上注释掉也没关系*/
            camera.stopPreview();
            isPreview = false;
            camera.release();
            camera = null;
        }
    }

    private Camera.Size getSizeForPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> ViewSize = parameters.getSupportedPreviewSizes();
        List<Camera.Size> picSize = parameters.getSupportedPictureSizes();
        List<Camera.Size> MaxSizes = new ArrayList<Camera.Size>();
        List<Camera.Size> MinSize = new ArrayList<Camera.Size>();
        Camera.Size tempSize;
        for (int j = ViewSize.size(); j >= 0; j--) {
            for (int i = 0; i < j - 1; i++) {
                if (ViewSize.get(i).width > ViewSize.get(i + 1).width) {
                    tempSize = ViewSize.get(i + 1);
                    ViewSize.set(i + 1, ViewSize.get(i));
                    ViewSize.set(i, tempSize);
                }
            }
        }

        for (int j = picSize.size(); j >= 0; j--) {
            for (int i = 0; i < j - 1; i++) {
                if (picSize.get(i).width > picSize.get(i + 1).width) {
                    tempSize = picSize.get(i + 1);
                    picSize.set(i + 1, picSize.get(i));
                    picSize.set(i, tempSize);
                }
            }
        }

        for (int i = 0; i < picSize.size(); i++) {
            if (picSize.get(i).width >= 1500) {
                MaxSizes.add(picSize.get(i));
            } else {
                MinSize.add(picSize.get(i));
            }
        }


        for (int i = ViewSize.size() - 1; i >= 0; i--) {
            if (ViewSize.get(i).width <= 1500) {
                for (int j = 0; j < MaxSizes.size(); j++) {
                    if ((double) ViewSize.get(i).width / ViewSize.get(i).height
                            == (double) MaxSizes.get(j).width / MaxSizes.get(j).height) {
                        parameters.setPictureSize(MaxSizes.get(j).width, MaxSizes.get(j).height);
                        return ViewSize.get(i);
                    }
                }
            }
        }
        for (int i = ViewSize.size() - 1; i >= 0; i--) {
            if (ViewSize.get(i).width <= 1500) {
                for (int j = MinSize.size() - 1; j >= 0; j--) {
                    if (ViewSize.get(i).width / ViewSize.get(i).height
                            == MinSize.get(j).width / MinSize.get(j).height) {
                        parameters.setPictureSize(MinSize.get(j).width, MinSize.get(j).height);

                        return ViewSize.get(i);
                    }
                }
            }
        }
        return null;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            isPreview = false;
            camera.release();
            camera = null;
        }
        if (handler != null) {
            handler.removeMessages(only_auto_focus);
            handler.removeMessages(auto_focus);
            handler.removeMessages(take_pic_ok);
            handler.removeMessages(decode_succeeded);
        }
    }
}