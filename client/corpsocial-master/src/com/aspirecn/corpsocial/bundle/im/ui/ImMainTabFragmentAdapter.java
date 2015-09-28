package com.aspirecn.corpsocial.bundle.im.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.aspirecn.corpsocial.common.ui.component.FragmentPagerAdapter;

import java.util.List;

/**
 * 工作群主页
 *
 * @author lihaiqiang
 */
public class ImMainTabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public ImMainTabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

}
