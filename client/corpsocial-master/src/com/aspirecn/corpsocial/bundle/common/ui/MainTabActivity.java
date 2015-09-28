/**
 * @(#) MainTabActivity.java Created on 2013-11-5
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */

package com.aspirecn.corpsocial.bundle.common.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddressBookUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddressBookUnReadMsgCountRespSubject.AddressBookUnReadMsgCountRespEventListener;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.domain.AppInfo;
import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.bundle.common.facade.AppStartPath;
import com.aspirecn.corpsocial.bundle.common.listener.GetCorpViewDefRespSubject.GetCorpViewDefRespListener;
import com.aspirecn.corpsocial.bundle.common.listener.QuitSubject.QuitEventListener;
import com.aspirecn.corpsocial.bundle.im.event.CalculateImUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.im.event.ImUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoadLocalChatsEvent;
import com.aspirecn.corpsocial.bundle.im.listener.ImUnReadMsgCountRespSubject.ImUnReadMsgCountRespEventListener;
import com.aspirecn.corpsocial.bundle.im.notify.MsgNotificationControllers;
import com.aspirecn.corpsocial.common.eventbus.EventActivityGroup;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.StateListUtil;
import com.aspiren.corpsocial.R;
import com.shamanland.fonticon.FontIconButton;
import com.shamanland.fonticon.FontIconDrawable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 主页面
 *
 * @author yinghuihong
 */
@EActivity(R.layout.main_tab_activity)
public class MainTabActivity extends EventActivityGroup implements GetCorpViewDefRespListener, QuitEventListener,
         AddressBookUnReadMsgCountRespEventListener,
        ImUnReadMsgCountRespEventListener {

    @ViewById(R.id.common_main_tab_th)
    TabHost mTabHost;
    @ViewById(R.id.common_main_tabs_layout)
    LinearLayout mLayoutTabs;
    @ViewById(R.id.common_main_tab_btn1_bt)
    FontIconButton mTabBtn1BT;
    @ViewById(R.id.common_main_tab_btn2_bt)
    FontIconButton mTabBtn2BT;
    @ViewById(R.id.common_main_tab_btn3_bt)
    FontIconButton mTabBtn3BT;
    @ViewById(R.id.common_main_tab_btn4_bt)
    FontIconButton mTabBtn4BT;
    @ViewById(R.id.common_main_tab_btn5_bt)
    FontIconButton mTabBtn5BT;
    @ViewById(R.id.common_main_tab_tips1_tv)
    TextView mTabTips1TV;
    @ViewById(R.id.common_main_tab_tips2_tv)
    TextView mTabTips2TV;
    @ViewById(R.id.common_main_tab_tips3_tv)
    TextView mTabTips3TV;
    @ViewById(R.id.common_main_tab_tips4_tv)
    TextView mTabTips4TV;
    @ViewById(R.id.common_main_tab_tips5_tv)
    TextView mTabTips5TV;
    @ViewById(R.id.common_main_tab_4_layout_rl)
    RelativeLayout mTabLayout4;
    @ViewById(R.id.common_main_tab_5_layout_rl)
    RelativeLayout mTabLayout5;
    /**
     * 记录被选中的菜单按钮
     */
    private int mCurrentTab = 0;
    private boolean isExit = false;
    /**
     * Tab按钮
     */
    private FontIconButton mTabBtns[] = new FontIconButton[5];
    /**
     * Tab提示
     */
    private TextView mTabTips[] = new TextView[5];
    private List<Menu> mSelectMenus;

    @AfterViews
    void doAfterViews() {

        mTabBtns[0] = mTabBtn1BT;
        mTabBtns[1] = mTabBtn2BT;
        mTabBtns[2] = mTabBtn3BT;
        mTabBtns[3] = mTabBtn4BT;
        mTabBtns[4] = mTabBtn5BT;

        mTabTips[0] = mTabTips1TV;
        mTabTips[1] = mTabTips2TV;
        mTabTips[2] = mTabTips3TV;
        mTabTips[3] = mTabTips4TV;
        mTabTips[4] = mTabTips5TV;

        initView();

        //统计通讯录未读条数
        calculateUnReadMsgCount();

        //加载本地IM消息(统计工作群未读条数)
        loadLocalChats();

    }

    /**
     * 初始化视图
     */
    private synchronized void initView() {
        mSelectMenus = obtainApps();
        mTabLayout4.setVisibility(mSelectMenus.size() >= 4 ? View.VISIBLE : View.GONE);
        mTabLayout5.setVisibility(mSelectMenus.size() >= 5 ? View.VISIBLE : View.GONE);

        mTabHost.setup(getLocalActivityManager());
        mTabHost.clearAllTabs();

        AppConfig appConfig = AppConfig.getInstance();
        mLayoutTabs.setBackgroundColor(ColorUtil.convert(appConfig.bottomViewDef.backgroundColor));
        int fontColor = ColorUtil.convert(appConfig.bottomViewDef.fontColor);
        ColorStateList colorStateList = StateListUtil.createColorStateList(0xff999999, 0xff999999, fontColor, 0xff999999);
        for (int i = 0; i < mSelectMenus.size(); i++) {
            Menu menu = mSelectMenus.get(i);
            FontIconDrawable fid = FontIconDrawable.inflate(this, R.xml.ic_main_tab);
            fid.setText(menu.fontResId);
            fid.setTextColorStateList(colorStateList);
            mTabBtns[i].setTextColor(colorStateList);
            mTabBtns[i].setText(menu.appName);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mTabBtns[i].setCompoundDrawables(null, fid, null, null);
            } else {
                mTabBtns[i].setCompoundDrawablesRelative(null, fid, null, null);
            }
            mTabHost.addTab(getTab(i, menu.cls));
        }
        // 设置按钮为选中状态
        changeButtonSelected(mCurrentTab);
    }

    @UiThread
    @Override
    public void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event) {
        initView();
    }

    /**
     * 点击Tab
     */
    @Click({R.id.common_main_tab_btn1_bt, R.id.common_main_tab_btn2_bt, R.id.common_main_tab_btn3_bt,
            R.id.common_main_tab_btn4_bt, R.id.common_main_tab_btn5_bt})
    void onClick(View v) {
        changeButtonSelected(Integer.valueOf((String) v.getTag()));
    }

    /**
     * 添加Tab页
     *
     * @param tabId
     * @param cls
     * @return
     */
    private TabSpec getTab(int tabId, String cls) {
        TabSpec spec = mTabHost.newTabSpec(String.valueOf(tabId));
        // 指定标签显示的内容 , 激活的activity对应的intent对象
        Intent intent = new Intent();
        intent.setClassName(this, cls);
        intent.putExtra("extra_navigation", true);
        spec.setContent(intent);
        // 设置标签的文字和样式
        spec.setIndicator(new View(this));
        return spec;
    }

    /**
     * 设置菜单按钮是否选中
     */
    private void changeButtonSelected(int index) {
        mTabHost.setCurrentTab(index);
        mTabBtns[mCurrentTab].setSelected(false);
        mTabBtns[index].setSelected(true);
        mCurrentTab = index;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (isExit) {
                    finish();
                } else {
                    Toast.makeText(this, "再按一次  退出程序", Toast.LENGTH_SHORT).show();
                    time();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Background
    void time() {
        isExit = true;
        SystemClock.sleep(3000);
        isExit = false;
    }

    /**
     * 被强迫退出回调方法
     */
    @UiThread
    @Override
    public void onQuitEvent(QuitEvent event) {
        Toast.makeText(this, event.getQuitInfo(), Toast.LENGTH_SHORT).show();
        //ProgressDialog dialog = ProgressDialog.show(this, "", "正在退出...");

    }

    /**
     * 清除通知栏消息
     */
    @Override
    protected void onResume() {
        super.onResume();
        MsgNotificationControllers.getInstance().clearAllNotify();
    }

    /**
     * IM加载本地消息
     */
    @Background
    void loadLocalChats() {
        LoadLocalChatsEvent loadLocalChatsEvent = new LoadLocalChatsEvent();
        loadLocalChatsEvent.setIndex(0);
        loadLocalChatsEvent.setCount(20);
        UiEventHandleFacade.getInstance().handle(loadLocalChatsEvent);
    }

    /**
     * 计算未读消息条数
     */
    @Background
    void calculateUnReadMsgCount() {
        UiEventHandleFacade.getInstance().handle(new CalculateAddressBookUnReadMsgCountEvent());
        UiEventHandleFacade.getInstance().handle(new CalculateImUnReadMsgCountEvent());
    }

    /**
     * IM 未读提示
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleIMUnReadMsgCountRespEvent(ImUnReadMsgCountRespEvent event) {
        int unReadCount = event.getUnReadCount();
        int sortNo = fetchSortNoByStartPath(AppStartPath.IM.START_PATH);
        mTabTips[sortNo - 1].setVisibility(unReadCount > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 通讯录未读提示
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleAddressBookUnReadMsgCountRespEvent(AddressBookUnReadMsgCountRespEvent event) {
        int totalCount = 0;
        totalCount += event.getUnReadInviteFriendCount();
        int sortNo = fetchSortNoByStartPath(AppStartPath.ADDRESS_BOOK.START_PATH);
        mTabTips[sortNo - 1].setVisibility(totalCount > 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取排序号
     *
     * @param startPath
     * @return
     */
    private int fetchSortNoByStartPath(String startPath) {
        int sortNo = 1;
        for (Menu menu : mSelectMenus) {
            if (menu.startPath.equals(startPath)) {
                sortNo = menu.sortNo;
                break;
            }
        }
        return sortNo;
    }

    private class Navigation {
        /**
         * 是否运行通过网络配置
         */
        boolean configurable;
        /**
         * 本地所有可选“菜单”
         */
        List<Menu> menus;
    }

    /**
     * 根据配置获取用于显示的APP
     */
    private List<Menu> obtainApps() {
        List<Menu> filterMenus = new ArrayList<Menu>();
        Navigation navigation = AssetsUtils.getInstance().read(getBaseContext(), "navigation.json", Navigation.class);
        List<Menu> defaultMenus = navigation.menus;
        List<AppInfo> appInfos = AppConfig.getInstance().bottomDef;
        if (navigation.configurable && appInfos != null && appInfos.size() != 0) {
            for (AppInfo appInfo : appInfos) {
                for (Menu app : defaultMenus) {
                    if (app.startPath.equals(appInfo.startPath)) {
                        app.sortNo = appInfo.sortNo;
                        app.appName = appInfo.name;
                        filterMenus.add(app);
                        break;
                    }
                }
            }
        } else {
            filterMenus.addAll(defaultMenus);
        }
        Collections.sort(filterMenus);
        return filterMenus;
    }
}
