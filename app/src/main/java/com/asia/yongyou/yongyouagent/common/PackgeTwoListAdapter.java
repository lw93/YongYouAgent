package com.asia.yongyou.yongyouagent.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.entity.PackagesVo;

import java.util.HashMap;
import java.util.List;

/**
 * 套餐适配器
 *
 * @author Created by liuwei
 * @time on 2017/10/9
 */
public class PackgeTwoListAdapter extends MyBaseAdapter {

    private Context context;
    private List<PackagesVo.Products2Bean.ProductsBeanX> list;

    public PackgeTwoListAdapter(Context context, List<PackagesVo.Products2Bean.ProductsBeanX> list) {
        super();
        this.context = context;
        this.list = list;
        // 初始化数据
        initData();
    }
    // 初始化isSelected的数据
    @Override
    public void initData() {
        if (isSelected.size() > 0) isSelected.clear();
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }


    @Override
    public  void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
        notifyDataSetChanged();
        selected = 0;
        for (int i = 0; i < list.size(); i++) {
            if (getIsSelected().get(i))  selected++;
        }
        if (listener!=null){
            listener.onSelectedItemCount(selected);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_packge_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
            holder = (ViewHolder) view.getTag();
        }

        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectBtn.isChecked()) {
                    getIsSelected().put(i,false);
                } else {
                    getIsSelected().put(i,true);
                }
                if (listener != null) {
                    listener.onSelectedItem(i,list.get(i));
                }
            }
        });
        holder.selectBtn.setChecked(getIsSelected().get(i));
        holder.packge.setText(list.get(i).getProductName());
        holder.price.setText(list.get(i).getCommAdjustFee() / 100 + "元");
        holder.expandView.setOnClickListener(new View.OnClickListener() {
            boolean show = false;

            @Override
            public void onClick(View view) {
                if (!show) {
                    if (holder.description.getVisibility() == View.GONE) {
                        holder.description.setVisibility(View.VISIBLE);
                        holder.expandView.setSelected(true);
                    }
                    show = true;
                } else {
                    if (holder.description.getVisibility() == View.VISIBLE) {
                        holder.description.setVisibility(View.GONE);
                        holder.expandView.setSelected(false);
                    }
                    show = false;
                }
            }
        });
        String desc = ToDBC(list.get(i).getProductDesc());
        holder.description.setText(desc);

        return view;
    }

}
