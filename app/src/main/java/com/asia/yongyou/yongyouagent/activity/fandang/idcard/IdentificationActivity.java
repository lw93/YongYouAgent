package com.asia.yongyou.yongyouagent.activity.fandang.idcard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.common.BaseProgressBar;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.IDCardInfo;
import com.asia.yongyou.yongyouagent.interfaces.ConnectInterface;
import com.asia.yongyou.yongyouagent.utils.CheckPhoneStatus;
import com.asia.yongyou.yongyouagent.utils.IDCardUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.kaer.sdk.IDCardItem;
import com.kaer.sdk.OnClientCallback;
import com.kaer.sdk.bt.BtReadClient;
import com.kaer.sdk.bt.OnBluetoothListener;
import com.kaer.sdk.utils.CardCode;
import com.kaer.sdk.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;

/**
 * 身份识别界面
 *
 * @author Created by liuwei
 * @time on 2017/9/2
 */
public class IdentificationActivity extends BaseActivity implements OnClientCallback, OnBluetoothListener, View.OnClickListener ,ConnectInterface{
    private View toolBarView;
    private TextView toolBarTitleTv;
    private ImageView toolBarArrowImg;
    private TextView firstStepDivideTv;
    private TextView firstStepTv;
    private View firstDividedView;
    private TextView secondStepDivideTv;
    private TextView secondStepTv;
    private View secondDividedView;
    private TextView thirdStepDivideTv;
    private TextView thirdStepTv;
    private View thirdDividedView;
    private DisableButton startIdentificationBtn;
    private PowerManager.WakeLock wakeLock;
    private BtReadClient mBtReadClient;
    private long startBtReadTime;
    private ReadAsync readAsync;
    private volatile boolean canRead = false;
    private BaseProgressBar progressBar;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {
                ViewUtils.showToast(IdentificationActivity.this, msg.obj.toString());
            } else if (msg.what == 201) {
                ViewUtils.showToast(IdentificationActivity.this, msg.obj.toString());
            } else if (msg.what == 202) {
                ViewUtils.showToast(IdentificationActivity.this, msg.obj.toString());
            } else if (msg.what == 203) {
                if (null != msg.obj) {
                    ViewUtils.showToast(IdentificationActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 204) {
                setThirdStepBackground(false);
            } else if (msg.what == 205) {
                if (msg.arg1 == 1) {
                    setFirtStepBackground(true);
                    return;
                }
                setFirtStepBackground(false);
                return;
            } else if (msg.what == 700) {
                if (msg.arg1 == 1) {
                    LogUtils.e("蓝牙连接成功!");
                    setSecondStepBackground(true);
                } else {
                    LogUtils.e("蓝牙断开!");
                    setSecondStepBackground(false);
                }
                return;
            } else if (msg.what == 900) {
                if (msg.arg1 == 0) {
                    canRead = true;
                    LogUtils.e("服务器连接成功！");
                } else if (msg.arg1 == 7) {
                    ViewUtils.showToast(IdentificationActivity.this, "服务器连接失败,请检查网络!");
                } else {
                    ViewUtils.showToast(IdentificationActivity.this, getEInfoByCode(msg.arg1));
                }
            }
            hideClickble();
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("onReceive---------");
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            LogUtils.e("onReceive---------STATE_TURNING_ON");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            LogUtils.e("onReceive----Bluetooth-----STATE_ON");
                            mHandler.obtainMessage(205, 1, 1).sendToTarget();
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            bluetoothHasBond(bluetoothAdapter);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            LogUtils.e("onReceive---------STATE_TURNING_OFF");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            LogUtils.e("onReceive----Bluetooth-----STATE_OFF");
                            mHandler.obtainMessage(205, 0, 0).sendToTarget();
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * 隐藏progressBar
     * button可点击
     */
    private void hideClickble() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setInVisiable();
                startIdentificationBtn.setDisableButtonClickable(true);
                startIdentificationBtn.setClickable(true);
            }
        }, 1000);
    }


    /**
     * 切换第一步背景
     *
     * @param b
     */
    private void setFirtStepBackground(boolean b) {
        if (b) {
            firstDividedView.setBackgroundResource(R.color.bg_header_red);
            firstStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_red);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_finish);
            firstStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        } else {
            firstDividedView.setBackgroundResource(R.color.text_grey);
            firstStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_grey);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_unfinish);
            firstStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        }
    }

    /**
     * 切换第二步背景
     *
     * @param b
     */
    private void setSecondStepBackground(boolean b) {
        if (b) {
            secondDividedView.setBackgroundResource(R.color.bg_header_red);
            secondStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_red);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_finish);
            secondStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        } else {
            secondDividedView.setBackgroundResource(R.color.text_grey);
            secondStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_grey);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_unfinish);
            secondStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        }
    }

    /**
     * 切换第三步背景
     *
     * @param b
     */
    private void setThirdStepBackground(boolean b) {
        if (b) {
            thirdDividedView.setBackgroundResource(R.color.bg_header_red);
            thirdStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_red);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_finish);
            thirdStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        } else {
            thirdDividedView.setBackgroundResource(R.color.text_grey);
            thirdStepDivideTv.setBackgroundResource(R.mipmap.idcard_step_grey);
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.idcard_step_bg);
            Drawable drawableRight = getResources().getDrawable(R.mipmap.idcard_unfinish);
            thirdStepTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        toolBarTitleTv.setText(getString(R.string.identification_toolbar_title) + "");
        toolBarArrowImg.setOnClickListener(this);
        startIdentificationBtn.setOnClickListener(this);
        startIdentificationBtn.setDisableButtonClickable(true);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null != bluetoothAdapter && bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            setFirtStepBackground(true);
            bluetoothHasBond(bluetoothAdapter);
        }
        initBtReadClient();
        if (null == progressBar) {
            progressBar = new BaseProgressBar(IdentificationActivity.this, "正在识别中......");
        }

    }
    //判断蓝牙是否绑定
    private void bluetoothHasBond(BluetoothAdapter bluetoothAdapter){
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        int size = devices.size();
        if (size > 0) {
            Iterator<BluetoothDevice> iterator = devices.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                if (count > size) count = 0;
                BluetoothDevice bluetoothDevice = iterator.next();
                if (count == size - 1 && !bluetoothDevice.getName().toUpperCase().startsWith("KT8003")) {
                    mHandler.obtainMessage(700, 0, 0).sendToTarget();
                    return;
                }
                if (null != bluetoothDevice && bluetoothDevice.getName().toUpperCase().startsWith("KT8003")) {
                    mHandler.obtainMessage(700, 1, 1).sendToTarget();
                    break;
                }
            }
            count++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();
    }

    /**
     * 清除数据
     */
    private void clearAll() {
        releaseWakeLock();
        if (null != mBtReadClient) {
            try {
                mBtReadClient.disconnectBt();
                mBtReadClient.disconnect();
                mBtReadClient = null;
                canRead = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != readAsync) {
            readAsync.cancel(true);
            readAsync = null;
        }
        if (null != progressBar) {
            progressBar.setInVisiable();
            progressBar = null;
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (null != mReceiver) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    //初始化
    public void initView() {
        setContentView(R.layout.activity_identification);
        toolBarView = this.findViewById(R.id.toolbar);
        toolBarTitleTv = (TextView) toolBarView.findViewById(R.id.header_text);
        toolBarArrowImg = (ImageView) toolBarView.findViewById(R.id.header_left_image);
        firstStepDivideTv = (TextView) this.findViewById(R.id.idcard_divide_1_text);
        firstStepTv = (TextView) this.findViewById(R.id.first_step);
        firstDividedView = this.findViewById(R.id.idcard_divide_1);
        secondStepDivideTv = (TextView) this.findViewById(R.id.idcard_divide_2_text);
        secondStepTv = (TextView) this.findViewById(R.id.second_step);
        secondStepDivideTv = (TextView) this.findViewById(R.id.idcard_divide_2_text);
        secondStepTv = (TextView) this.findViewById(R.id.second_step);
        secondDividedView = this.findViewById(R.id.idcard_divide_2);
        thirdStepDivideTv = (TextView) this.findViewById(R.id.idcard_divide_3_text);
        thirdStepTv = (TextView) this.findViewById(R.id.third_step);
        thirdDividedView = this.findViewById(R.id.idcard_divide_3);
        startIdentificationBtn = (DisableButton) this.findViewById(R.id.start_identification_btn);

    }



    /**
     * 初始化
     */
    private void initBtReadClient() {

        if (null == mBtReadClient) {
            mBtReadClient = BtReadClient.getInstance();
        }
        mBtReadClient.setClientCallback(this);
        if (null == wakeLock) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "kaer");
        }
        if (!canRead) connectBtServer();
    }

    /**
     * 连接服务器
     */
    private synchronized void connectBtServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ip = IDCardUtils.getIp(IdentificationActivity.this);
                    int port = IDCardUtils.getPort(IdentificationActivity.this);
                    String account = IDCardUtils.getAccount(IdentificationActivity.this);
                    String source = IDCardUtils.getPassword(IdentificationActivity.this);
                    String password = getMD5(source.getBytes());
                    mBtReadClient.setReadParams(ip, port, account, password, false);
                    int result = mBtReadClient.init(IdentificationActivity.this, ip, port, account, password,
                            IDCardUtils.getIsWss(IdentificationActivity.this));
                    if (null != mHandler)
                        mHandler.obtainMessage(900, result, result).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start_identification_btn) {
            asynThread();
        } else if (id == R.id.header_left_image) {
            this.finish();
        }
    }

    /**
     * 异步识别
     */
    private void asynThread() {
        boolean netState = CheckPhoneStatus.checkNetworkWithDialog(this);
        if (!netState) {
            if (canRead) canRead = false;
            return;
        }
        if (netState) {
            if (null != progressBar) progressBar.setVisiable();
            startIdentificationBtn.setDisableButtonClickable(false);
            startIdentificationBtn.setClickable(false);
            if (null != mBtReadClient && mBtReadClient.getConnect() != 2) {
                if (canRead) canRead = false;
                connectBtServer();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    recognizeID();
                }
            }).start();
        }
    }


    /**
     * 开始识别
     */
    private synchronized void recognizeID() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == bluetoothAdapter) {
            mHandler.obtainMessage(200, "此设备不支持蓝牙传输功能！").sendToTarget();
            mHandler.obtainMessage(203).sendToTarget();
            mBtReadClient.disconnect();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            mHandler.obtainMessage(201, "蓝牙未打开，请将蓝牙打开！").sendToTarget();
            mHandler.obtainMessage(203).sendToTarget();
            return;
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices.size() < 1) {
            mHandler.obtainMessage(202, "没有绑定任何设备的蓝牙，请连接绑定设备！").sendToTarget();
            mHandler.obtainMessage(203).sendToTarget();
            return;
        }
        int size = devices.size();
        if (size > 0) {
            Iterator<BluetoothDevice> iterator = devices.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                if (count > size) count = 0;
                BluetoothDevice bluetoothDevice = iterator.next();
                if (count == size - 1 && !bluetoothDevice.getName().toUpperCase().startsWith("KT8003")) {
                    mHandler.obtainMessage(202, "读卡器没有绑定蓝牙，请打开蓝牙连接设备！").sendToTarget();
                    return;
                }
                if (null != bluetoothDevice && bluetoothDevice.getName().toUpperCase().startsWith("KT8")) {
                    readCard(bluetoothDevice);
                    break;
                }
                count++;
            }
        }
    }

    /**
     * 开始读卡
     *
     * @param bluetoothDevice
     */
    private synchronized void readCard(BluetoothDevice bluetoothDevice) {
        if (mBtReadClient.getBtState() == 0) {
            LogUtils.e("开始连接设备...");
            mBtReadClient.setBluetoothListener(this);
            LogUtils.e(bluetoothDevice.getName() + "/" + bluetoothDevice.getAddress());
            boolean flag = mBtReadClient.connectBt(bluetoothDevice);
            if (!flag) {
                mHandler.obtainMessage(203, "蓝牙连接失败，请检查读卡器设备是否开启！").sendToTarget();
                return;
            }
        }
        if (mBtReadClient.getBtState() == 2) {
            readAsync = new ReadAsync(IdentificationActivity.this);
            readAsync.execute();
        }

    }

    /**
     * 释放对象
     */
    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    /**
     * 获取返回的错误码
     *
     * @param code
     * @return
     */

    protected String getEInfoByCode(int code) {
        return CardCode.errorCodeDescription(code);
    }

    /**
     * md5编码
     *
     * @param source
     * @return
     */
    public String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 缩小图片
     *
     * @param bitmap
     * @return
     */
    protected Bitmap scale(Bitmap bitmap) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        int width = displaysMetrics.widthPixels;
        Matrix matrix = new Matrix();
        float scale = width / (8.0f * bitmap.getWidth());
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    //kaer设备接口回调
    @Override
    public void preExcute(long l) {
        startBtReadTime = l;

    }

    @Override
    public void updateProgress(int i) {
        LogUtils.e("arg0.progress=" + i);
    }

    @Override
    public void onConnectChange(int i) {
        //设备实时连接状态监听
        boolean connected = i == 1;
        LogUtils.e(connected ? "onConnectChange: 读卡设备已连接服务器" : "onConnectChange：读卡设备已断开服务器");
    }

    //蓝牙接口回调
    @Override
    public void connectResult(boolean b) {
        LogUtils.e("回调result: " + b);
//        int what = b ? 1 : 0;
//        mHandler.obtainMessage(700, what, what).sendToTarget();
    }

    @Override
    public void connectionLost() {

    }



    /**
     * 获取数据
     *
     * @param arg0
     */
    private void updateResult(IDCardItem arg0) {
        if (arg0.retCode != 1) {
            mBtReadClient.disconnectBt();
            if (arg0.retCode == 3) {
                mHandler.obtainMessage(900, 7, 7).sendToTarget();
                return;
            }
            String readError = getEInfoByCode(arg0.retCode);
            mHandler.obtainMessage(203, readError).sendToTarget();
            return;
        }

        if (arg0.retCode == 1) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setThirdStepBackground(true);
                }
            });
            final IDCardInfo idCardInfo = parclebleData(arg0);
            LogUtils.e("读取共耗时:" + String.valueOf(System.currentTimeMillis() - startBtReadTime) + "毫秒");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBtReadClient.disconnectBt();
                    mHandler.obtainMessage(204).sendToTarget();
//                    redirectToActivity(idCardInfo);
                    redirect(idCardInfo);
                }

            }, 2000);

        }

    }

    /**
     * 跳转
     *
     * @param idCardInfo
     */
    private void redirectToActivity(IDCardInfo idCardInfo) {
        final Intent intentIdCardInfo = new Intent(this, IdCardInfoActivity.class);
        intentIdCardInfo.putExtra(Constant.IDCARDINFO, idCardInfo);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(intentIdCardInfo);
            }
        }, 1000);
    }

    /**
     * 信息转化
     *
     * @param item
     * @return
     */
    private IDCardInfo parclebleData(IDCardItem item) {
        IDCardInfo idCardInfo = new IDCardInfo();
        //姓名
        idCardInfo.setPartyName(item.partyName);
        //身份证号
        idCardInfo.setCertNumber(item.certNumber);
        //住址
        idCardInfo.setCertAddress(item.certAddress);
        //性别
        idCardInfo.setGender(item.gender);
        //民族
        idCardInfo.setNation(item.nation);
        //出生日期
        idCardInfo.setBornDay(item.bornDay);
        //有效期限
        String effDate = item.effDate;
        String expDate = item.expDate;
        StringBuffer validDate = new StringBuffer();
        validDate.append(effDate.substring(0, 4));
        validDate.append(".");
        validDate.append(effDate.substring(4, 6));
        validDate.append(".");
        validDate.append(effDate.substring(6, 8));
        validDate.append("-");
        validDate.append(expDate.substring(0, 4));
        validDate.append(".");
        validDate.append(expDate.substring(4, 6));
        validDate.append(".");
        validDate.append(expDate.substring(6, 8));
        idCardInfo.setValidDate(validDate.toString());
        //开始日期
        idCardInfo.setEffDate(effDate);
        //终止日期
        idCardInfo.setExpDate(expDate);
        //签发机关
        idCardInfo.setCertOrg(item.certOrg);
        //头像
        Bitmap pic = item.picBitmap;
        idCardInfo.setPicBitmap(scale(pic));
        //英文名
        idCardInfo.setEnglishName(item.englishName);

        return idCardInfo;
    }

    @Override
    public void redirect(IDCardInfo idCardInfo) {
        redirectToActivity(idCardInfo);

    }


    /**
     * 异步读卡类
     */
    class ReadAsync extends AsyncTask<Intent, Integer, IDCardItem> {
        private WeakReference<Activity> mActivity;

        public ReadAsync(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startBtReadTime = System.currentTimeMillis();
        }

        @Override
        protected IDCardItem doInBackground(Intent... params) {
            IDCardItem item = mBtReadClient.readCert(0);
            LogUtils.e("IDCardItem :" + item);
            return item;
        }

        @Override
        protected void onPostExecute(IDCardItem result) {
            super.onPostExecute(result);
            if (null != mActivity.get()) {
                updateResult(result);
            }
        }
    }

}
