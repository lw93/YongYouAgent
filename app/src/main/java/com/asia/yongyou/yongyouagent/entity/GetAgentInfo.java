package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;

/**
 * Created by ichen on 2017/9/8.
 */
public class GetAgentInfo implements Serializable {
	private String totalOrder;

	public String getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(String totalOrder) {
		this.totalOrder = totalOrder;
	}
}
