package com.asia.yongyou.yongyouagent.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.login.LoginActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.CommDialog;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.Version;
import com.asia.yongyou.yongyouagent.utils.CheckPhoneStatus;
import com.asia.yongyou.yongyouagent.utils.ParseJsonUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.version.UpdateVersion;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ichen on 2017/8/29.
 */
public class SplashActivity extends BaseActivity {
    private AlphaAnimation alphaAnim;
    private View view;
    private Version version;
    private TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!CheckPhoneStatus.checkNetworkWithDialog(this)) {
            return;
        }
        initView();
        initData();
    }

    private void initData() {
        alphaAnim = new AlphaAnimation(0.3f, 1.0f);
        alphaAnim.setDuration(3000);
        view.startAnimation(alphaAnim);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

			@Override
			public void onAnimationEnd(Animation animation) {
				versionText.setText("当前版本为："+UpdateVersion.instance(getApplicationContext()).getViersionName());
				login();
//				redirect();
			}

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

	private void login() {
		String agentNum = LocalManager.getInstance(SplashActivity.this).getAgentNum();
		String agentPwd = LocalManager.getInstance(SplashActivity.this).getAgentPassword();
		if (!"".equals(agentNum)){
			WSUser.agentLogin(SplashActivity.this,agentNum,agentPwd,new BaseResponseHandler(SplashActivity.this,
					true,"正在登陆，请稍候..."){
				@Override
				public void onSuccess(int statusCode, JSONObject response) {
					super.onSuccess(statusCode, response);
					try {
						if (response.getString("status").equals("000000")){
							updateVersion();
							Intent intent = new Intent(SplashActivity.this,MainActivity.class);
							startActivity(intent);
							finish();

						}else{
							updateVersion();
							redirect();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						updateVersion();
						redirect();
					}
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					ViewUtils.showToast(SplashActivity.this,"请重新登录！");
					updateVersion();
					redirect();
				}
			});
		}else{
			updateVersion();
			redirect();
		}
	}

	private void updateVersion() {
		WSUser.updateVersion(this,new BaseResponseHandler(this,true,"检查更新版本......"){
			@Override
			public void onSuccess(int statusCode, JSONObject response) {
				super.onSuccess(statusCode, response);
				System.out.println("response="+response.toString());
				try{
					//请求成功
					if (response.getString("status").equals(Constant.STATUS_OK)){
						version = (Version) ParseJsonUtils.getInstance().getObject(response.toString(),Version.class);
						UpdateVersion.instance(SplashActivity.this).saveCurrentVersion(version);
						System.out.println("检查更新响应中的版本号="+version.getVersion_code());

                        if (UpdateVersion.instance(getApplicationContext()).checkVersionCode()) {
                            if (Constant.FORCE_UPDATE.equals(version.getUpdate_tag())) {
                                //强制更新
                                forceUpdateDialog();
                            } else {
                                //更新提示框
                                showUpdateDialog();
                            }
                        }
                    } else {
						System.out.println("失败原因:::"+response.getString("msg"));
						ViewUtils.showToast(SplashActivity.this, "检查更新失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                ViewUtils.showToast(SplashActivity.this, "检查更新失败!");
	            error.printStackTrace();
            }
        });
    }

    //强制更新
    private void forceUpdateDialog() {
        CommDialog.showDialog(this, "发现新版本，请更新！", "确定", new CommDialog.DialogOneCallBack() {
            @Override
            public void callBack(Dialog dialog) {
                dialog.dismiss();
                UpdateVersion.instance(SplashActivity.this).openWeb(version.getApk_url());
            }
        });
    }

    //不是强制更新
    private void showUpdateDialog() {
        CommDialog.showDialog(this, "发现新版本，请更新！", new CommDialog.DialogCallBack() {
            @Override
            public void open(Dialog dialog) {
                dialog.dismiss();
                //打开浏览器下载
                UpdateVersion.instance(SplashActivity.this).openWeb(version.getApk_url());
            }

            @Override
            public void cancle(Dialog dialog) {
                dialog.dismiss();
                redirect();
            }
        });
    }

    public void redirect() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        view = findViewById(R.id.view);
        versionText = (TextView) view.findViewById(R.id.version);
    }


}
