package com.asia.yongyou.yongyouagent.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.activity.UserActivity;

import java.util.List;
import java.util.Map;

/**
 * 我的工作台列表适配器
 *
 * @author Created by liuwei
 * @time on 2017/9/3
 */
public class MyWorkAdapter extends SimpleAdapter {
    private Context context;
    private List<? extends Map<String, ?>> data;

    public MyWorkAdapter(Context context, List<? extends Map<String, ?>> data) {
        super(context, data, R.layout.mywork_grid_item, new String[]{UserActivity.MYWORK_IMAGE,
                UserActivity.MYWORK_TEXT}, new int[]{R.id.mywork_grid_item_img, R.id.mywork_grid_item_text});
        this.context = context;
        this.data = data;
    }

    public MyWorkAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mywork_grid_item, null);
            LinearLayout rootView = (LinearLayout) convertView.findViewById(R.id.mywork_grid_root);
            rootView.setFocusableInTouchMode(false);
            rootView.setClickable(false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.mywork_grid_item_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.mywork_grid_item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> iconMap = (Map<String, Object>) data.get(position).get(UserActivity.MYWORK_IMAGE);
        Integer iconNormal = (Integer) iconMap.get(UserActivity.MYWORK_IMAGE_NORMAL);
        Integer iconPressed = (Integer) iconMap.get(UserActivity.MYWORK_IMAGE_PRESSED);
        StateListDrawable bg = new StateListDrawable();
        Drawable normalDrawable = context.getResources().getDrawable(iconNormal);
        Drawable pressedDrawable = context.getResources().getDrawable(iconPressed);
        //获取对应的属性值 Android框架自带的属性 attr
        int pressed = android.R.attr.state_pressed;
        int enabled = android.R.attr.state_enabled;
        bg.addState(new int[]{enabled, pressed}, pressedDrawable);
        bg.addState(new int[]{enabled}, normalDrawable);
        bg.addState(new int[]{}, normalDrawable);
        holder.imageView.setBackgroundDrawable(bg);
        holder.imageView.setFocusableInTouchMode(true);
        holder.imageView.setClickable(true);
        String text = (String) data.get(position).get(UserActivity.MYWORK_TEXT);
        holder.textView.setText(text + "");
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
