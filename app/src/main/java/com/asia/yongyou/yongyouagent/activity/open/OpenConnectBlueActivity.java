package com.asia.yongyou.yongyouagent.activity.open;


import android.content.Intent;

import com.asia.yongyou.yongyouagent.activity.fandang.idcard.IdentificationActivity;
import com.asia.yongyou.yongyouagent.constant.Constant;
import com.asia.yongyou.yongyouagent.entity.IDCardInfo;

/**
 * Created by ichen on 2017/10/23.
 */
public class OpenConnectBlueActivity extends IdentificationActivity {


	@Override
	public void redirect(IDCardInfo idCardInfo) {
//		super.redirect(idCardInfo);
		final Intent intentIdCardInfo = new Intent(this, OpenCustomerActivity.class);
		intentIdCardInfo.putExtra(Constant.IDCARDINFO, idCardInfo);
		startActivity(intentIdCardInfo);

	}
}
