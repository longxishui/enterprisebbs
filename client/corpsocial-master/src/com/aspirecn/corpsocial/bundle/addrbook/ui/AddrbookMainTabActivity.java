/**
 * @(#) AddressBook_MainTabActivity.java Created on 2013-11-5
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.bundle.common.facade.AppStartPath;
import com.aspirecn.corpsocial.bundle.common.listener.GetCorpViewDefRespSubject;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingViewModel;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.ui.component.tabView.ScrollableTabView;
import com.aspirecn.corpsocial.common.ui.component.tabView.TabEntity;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人模块主页
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.addrbook_main_tab_activity)
public class AddrbookMainTabActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener, GetCorpViewDefRespSubject.GetCorpViewDefRespListener {

    @ViewById(R.id.addrbook_main_tabView)
    ScrollableTabView mScrollableTabView;

    @ViewById(R.id.addrbook_viewpager)
    ViewPager mViewPager;

    /**
     * 控件修正
     */
    @AfterViews
    void init() {
        ActionBarFragment fab = ActionBarFragment_.builder().navigation(true).startPath(AppStartPath.ADDRESS_BOOK.START_PATH).
                title(getString(R.string.common_main_tab_linkman)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);

        List<Tab> tabs = initTabs();

        List<Fragment> fragments = new ArrayList<Fragment>();
        List<TabEntity> tabEntities = new ArrayList<TabEntity>();
        for (Tab tab : tabs) {
            try {
                // view pager
                Class c = Class.forName(tab.cls);
                fragments.add((Fragment) c.newInstance());
                // tab view
                TabEntity tabEntity = new TabEntity();
                tabEntity.setName(tab.name);
                tabEntities.add(tabEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initTabView(tabEntities);
        initViewPager(fragments);
    }

    protected List<Tab> initTabs() {
        Tabs t = AssetsUtils.getInstance().read(this, "addressbook.json", Tabs.class);
        return t.tabs;
    }

    private void initTabView(List<TabEntity> tabEntities) {
        mScrollableTabView.init(this, new ScrollableTabView.OntabChangeListener() {
            @Override
            public void notifyTabChange(int position) {
                mViewPager.setCurrentItem(position, true);
            }
        }, tabEntities);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager(List<Fragment> fragments) {
        mViewPager.setAdapter(new AddrbookMainTabPagerAdapter(getSupportFragmentManager(), fragments));
        mViewPager.requestDisallowInterceptTouchEvent(true);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mScrollableTabView.updateTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 搜索按钮方法
     */
    protected void startSearchActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AddrbookSearchActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        ShowButton sb = AssetsUtils.getInstance().read(AspirecnCorpSocial.getContext(), "addressbook_show.json", ShowButton.class);
        if(sb.showOrNot){
            fab.build().setFirstButtonIcon(R.drawable.actionbar_add_btn_bg_selector).apply();
            fab.build().setFirstButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), NewFriendsActivity_.class);
                    startActivity(intent);
                }
            }).apply();
        }
        fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
        fab.build().setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity();
            }
        }).apply();
    }

    @UiThread
    @Override
    public void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event) {
        mScrollableTabView.updateTabColor(event.fontColor);
    }

    protected class Tab {
        String name;
        String cls;
    }

    protected class Tabs {
        public List<Tab> tabs;
    }

    protected  class ShowButton{
        public boolean showOrNot;
    }
}
