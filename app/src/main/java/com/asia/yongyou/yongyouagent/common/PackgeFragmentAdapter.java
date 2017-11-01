package com.asia.yongyou.yongyouagent.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 选择套餐适配器
 *
 * @author Created by liuwei
 * @time on 2017/10/9
 */
public class PackgeFragmentAdapter extends FragmentPagerAdapter {
    //fragment列表
    private List<Fragment> fragments;
    //tab名的列表
    private List<String> titles;

    public PackgeFragmentAdapter(FragmentManager fm, List<Fragment> listFragment, List<String> listTitles) {
        super(fm);
        this.fragments = listFragment;
        this.titles = listTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
