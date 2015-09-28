package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.GetCorpViewDefRespSubject;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSGroup;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSGroupRespSubject.GetBBSGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSGroupDao;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.BaseBBSFragment;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.BaseBBSFragment_;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.FragmentViewPagerAdapter;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.ui.component.tabView.ScrollableTabView;
import com.aspirecn.corpsocial.common.ui.component.tabView.TabEntity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duyz
 */
@EActivity(R.layout.workgrp_main_tab_activity)
public class WorkGrpMainActivity extends EventFragmentActivity implements
        GetBBSGroupRespEventListener, ActionBarFragment.LifeCycleListener, GetCorpViewDefRespSubject.GetCorpViewDefRespListener {
    public ArrayList<BBSGroup> bbsGroups = null;
    List<TabEntity> listTabEntity = new ArrayList<TabEntity>();
    @ViewById(R.id.workgrp_title_tabid)
    ScrollableTabView tabView;
    @ViewById(R.id.workgrp_viewpager_id)
    ViewPager mViewPager;
    @ViewById(R.id.workgrp_loading_viewid)
    RelativeLayout loadingView;
    @ViewById(R.id.actionbar_right_btn)
    ImageButton btnRight;
    @ViewById(R.id.workgrp_main_tab_noitem_ll)
    LinearLayout mLL_noGroup;
    /**
     * viewPager的适配器
     */
//	private FragmentViewPagerAdapter adapter;
    private String groupId = null;
    private String corpId = Config.getInstance().getCorpId();
    private BBSGroupDao groupDao = null;
    private int REQUEST_NEW_BBS = 3;
    private boolean isCreate = false;
    @Click(R.id.tv_sliding_panel)
    void doSlidingPanel() {
    }

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().navigation(true).
                title(getString(R.string.common_main_tab_colleagues)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
        groupDao = new BBSGroupDao();
        initWithLocalData();
    }

    @Background
    void doGetBBSGroup() {
        GetBBSGroupEvent getBBSGroupEvent = new GetBBSGroupEvent(corpId);
        uiEventHandleFacade.handle(getBBSGroupEvent);
    }

    @UiThread
    @Override
    public void onHandleGetBBSGroupRespEvent(
            GetBBSGroupRespEvent getBBSGroupRespEvent) {
        if (ErrorCode.SUCCESS.getValue() == getBBSGroupRespEvent
                .getErrcode()) {
            if (getBBSGroupRespEvent.isChange()) {
                initWithLocalData();
            } else {
                return;
            }
        }
        if (getBBSGroupRespEvent.getBbsGroups() == null || getBBSGroupRespEvent.getBbsGroups().size() == 0) {
            mLL_noGroup.setVisibility(View.VISIBLE);
        }
        if (loadingView.getVisibility() == View.VISIBLE) {
            loadingView.setVisibility(View.GONE);
        }
    }

    private void initWithLocalData() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        listTabEntity.clear();
        bbsGroups = groupDao.findAllGroups(corpId);
        boolean flag = true;
        if (bbsGroups.size() > 0) {
            mLL_noGroup.setVisibility(View.GONE);
            for (final BBSGroup mGroup : bbsGroups) {
                Bundle arguments = new Bundle();
                arguments.putString("groupid", mGroup.getBbsGroupEntity().getId());
                if (flag) {
                    arguments.putBoolean("isFirstTab", true);
                    flag = false;
                } else {
                    arguments.putBoolean("isFirstTab", false);
                }
                BaseBBSFragment bbsFragment = new BaseBBSFragment_();
                bbsFragment.setArguments(arguments);
                fragments.add(bbsFragment);
                TabEntity tabEntity = new TabEntity();
                tabEntity.setName(mGroup.getBbsGroupEntity().getName());
                tabEntity.setNotifyNum(0);
                listTabEntity.add(tabEntity);
            }
            groupId = bbsGroups.get(0).getBbsGroupEntity().getId();
            FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mViewPager, fragments);
            fragmentViewPagerAdapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
                @Override
                public void onExtraPageSelected(int position) {
                    tabView.updateTabView(position);
                    groupId = bbsGroups.get(position).getBbsGroupEntity().getId();
                    super.onExtraPageSelected(position);
                }
            });
            tabView.init(this, new ScrollableTabView.OntabChangeListener() {
                @Override
                public void notifyTabChange(int position) {
                    mViewPager.setCurrentItem(position, true);
                }
            }, listTabEntity);
        }
        if (bbsGroups != null && bbsGroups.size() > 0&&!isCreate) {
            doGetBBSGroup();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if ((bbsGroups == null || bbsGroups.size() == 0)) {
            if (!(loadingView.getVisibility() == View.VISIBLE)) {
                loadingView.setVisibility(View.VISIBLE);
                isCreate = true;
                doGetBBSGroup();
            }
        }
        super.onResume();
    }

    @UiThread
    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonIcon(R.drawable.workgrp_bbs_new_btn)
                .setSecondButtonIcon(R.drawable.workgrp_bbs_concernme_btn)
                .apply();

        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupId == null) {
                    Toast.makeText(WorkGrpMainActivity.this, "暂无频道", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent mIntent = new Intent(WorkGrpMainActivity.this, com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpNewBBSActivity_.class);
                mIntent.putExtra("groupid", groupId);
                startActivityForResult(mIntent, REQUEST_NEW_BBS);

            }
        }).setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(WorkGrpMainActivity.this, com.aspirecn.corpsocial.bundle.workgrp.ui.ConcernMeBBSListActivity_.class);
                startActivity(mIntent);
            }
        }).apply();
    }

    @UiThread
    @Override
    public void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event) {
        tabView.updateTabColor(event.fontColor);
    }
}