package com.asia.yongyou.yongyouagent.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.MainActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.utils.StringUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.ClearButtonEditText;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private ClearButtonEditText editUsername, editPassword;
    private DisableButton btnLogin;
    private CheckBox checkRememberPwd, loginPwdVisi;
    private TextView errorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        editUsername = (ClearButtonEditText) findViewById(R.id.edit_username);
        //回显代理商工号
        editUsername.setText(LocalManager.getInstance(getApplicationContext()).getAgentNum());
        editPassword = (ClearButtonEditText) findViewById(R.id.edit_pwd);
        checkRememberPwd = (CheckBox) findViewById(R.id.check_remember_pwd);
        loginPwdVisi = (CheckBox) findViewById(R.id.loginPwdVisi);
        errorInfo = (TextView) findViewById(R.id.error_info);
        btnLogin = (DisableButton) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(this);

        checkRememberPwd.setChecked(false);
        loginPwdVisi.setChecked(false);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginPwdVisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    Editable etable_setting = editPassword.getText();
                    Selection.setSelection(etable_setting,
                            etable_setting.length());
                } else {
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Editable etable_setting = editPassword.getText();
                    Selection.setSelection(etable_setting,
                            etable_setting.length());
                }

            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btnLogin.setDisableButtonClickable(check());
                } else {
                    btnLogin.setDisableButtonClickable(false);
                }

            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    btnLogin.setDisableButtonClickable(check());
                } else {
                    btnLogin.setDisableButtonClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (check()) {
                    //TODO 1.调用登录接口 2.记住密码逻辑处理 3.登录错误信息显示处理
                    agentLoginCheck(editUsername.getText().toString().trim(), editPassword.getText().toString().trim());
//                    if (checkRememberPwd.isChecked()){
//                        LocalManager.getInstance(LoginActivity.this).saveUserPreferences(editUsername.getText().toString().trim(),
//                                editPassword.getText().toString().trim());
//                    }
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

                break;
        }
    }

    private void agentLoginCheck(final String agentNum, final String agentPwd) {
        btnLogin.setClickable(false);
        WSUser.agentLogin(this, agentNum, agentPwd, new BaseResponseHandler(this, true,
                StringUtils.getResource(LoginActivity.this, R.string.agent_login_request).toString()) {
            @Override
            public void onSuccess(int statusCode, final org.json.JSONObject response) {
                super.onSuccess(statusCode, response);

                try {
                    if (response.getString("status").equals("000000")) {
                        //登陆成功
                        //记住密码
                        LocalManager.getInstance(LoginActivity.this).saveAgentNum(agentNum);
                        if (checkRememberPwd.isChecked()) {
                            LocalManager.getInstance(LoginActivity.this).saveUserPreferences(agentNum, agentPwd);
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        final String erroeMsg = response.getString("msg");
                        errorInfo.setText(erroeMsg);
                        errorInfo.setVisibility(View.VISIBLE);
                        errorInfo.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorInfo.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ViewUtils.showToast(LoginActivity.this,"登录请求失败！");
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                System.out.println("登录onfailure:" + error + content);
                ViewUtils.showToast(LoginActivity.this, "登录请求发送失败，请稍后再试！");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                btnLogin.setClickable(true);
            }
        });
    }

    /**
     * 校验输入
     */
    private boolean check() {
        if (TextUtils.isEmpty(editUsername.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
            return false;
        }
        return true;
    }
}
