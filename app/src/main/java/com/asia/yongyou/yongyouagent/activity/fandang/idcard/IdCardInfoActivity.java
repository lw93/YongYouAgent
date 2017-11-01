package com.asia.yongyou.yongyouagent.activity.fandang.idcard;

import android.app.Dialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.activity.MainActivity;
import com.asia.yongyou.yongyouagent.activity.fandang.PhoneCheckActivity;
import com.asia.yongyou.yongyouagent.activity.login.LoginActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.CommDialog;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.CheckOldCustVo;
import com.asia.yongyou.yongyouagent.entity.IDCardInfo;
import com.asia.yongyou.yongyouagent.interfaces.CustomerInterface;
import com.asia.yongyou.yongyouagent.utils.BitmapToBase64;
import com.asia.yongyou.yongyouagent.utils.Java3DESUtil;
import com.asia.yongyou.yongyouagent.utils.NumUtil;
import com.asia.yongyou.yongyouagent.utils.ParseJsonUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

/**
 * 身份信息界面
 *
 * @author Created by liuwei
 * @time on 2017/9/2
 */
public class IdCardInfoActivity extends BaseActivity implements View.OnClickListener ,CustomerInterface{
    private View toolBarView;
    private TextView toolBarTitleTv;
    private ImageView toolBarArrowImg;
    private TextView userNameTv;
    private TextView idTypeTv;
    private TextView idNumberTv;
    private TextView idAddressTv;
    private TextView userSexTv;
    private TextView userNationTv;
    private TextView userBrithdayTv;
    private TextView idDateTv;
    public EditText userPhoneEv;
    private RelativeLayout upload_relativelayout;
    private TextView upload_result;
    private TextView uploadFinishTv;
    private DisableButton submitBtn;
    public String positiveStr;//正面照编码
    public String negativeStr;//反面照编码
    public String handleStr;//手持照编码
    public IDCardInfo idCardInfo;
    public static final int NEW_USER_STATE = 200;
    public volatile boolean isOlder = false;
    public String custFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_idcard_information);
        toolBarView = this.findViewById(R.id.toolbar);
        toolBarTitleTv = (TextView) toolBarView.findViewById(R.id.header_text);
        toolBarArrowImg = (ImageView) toolBarView.findViewById(R.id.header_left_image);
        userNameTv = (TextView) this.findViewById(R.id.user_name_text);
        idTypeTv = (TextView) this.findViewById(R.id.id_type_text);
        idNumberTv = (TextView) this.findViewById(R.id.id_number_text);
        idAddressTv = (TextView) this.findViewById(R.id.id_address_text);
        userSexTv = (TextView) this.findViewById(R.id.user_sex_text);
        userNationTv = (TextView) this.findViewById(R.id.user_nation_text);
        userBrithdayTv = (TextView) this.findViewById(R.id.user_birthday_text);
        idDateTv = (TextView) this.findViewById(R.id.id_date_text);
        userPhoneEv = (EditText) this.findViewById(R.id.user_phone_text);
        upload_relativelayout = (RelativeLayout) this.findViewById(R.id.upload_id_relativelayout);
        upload_result = (TextView) this.findViewById(R.id.upload_result);
        uploadFinishTv = (TextView) this.findViewById(R.id.uploadfinished_text);
        submitBtn = (DisableButton) this.findViewById(R.id.identification_submit_btn);
    }

    private void enabledButton(boolean enable) {
        submitBtn.setDisableButtonClickable(enable);
        submitBtn.setClickable(enable);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = this.getIntent();
        this.idCardInfo = (IDCardInfo) intent.getParcelableExtra(Constant.IDCARDINFO);
        if (null == idCardInfo) {
            this.finish();
            return;
        }
        toolBarTitleTv.setText(getString(R.string.idcard_info) + "");
        toolBarArrowImg.setOnClickListener(this);
        userPhoneEv.setOnClickListener(this);
        upload_relativelayout.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        userNameTv.setText(idCardInfo.getPartyName() + "");
        idTypeTv.setText("身份证");
        idNumberTv.setText(idCardInfo.getCertNumber() + "");
        idAddressTv.setText(idCardInfo.getCertAddress() + "");
        if (idCardInfo.getCertAddress().length() < 20) {
            idAddressTv.setGravity(Gravity.CENTER | Gravity.RIGHT);

        } else {
            idAddressTv.setGravity(Gravity.CENTER | Gravity.LEFT);
        }
        userSexTv.setText(idCardInfo.getGender() + "");
        userNationTv.setText(idCardInfo.getNation() + "");
        userBrithdayTv.setText(idCardInfo.getBornDay() + "");
        idDateTv.setText(idCardInfo.getValidDate() + "");
        if (!isOlder) isOlder();
        setUserOlder(isOlder);
        initButton(isOlder);
        userPhoneEv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                initButton(isOlder);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 变换button状态
     */
    private void initButton(boolean isOlderUser) {
        if (isOlderUser && userPhoneEv.getText().toString().length() == 11) {
            enabledButton(true);
        } else if (!TextUtils.isEmpty(positiveStr) && !TextUtils.isEmpty(positiveStr)
                && !TextUtils.isEmpty(positiveStr) && userPhoneEv.getText().toString().length() == 11) {
            enabledButton(true);
        } else {
            enabledButton(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAll();

    }

    private void clearAll() {

        if (null != IdCardScanActivity.positivebitmap) {
            IdCardScanActivity.positivebitmap.recycle();
            IdCardScanActivity.positivebitmap = null;
        }
        if (null != IdCardScanActivity.nagetiveBitmap) {
            IdCardScanActivity.nagetiveBitmap.recycle();
            IdCardScanActivity.nagetiveBitmap = null;
        }
        if (null != IdCardScanActivity.handleBitmap) {
            IdCardScanActivity.handleBitmap.recycle();
            IdCardScanActivity.handleBitmap = null;
        }

        if (null != positiveStr) {
            positiveStr = null;
        }
        if (null != negativeStr) {
            negativeStr = null;
        }
        if (null != handleStr) {
            handleStr = null;
        }
        if (null != idCardInfo) {
            idCardInfo = null;
        }
        if (isOlder) isOlder = false;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.header_left_image) {
            this.finish();
        } else if (id == R.id.upload_id_relativelayout) {
            redirectToActivity(IdCardPicsActivity.class);
        } else if (id == R.id.identification_submit_btn) {
//            submitResult();
            submit();
        }
    }

    /**
     * 跳转
     */
    private void redirectToActivity(Class<?> cls) {
        Intent intent;
        if (cls == IdCardPicsActivity.class) {
            intent = new Intent(this, IdCardPicsActivity.class);
            startActivityForResult(intent, NEW_USER_STATE);
        } else if (cls == LoginActivity.class) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (cls == PhoneCheckActivity.class) {
            intent = new Intent(this, PhoneCheckActivity.class);
            startActivity(intent);
        }
//        else if (cls == LoginActivity.class) {
//            intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_USER_STATE && resultCode == NEW_USER_STATE) {
            if (null != data) {
                if (null == IdCardScanActivity.positivebitmap) {
                    ViewUtils.showToast(this, "正面照片为空，请重新拍照！");
                    return;
                }
                if (null == IdCardScanActivity.nagetiveBitmap) {
                    ViewUtils.showToast(this, "反面照片为空，请重新拍照！");
                    return;
                }
                if (null == IdCardScanActivity.handleBitmap) {
                    ViewUtils.showToast(this, "手持照片为空，请重新拍照！");
                    return;
                }
                if (upload_relativelayout.getVisibility() == View.VISIBLE) {
                    upload_result.setText("已完成");

                }
                positiveStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(IdCardScanActivity.positivebitmap));
                negativeStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(IdCardScanActivity.nagetiveBitmap));
                handleStr = URLEncoder.encode(BitmapToBase64.BitmaptoBase64Encode(IdCardScanActivity.handleBitmap));
                custFace=BitmapToBase64.BitmaptoBase64Encode(IdCardScanActivity.positivebitmap);
            }
        }
    }

    /**
     * 验证手机号
     *
     * @return
     */
    private boolean checkPhone() {
        String phoneNum = userPhoneEv.getText().toString().trim();
        boolean phone = NumUtil.isPhoneNum(phoneNum);
        return phone;
    }

    /**
     * 判断新老用户
     *
     * @return
     */
    private boolean isOlder() {
        String idNum = idNumberTv.getText().toString().trim();
        if (TextUtils.isEmpty(idNum)) {
            ViewUtils.showToast(this, "身份证号码为空，请重新识别！");
            return false;
        }
        String agentNum = LocalManager.getInstance(this).getAgentNum();
        if (TextUtils.isEmpty(agentNum)) {
            redirectToActivity(LoginActivity.class);
            return false;
        }
        WSUser.checkUser(this, agentNum, idNum, new BaseResponseHandler(this, false, null) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    if (response.getString("status").equals(Constant.STATUS_OK)) {
                        CheckOldCustVo checkOldCustVo = (CheckOldCustVo) ParseJsonUtils.getInstance().getObject(response.toString(), CheckOldCustVo.class);
                        boolean old = checkOldCustVo.isOldCust();
                        if (!old) return;
                        isOlder = true;
                        setUserOlder(true);
                        return;
                    }else{
                        String error = response.getString("msg");
                        ViewUtils.showToast(IdCardInfoActivity.this, error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setUserOlder(false);
                if (isOlder) isOlder = false;
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                setUserOlder(false);
                if (isOlder) isOlder = false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        if (isOlder) return true;
        return false;
    }

//    /**
//     * 异常判断
//     *
//     * @param throwable
//     */
//    private void parseException(Throwable throwable) {
//        if (throwable instanceof UnknownHostException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "请求地址无效，请检查网络！");
//        } else if (throwable instanceof SocketException) {
//            if (throwable instanceof ConnectException) {
//                ViewUtils.showToast(IdCardInfoActivity.this, "服务器连接异常！");
//                return;
//            }
//            ViewUtils.showToast(IdCardInfoActivity.this, "服务器异常！");
//        } else if (throwable instanceof SocketTimeoutException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "服务器响应超时！");
//        } else if (throwable instanceof ConnectTimeoutException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "连接服务器超时！");
//        } else if (throwable instanceof IOException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "获取到的数据流异常！");
//        } else if (throwable instanceof NullPointerException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "没有获取到任何数据！");
//        } else if (throwable instanceof JSONException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "获取的数据有误！");
//        } else if (throwable instanceof ParseException) {
//            ViewUtils.showToast(IdCardInfoActivity.this, "数据解析有误！");
//        }
//    }

    /**
     * 根据新老用户改变视图
     *
     * @param older
     */
    private void setUserOlder(boolean older) {
        if (older) {
            if (upload_relativelayout.getVisibility() == View.VISIBLE) {
                upload_relativelayout.setVisibility(View.GONE);
            }
            if (uploadFinishTv.getVisibility() == View.GONE) {
                uploadFinishTv.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (upload_relativelayout.getVisibility() == View.GONE) {
            upload_relativelayout.setVisibility(View.VISIBLE);
        }
        if (uploadFinishTv.getVisibility() == View.VISIBLE) {
            uploadFinishTv.setVisibility(View.GONE);
        }
    }


    /**
     * 提交订单
     */
    private void submitResult() {
        //返回结果
        boolean hasPhone = checkPhone();
        if (!hasPhone) {
            ViewUtils.showToast(this, "联系人号码为空或输入有误，请重新输入手机号！");
            return;
        }
        String agentNum = LocalManager.getInstance(this).getAgentNum();
        if (TextUtils.isEmpty(agentNum)) {
            redirectToActivity(LoginActivity.class);
            return;
        }
        final RequestParams requestParams = new RequestParams();
        requestParams.add("agentNum", agentNum);
        requestParams.add("certNum", idCardInfo.getCertNumber());
        requestParams.add("custName", idCardInfo.getPartyName());
        requestParams.add("sex", idCardInfo.getGender());
        requestParams.add("certAddr", idCardInfo.getCertAddress());
        requestParams.add("nation", idCardInfo.getNation());
        requestParams.add("issuingauthority", idCardInfo.getCertOrg());
        requestParams.add("birthday", idCardInfo.getBornDay());
        requestParams.add("certvaliddate", idCardInfo.getEffDate());
        requestParams.add("certexpdate", idCardInfo.getExpDate());
        if (isOlder) requestParams.add("isOldCust", "1");
        else requestParams.add("isOldCust", "2");
        String phone = userPhoneEv.getText().toString().trim();
        requestParams.add("contactTel", Java3DESUtil.encryptThreeDESECB(phone));
        String telPhone = LocalManager.getInstance(this).getArchivePhone();
        if (TextUtils.isEmpty(telPhone)) {
            redirectToActivity(PhoneCheckActivity.class);
            return;
        }
        requestParams.add("telPhone", telPhone);
        requestParams.add("frontImg", positiveStr);
        requestParams.add("backImg", negativeStr);
        requestParams.add("handImg", handleStr);
        requestParams.add("custFace", custFace);

        WSUser.submitIDCardOrder(this, requestParams, new BaseResponseHandler(IdCardInfoActivity.this, true, "正在提交中......") {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    if (response.getString("status").equals(Constant.STATUS_OK)) {
                        CommDialog.showDialog(IdCardInfoActivity.this, "返档激活办理成功！",
                                "确定", new CommDialog.DialogOneCallBack() {
                                    @Override
                                    public void callBack(Dialog dialog) {
                                        dialog.dismiss();
                                        startActivity(new Intent(IdCardInfoActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                    }else{
                        CommDialog.showDialog(IdCardInfoActivity.this, "返档请求失败！" + response.getString("msg"),
                                "确认并返回", new CommDialog.DialogOneCallBack() {
                                    @Override
                                    public void callBack(Dialog dialog) {
                                        dialog.dismiss();
                                        startActivity(new Intent(IdCardInfoActivity.this,MainActivity.class));
                                        finish();
                                    }
                                });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtils.showToast(IdCardInfoActivity.this,"返档请求发送异常！");
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                System.out.println("onfailure的入参："+requestParams.toString());
                System.out.println("失败报错："+error+content);
                setUserOlder(false);
                if (isOlder) isOlder = false;
                ViewUtils.showToast(IdCardInfoActivity.this,"返档请求发送失败！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, String response) {
                super.onSuccess(statusCode, response);
            }
        });
    }

    @Override
    public void submit() {
        submitResult();
    }
}
