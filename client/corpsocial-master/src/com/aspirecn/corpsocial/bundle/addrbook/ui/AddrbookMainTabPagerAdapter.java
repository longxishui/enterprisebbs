package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.aspirecn.corpsocial.common.ui.component.FragmentPagerAdapter;

import java.util.List;

/**
 * 联系人主界面adapter
 *
 * @author chenziqiang
 */
public class AddrbookMainTabPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public AddrbookMainTabPagerAdapter(FragmentManager fm, List<Fragment> list) {
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
