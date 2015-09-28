/**
 * @(#) AddressBook_MainTabActivity.java Created on 2013-11-5
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.common.ui.JumpActivity_;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;

import java.util.List;

/**
 * 联系人模块
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.addrbook_main_tab_activity)
public class ShareAddrbookMainTabActivity extends AddrbookMainTabActivity {

    public Bundle bundle;

    @AfterInject
    void doAfterInject() {
        Intent intent = getIntent();
        bundle = intent.getExtras();
        JumpActivity_.addActivity(this);
    }

    @Override
    protected void startSearchActivity() {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(this, ShareAddrbookSearchActivity_.class);
        startActivity(intent);
    }


    @Override
    public List<Tab> initTabs() {
        Tabs t = AssetsUtils.getInstance().read(this, "addressbook_share.json", Tabs.class);
        return t.tabs;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JumpActivity_.finishActivity(this.getClass().getName());
    }

}
