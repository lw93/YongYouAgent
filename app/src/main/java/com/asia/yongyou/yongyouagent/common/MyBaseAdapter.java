package com.asia.yongyou.yongyouagent.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asia.yongyou.yongyouagent.R;
import com.asia.yongyou.yongyouagent.entity.PackagesByPriceVo;
import com.asia.yongyou.yongyouagent.entity.PackagesVo;

import java.util.HashMap;

/**
 * @author Created by liuwei
 * @time on 2017/10/26
 */
public class MyBaseAdapter extends BaseAdapter {
    public int selected;
    public onSelectedListener listener;
    public HashMap<Integer, Boolean> isSelected;

    public MyBaseAdapter() {
        super();
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
    }

    // 初始化isSelected的数据
    public void initData() {

    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public int getSelectedCount() {
        return selected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
        notifyDataSetChanged();
        selected = 0;
        for (int i = 0; i < isSelected.size(); i++) {
            if (getIsSelected().get(i)) selected++;
        }
        if (listener != null) {
            listener.onSelectedItemCount(selected);
        }
    }

    public void setSelectedListener(onSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 文字半角转换为全角
     *
     * @param input
     * @return
     */
    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static class ViewHolder {
        public CheckedTextView selectBtn;
        public TextView packge;
        public TextView price;
        public ImageButton expandView;
        public TextView description;

        public ViewHolder(View view) {
            selectBtn = (CheckedTextView) view.findViewById(R.id.selectView);
            packge = (TextView) view.findViewById(R.id.packge);
            price = (TextView) view.findViewById(R.id.price);
            expandView = (ImageButton) view.findViewById(R.id.expandedView);
            description = (TextView) view.findViewById(R.id.order_descripton);
        }
    }

    public interface onSelectedListener {
        void onSelectedItem(int position, PackagesVo.Products1Bean.ProductsBean productsBean);

        void onSelectedItem(int position, PackagesVo.Products2Bean.ProductsBeanX productsBeanX);

        void onSelectedItem(int position, PackagesByPriceVo.ProductsBean productsBean);

        void onSelectedItemCount(int selected);
    }
}
