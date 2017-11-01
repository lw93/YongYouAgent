package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ichen on 2017/10/18.
 */
public class NumListVo implements Serializable {


	/**
	 * success : true
	 * msg : 操作成功
	 * status : 000000
	 * data : [{"amount":"0","telecomId":"1","minRentFee":"0","telPhone":"17000001900"},{"amount":"0","telecomId":"1","minRentFee":"0","telPhone":"17000115194"}]
	 */

	private List<DataBean> data;


	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * amount : 0
		 * telecomId : 1
		 * minRentFee : 0
		 * telPhone : 17000001900
		 */

		private String amount;
		private String telecomId;
		private String minRentFee;
		private String telPhone;

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getTelecomId() {
			return telecomId;
		}

		public void setTelecomId(String telecomId) {
			this.telecomId = telecomId;
		}

		public String getMinRentFee() {
			return minRentFee;
		}

		public void setMinRentFee(String minRentFee) {
			this.minRentFee = minRentFee;
		}

		public String getTelPhone() {
			return telPhone;
		}

		public void setTelPhone(String telPhone) {
			this.telPhone = telPhone;
		}
	}
}
