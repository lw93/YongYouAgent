package com.asia.yongyou.yongyouagent.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/8/31.
 */
public class WorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上下文
    private Context mContext;
    //数据
    private List<Map<String, Object>> datas;

    //适配器初始化
    public WorkAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mywork_grid_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // /将数据与item视图进行绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).imageView.setImageResource((Integer) datas.get(position).get("image"));
        ((MyViewHolder) holder).textView.setText((String) datas.get(position).get("text"));

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义ViewHolder，用于加载图片
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.mywork_grid_item_img);
            textView = (TextView) view.findViewById(R.id.mywork_grid_item_text);
        }
    }

}
