package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.GetCorpViewDefRespSubject;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.ConcernMeBBSFragment;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.FragmentViewPagerAdapter;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.ui.component.tabView.ScrollableTabView;
import com.aspirecn.corpsocial.common.ui.component.tabView.TabEntity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.workgrp_bbs_concernme_activity)
public class ConcernMeBBSListActivity extends FragmentActivity implements GetCorpViewDefRespSubject.GetCorpViewDefRespListener {

    /** listView的HeadView,用来显示帖子内容 */
    /**
     * 我创建的
     */
    public static final int BBS_CREATE = 0;
    /**
     * 我参与的
     */
    public static final int BBS_PARTIN = 1;
    /**
     * 标签名字
     */
    List<TabEntity> tabEntityList = new ArrayList<TabEntity>();
    @ViewById(R.id.workgrp_bbs_concernme_vp)
    ViewPager mViewPager;
    @ViewById(R.id.workgrp_concernme_title_tabid)
    ScrollableTabView tabView;
    /**
     * 关于我的fragment
     */
    private ConcernMeBBSFragment concernMeBBSFragment_create;
    private ConcernMeBBSFragment concernMeBBSFragment_partin;
    private FragmentViewPagerAdapter adapter;
    private List<Fragment> fragments;

    @AfterViews
    protected void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title("与我相关").
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        TabEntity tabEntity1 = new TabEntity("我发起", 0);
        TabEntity tabEntity2 = new TabEntity("我参与", 0);
        tabEntityList.add(tabEntity1);
        tabEntityList.add(tabEntity2);

        fragments = new ArrayList<Fragment>();
        concernMeBBSFragment_create = new ConcernMeBBSFragment();
        Bundle arguments_create = new Bundle();
        arguments_create.putInt("type", BBS_CREATE);
        concernMeBBSFragment_create.setArguments(arguments_create);

        concernMeBBSFragment_partin = new ConcernMeBBSFragment();
        Bundle arguments_partin = new Bundle();
        arguments_partin.putInt("type", BBS_PARTIN);
        concernMeBBSFragment_partin.setArguments(arguments_partin);

        fragments.add(concernMeBBSFragment_create);
        fragments.add(concernMeBBSFragment_partin);
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                mViewPager, fragments);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
                if (tabView != null) {
                    tabView.updateTabView(i);
                }
            }
        });
        tabView.init(this, new ScrollableTabView.OntabChangeListener() {
            @Override
            public void notifyTabChange(int position) {
                mViewPager.setCurrentItem(position, true);
            }
        }, tabEntityList);
    }

    @UiThread
    @Override
    public void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event) {
        tabView.updateTabColor(event.fontColor);
    }
}
