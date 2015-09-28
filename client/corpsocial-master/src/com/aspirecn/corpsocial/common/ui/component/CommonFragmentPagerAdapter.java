package com.aspirecn.corpsocial.common.ui.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by yinghuihong on 15/7/27.
 */
public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
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
