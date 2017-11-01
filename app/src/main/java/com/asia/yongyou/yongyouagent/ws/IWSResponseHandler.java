package com.asia.yongyou.yongyouagent.ws;

import org.json.JSONObject;

public interface IWSResponseHandler {

	public void onStart();

	public void onSuccess(int statusCode, JSONObject response);
	
	public void onSuccess(int statusCode, String response);

	public void onFailure(Throwable error, String content);

	public void onFinish();

}
