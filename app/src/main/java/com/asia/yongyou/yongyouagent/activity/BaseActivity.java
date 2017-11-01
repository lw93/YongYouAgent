package com.asia.yongyou.yongyouagent.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;


public class BaseActivity extends FragmentActivity {

    private long mExitTime;

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("exit");
        this.registerReceiver(this.broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (null != broadcastReceiver) this.unregisterReceiver(this.broadcastReceiver);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                myExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void myExit() {
        Intent intent = new Intent();
        intent.setAction("exit");
        this.sendBroadcast(intent);
        super.finish();
    }

}
