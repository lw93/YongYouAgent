package com.asia.yongyou.yongyouagent.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.fandang.idcard.IdentificationActivity;
import com.asia.yongyou.yongyouagent.common.MyWorkAdapter;
import com.asia.yongyou.yongyouagent.widget.DisableButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ichen on 2017/5/8.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private View toolBarView;
    private TextView toolBarTitleTv;
    private ImageView toolBarArrowImg;
    private CircleImageView userHeaderImg;
    private TextView userNameTv;
    private TextView userSellCountTv;
    private GridView userWorkGv;
    private DisableButton userLogOutBtn;
    private Context context;
    public static final String MYWORK_IMAGE = "image";
    public static final String MYWORK_IMAGE_NORMAL = "imageNormal";
    public static final String MYWORK_IMAGE_PRESSED = "imagePressed";
    public static final String MYWORK_TEXT = "text";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (null == context) {
            this.context = getActivity();
        } else {
            this.context = context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_user, container, false);
        toolBarView = view.findViewById(R.id.toolbar);
        toolBarTitleTv = (TextView) toolBarView.findViewById(R.id.header_text);
        toolBarTitleTv.setText(getString(R.string.mywork) + "");
        toolBarArrowImg = (ImageView) toolBarView.findViewById(R.id.header_left_image);
        toolBarArrowImg.setOnClickListener(this);
        userHeaderImg = (CircleImageView) view.findViewById(R.id.mywork_head_img);
        userHeaderImg.setOnClickListener(this);
        userNameTv = (TextView) view.findViewById(R.id.mywork_head_username);
        userSellCountTv = (TextView) view.findViewById(R.id.mywork_sell);
        userWorkGv = (GridView) view.findViewById(R.id.mywork_task);
        userLogOutBtn = (DisableButton) view.findViewById(R.id.mywork_logout);
        userLogOutBtn.setOnClickListener(this);
        userLogOutBtn.setDisableButtonClickable(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    // TODO: 2017/9/4  初始化数据
    private void initData() {
        MyWorkAdapter simpleAdapter = new MyWorkAdapter(context, getWorkList());
        userWorkGv.setAdapter(simpleAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
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
        int id = view.getId();
        if (id == R.id.mywork_logout) {
            setUserLogOut();
        } else if (id == R.id.header_left_image) {
            Activity activity = (Activity) context;
            if (null != activity) activity.finish();
        } else if (id == R.id.mywork_head_img) {
            // TODO: 2017/9/4  更换头像功能
            //setUserHeaderImg();
        }

    }

    // TODO: 2017/9/4  注销功能
    private void setUserLogOut() {
        startActivity(new Intent(getActivity(), IdentificationActivity.class));
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
