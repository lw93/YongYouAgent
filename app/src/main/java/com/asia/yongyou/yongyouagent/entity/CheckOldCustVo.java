package com.asia.yongyou.yongyouagent.entity;

import java.io.Serializable;

/**
 * Created by ichen on 2017/9/8.
 */
public class CheckOldCustVo implements Serializable {

	private boolean isOldCust;

	public boolean isOldCust() {
		return isOldCust;
	}

	public void setOldCust(boolean oldCust) {
		isOldCust = oldCust;
	}
}
