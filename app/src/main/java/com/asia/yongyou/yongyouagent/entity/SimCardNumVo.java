package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ichen on 2017/10/19.
 */
public class SimCardNumVo implements Serializable {


	/**
	 * success : true
	 * msg : 操作成功
	 * status : 000000
	 * data : [{"sim":"8986011484000000789"}]
	 */

	private boolean success;
	private String msg;
	private String status;
	private List<DataBean> data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * sim : 8986011484000000789
		 */

		private String sim;

		public String getSim() {
			return sim;
		}

		public void setSim(String sim) {
			this.sim = sim;
		}
	}
}
