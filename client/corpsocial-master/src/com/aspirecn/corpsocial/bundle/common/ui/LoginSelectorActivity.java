/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.content.Intent;

import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.EActivity;

/**
 * @author lizhuo_a
 */
@EActivity(R.layout.common_login_activity)
public class LoginSelectorActivity extends LoginActivity {

    @Override
    void doAfterView() {
        // super.doAfterView();

    }

    @Override
    void doLoginSuccess() {
        // super.doLoginSuccess();
        Intent intent = new Intent();
        intent.setClass(
                this,
                com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookMainTabActivity_.class);
        startActivity(intent);
        finish();
    }

    void startActivity(String className) {
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
            Intent intent = new Intent();
            intent.setClass(this, cls);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
