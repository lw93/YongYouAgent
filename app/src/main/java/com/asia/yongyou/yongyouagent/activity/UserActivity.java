package com.asia.yongyou.yongyouagent.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.login.LoginActivity;
import com.asia.yongyou.yongyouagent.activity.manager.LocalManager;
import com.asia.yongyou.yongyouagent.common.MyWorkAdapter;
import com.asia.yongyou.yongyouagent.entity.GetAgentInfo;
import com.asia.yongyou.yongyouagent.utils.ParseJsonUtils;
import com.asia.yongyou.yongyouagent.utils.ViewUtils;
import com.asia.yongyou.yongyouagent.widget.DisableButton;
import com.asia.yongyou.yongyouagent.ws.BaseResponseHandler;
import com.asia.yongyou.yongyouagent.ws.WSUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Created by liuwei
 * @time on 2017/9/5
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {

    private View toolBarView;
    private TextView toolBarTitleTv;
    private ImageView toolBarArrowImg;
    private CircleImageView userHeaderImg;
    private TextView userNameTv;
    private TextView userSellCountTv;
    private GridView userWorkGv;
    private DisableButton userLogOutBtn;
    public static final String MYWORK_IMAGE = "image";
    public static final String MYWORK_IMAGE_NORMAL = "imageNormal";
    public static final String MYWORK_IMAGE_PRESSED = "imagePressed";
    public static final String MYWORK_TEXT = "text";
    private GetAgentInfo getAgentInfo;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }


    private void initView() {
        toolBarView = this.findViewById(R.id.toolbar);
        toolBarTitleTv = (TextView) toolBarView.findViewById(R.id.header_text);
        toolBarArrowImg = (ImageView) toolBarView.findViewById(R.id.header_left_image);
        userHeaderImg = (CircleImageView) this.findViewById(R.id.mywork_head_img);
        userNameTv = (TextView) this.findViewById(R.id.mywork_head_username);
        userSellCountTv = (TextView) this.findViewById(R.id.mywork_sell);
        userWorkGv = (GridView) this.findViewById(R.id.mywork_task);
        userWorkGv.setVisibility(View.INVISIBLE);
        userLogOutBtn = (DisableButton) this.findViewById(R.id.mywork_logout);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        toolBarTitleTv.setText(getString(R.string.mywork) + "");
        toolBarArrowImg.setOnClickListener(this);
//        userHeaderImg.setOnClickListener(this);
        userLogOutBtn.setOnClickListener(this);
        userLogOutBtn.setDisableButtonClickable(true);
        MyWorkAdapter simpleAdapter = new MyWorkAdapter(this, getWorkList());
        userWorkGv.setAdapter(simpleAdapter);
        final String agentNum = LocalManager.getInstance(this).getAgentNum();
        userNameTv.setText(agentNum);
        WSUser.getAgentInfo(UserActivity.this, agentNum, new BaseResponseHandler(UserActivity.this, false,
                null) {
            @Override
            public void onSuccess(int statusCode, final JSONObject response) {
                super.onSuccess(statusCode, response);
                try {
                    if (response.getString("status").equals("000000")) {
                        getAgentInfo = (GetAgentInfo) ParseJsonUtils.getInstance().getObject(response.toString(), GetAgentInfo.class);
                        final String totalOrder = getAgentInfo.getTotalOrder();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userSellCountTv.setText(totalOrder);
                            }
                        });
                    } else {
                        ViewUtils.showToast(UserActivity.this, "页面初始化失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }

    /**
     * 设置用户姓名
     *
     * @param string
     */
    private void setUserName(String string) {
        if (!TextUtils.isEmpty(string)) userNameTv.setText(string);
    }

    /**
     * 设置用户头像
     *
     * @param bitmap
     */
    private void setUserHeaderImg(Bitmap bitmap) {
        if (null != bitmap) userHeaderImg.setImageBitmap(bitmap);
    }

    /**
     * 设置销量
     *
     * @param string
     */
    private void setUserSellCount(String string) {
        if (!TextUtils.isEmpty(string)) userSellCountTv.setText(string);
    }

    /**
     * 初始化数据
     *
     * @return
     */
    private List<Map<String, Object>> getWorkList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(16);
        Map<String, Object> map = null;
        // 图片封装为一个数组
        int[] iconNormal = {R.mipmap.czjl_nor, R.mipmap.jfjl_nor};
        int[] iconPressed = {R.mipmap.czjl_down, R.mipmap.jfjl_down};
        String[] iconName = {"充值记录", "缴费记录"};
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> iconMap = new HashMap<String, Object>(2);
            iconMap.put(MYWORK_IMAGE_NORMAL, iconNormal[i]);
            iconMap.put(MYWORK_IMAGE_PRESSED, iconPressed[i]);
            map = new HashMap<String, Object>(2);
            map.put(MYWORK_IMAGE, iconMap);
            map.put(MYWORK_TEXT, iconName[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mywork_logout:
                LocalManager.getInstance(this).clearUserPreferences();
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            break;
            case R.id.header_left_image:
                finish();
            break;
        }

    }

    /**
     * 注销功能
     */
    private void setUserLogOut() {
        LocalManager.getInstance(this).clearUserPreferences();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mywork_head_default_man);
        setUserHeaderImg(bitmap);
        setUserName(getString(R.string.mywork_username_unlogin));
        setUserSellCount("0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
