package com.aspirecn.corpsocial.bundle.im.ui;

/**
 * @(#) AddressBook_MainTabActivity.java Created on 2013-11-5
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;

import com.aspirecn.corpsocial.bundle.common.facade.AppStartPath;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.presenter.ImPresenter;
import com.aspirecn.corpsocial.bundle.im.presenter.impl.ImPresenterImpl;
import com.aspirecn.corpsocial.bundle.im.view.ImView;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.ui.component.tabView.ScrollableTabView;
import com.aspirecn.corpsocial.common.ui.component.tabView.TabEntity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作群主页面
 *
 * @author lihaiqiang
 */
@EActivity(R.layout.im_main_tab_fragmentactivity)
public class ImMainTabFragmentActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener, ImView {

    public static Animation animation = null;
    public int mCurrentItem = 0;// 显示页

    //内容ViewPager
    @ViewById(R.id.im_main_tab_content_vp)
    ViewPager mContentVp;

    @ViewById(R.id.im_main_tabView)
    ScrollableTabView mScrollableTabView;

    private ImPresenter mPresenter = new ImPresenterImpl(this);

    @AfterViews
    void doAfterViews() {

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.common_main_tab_msg)).
                navigation(true).startPath(AppStartPath.IM.START_PATH).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
        EventListenerSubjectLoader.getInstance().registerListener(mPresenter);
        mPresenter.doAfterViews();
    }

    @Override
    public void initView(boolean isShowMyGroup) {
        if (!isShowMyGroup) {
            showTab();
        } else {
            List<TabEntity> tabEntities = new ArrayList<TabEntity>();
            TabEntity tabEntity1 = new TabEntity();
            tabEntity1.setName("聊天");
            TabEntity tabEntity2 = new TabEntity();
            tabEntity2.setName("我的群组");
            tabEntities.add(tabEntity1);
            tabEntities.add(tabEntity2);
            initTabView(tabEntities);
        }
        initViewPager(mCurrentItem, isShowMyGroup);

    }

    /**
     * 初始化游标
     */
    private void initTabView(List<TabEntity> tabEntities) {
        mScrollableTabView.init(this, new ScrollableTabView.OntabChangeListener() {
            @Override
            public void notifyTabChange(int position) {
                mContentVp.setCurrentItem(position, true);
            }
        }, tabEntities);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager(int currIndex, boolean isShowMyGroup) {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ImChatListFragment_());
        if (isShowMyGroup) {
            fragments.add(new ImGroupListFragment_());
        }
        ImMainTabFragmentAdapter adapter = new ImMainTabFragmentAdapter(
                getSupportFragmentManager(), fragments);
        mContentVp.setAdapter(adapter);
        mContentVp.setCurrentItem(currIndex);
        mContentVp.setOnPageChangeListener(new PageChangeListener());
    }

    @UiThread
    void showTab() {
        mScrollableTabView.setVisibility(View.GONE);
    }

    /**
     * 创建群
     */
    private void onClickCreateGroupBtn() {
        startActivity(new Intent(this, CreateGroupActivity_.class));
    }

    /**
     * 搜索按钮
     */
    private void onClickSearchBtn() {
        Intent intent = new Intent(this, SearchChatActivity_.class);
        startActivity(intent);
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonIcon(R.drawable.actionbar_add_btn_bg_selector).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreateGroupBtn();
            }
        }).apply();
        fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
        fab.build().setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearchBtn();
            }
        }).apply();
    }

    /**
     * ViewPager换页监听器
     */
    class PageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            mScrollableTabView.updateTabView(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    protected void onDestroy() {
        EventListenerSubjectLoader.getInstance().unregisterListener(mPresenter);
        super.onDestroy();
    }

}
