package com.asia.yongyou.yongyouagent.activity.open;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.asia.yongyou.yongyouagent.activity.MainActivity;
import com.asia.yongyou.yongyouagent.activity.fandang.idcard.IdCardInfoActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.CommDialog;
import com.asia.yongyou.yongyouagent.entity.IDCardInfo;
import com.asia.yongyou.yongyouagent.interfaces.ConnectInterface;
import com.asia.yongyou.yongyouagent.interfaces.CustomerInterface;
import com.asia.yongyou.yongyouagent.utils.Java3DESUtil;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ichen on 2017/10/23.
 */
public class OpenCustomerActivity extends IdCardInfoActivity {

	private String isOldCust;
	private String contactTelEnc;
	private String openTel,openSim;
	@Override
	public void submit() {
		ViewUtils.showToast(OpenCustomerActivity.this,"提交开户订单");
		String agentNum = LocalManager.getInstance(this).getAgentNum();
		contactTelEnc= Java3DESUtil.encryptThreeDESECB(userPhoneEv.getText().toString().trim());
		SharedPreferences sp=getSharedPreferences("openInfo", Context.MODE_PRIVATE);
		openTel = sp.getString("openTel","");
		openSim =sp.getString("openSim","");
		if (isOlder){
			isOldCust="1";
		}else{
			isOldCust="2";
		}

		SharedPreferences app=getSharedPreferences("prodIdList",Context.MODE_PRIVATE);
		String prodIdList = app.getString("prodIdList", "");
		System.out.println("prodIdList:::::"+prodIdList);



		//开户接口
		WSUser.openAccount(this,idCardInfo, agentNum,contactTelEnc,openTel,openSim,isOldCust,
				positiveStr,negativeStr,handleStr,custFace,prodIdList,new BaseResponseHandler(OpenCustomerActivity.this,true,"正在提交开户订单..."
						){
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode, response);
						try {
							if (response.getString("status").equals("000000")){
								CommDialog.showDialog(OpenCustomerActivity.this, "开户成功！",
										"确定", new CommDialog.DialogOneCallBack() {
											@Override
											public void callBack(Dialog dialog) {
												dialog.dismiss();
												startActivity(new Intent(OpenCustomerActivity.this, MainActivity.class));
												finish();
											}
										});
							}else{
								CommDialog.showDialog(OpenCustomerActivity.this, "开户失败！" + response.getString("msg"),
										"确认并返回", new CommDialog.DialogOneCallBack() {
											@Override
											public void callBack(Dialog dialog) {
												dialog.dismiss();
												startActivity(new Intent(OpenCustomerActivity.this,MainActivity.class));
												finish();
											}
										});
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ViewUtils.showToast(OpenCustomerActivity.this,"开户失败！");
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
							ViewUtils.showToast(OpenCustomerActivity.this,"开户请求发送失败");
					}
				}
				);
	}
}
