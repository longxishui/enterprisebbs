package com.aspirecn.corpsocial.bundle.workgrp.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.aspirecn.corpsocial.common.ui.component.FragmentPagerAdapter;

import java.util.List;

/**
 * 同事圈适配器
 *
 * @author duyinzhou
 */
public class WorkgrpFragmentListAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public WorkgrpFragmentListAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
