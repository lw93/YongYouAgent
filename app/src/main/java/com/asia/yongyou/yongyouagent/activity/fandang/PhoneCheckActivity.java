package com.asia.yongyou.yongyouagent.activity.fandang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.BaseActivity;
import com.asia.yongyou.yongyouagent.activity.fandang.idcard.IdentificationActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.utils.Java3DESUtil;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.ClearButtonEditText;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 反档激活号码校验
 */
public class PhoneCheckActivity extends BaseActivity implements View.OnClickListener {
    private ClearButtonEditText phone, simCard;
    private DisableButton checkBtn;
    private TextView title;
    private ImageView headerback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_check);
        initview();
    }

    private void initview() {
        title = (TextView) this.findViewById(R.id.header_text);
        title.setText("返档激活号码校验");
        headerback = (ImageView) this.findViewById(R.id.header_left_image);
        headerback.setVisibility(View.VISIBLE);
        headerback.setOnClickListener(this);
        phone = (ClearButtonEditText) findViewById(R.id.fandang_phone);
        simCard = (ClearButtonEditText) findViewById(R.id.fandang_puk);
        simCard.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        checkBtn = (DisableButton) findViewById(R.id.check_btn);
        checkBtn.setOnClickListener(this);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    checkBtn.setDisableButtonClickable(check());
                } else {
                    checkBtn.setDisableButtonClickable(false);
                }

            }
        });
        simCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    checkBtn.setDisableButtonClickable(check());
                } else {
                    checkBtn.setDisableButtonClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_btn:
                String telPhoneEnc = Java3DESUtil.encryptThreeDESECB(phone.getText().toString().trim());
                String simCardNum = simCard.getText().toString().trim();
                if (simCardNum.length()!=5){
                    ViewUtils.showToast(PhoneCheckActivity.this,"请输入SIM卡号后五位");
                }else{
                    ArchivePhoneCheck(telPhoneEnc, LocalManager.getInstance(PhoneCheckActivity.this).getAgentNum(), simCardNum);
                }

                break;
            case R.id.header_left_image:
                finish();
                break;
        }
    }

    private void ArchivePhoneCheck(final String archiveTel, final String agentNum, final String simCardNum) {
        checkBtn.setClickable(false);
        WSUser.archiveCheck(PhoneCheckActivity.this, archiveTel, agentNum, simCardNum,
                new BaseResponseHandler(PhoneCheckActivity.this, true, "正在验证，请稍候...") {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        try {
                            if (response.getString("status").equals(Constant.STATUS_OK)) {
                                //验证成功，跳到身份证识别卡页面
                                SharedPreferences sp = getSharedPreferences("archiveInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("archivetel", archiveTel);
                                editor.putString("simCard", simCardNum);
                                editor.commit();
                                Intent intent = new Intent(PhoneCheckActivity.this, IdentificationActivity.class);
                                startActivity(intent);
                            } else {
                                ViewUtils.showToast(PhoneCheckActivity.this, response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ViewUtils.showToast(PhoneCheckActivity.this, "验证失败，请稍后再试");
                        }
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        super.onFailure(error, content);
//                        ViewUtils.showToast(PhoneCheckActivity.this, error.toString());
                        ViewUtils.showToast(PhoneCheckActivity.this, "验证请求发送失败，请重试！");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        checkBtn.setClickable(true);
                    }
                });
    }


    /**
     * 校验输入
     */
    private boolean check() {
        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(simCard.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        String telPhone = phone.getText().toString().trim();
        if (!TextUtils.isEmpty(telPhone)) LocalManager.getInstance(this).saveArchivePhone(telPhone);
    }
}
