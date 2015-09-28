/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.event.RegisterActiveEvent;
import com.aspirecn.corpsocial.bundle.common.event.RegisterActiveRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.RegisterEvent;
import com.aspirecn.corpsocial.bundle.common.event.RegisterRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.RegisterActiveRespSubject.RegisterActiveRespEventListener;
import com.aspirecn.corpsocial.bundle.common.listener.RegisterRespSubject.RegisterRespEventListener;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 注册账号：输入验证码、密码设置账号密码
 *
 * @author chenziqiang
 */
@EActivity(R.layout.common_register_active_activity)
public class RegisterActiveActivity extends EventFragmentActivity implements
        RegisterActiveRespEventListener, RegisterRespEventListener {


    @ViewById(R.id.common_next_action)
    Button submitBtn;
    // 显示手机号码
    @ViewById(R.id.common_regiter_phone)
    TextView mMobileNumberText;
    // 激活码输入框
    @ViewById(R.id.common_register_key)
    EditText mActiveCodeEdit;
    // 发送获取激活码按钮
    @ViewById(R.id.common_register_time)
    Button mSendBtn;
    // 密码输入框
    @ViewById(R.id.common_register_password)
    EditText mPasswordEdit1;
    // 确认密码输入框
    @ViewById(R.id.common_register_password_repeat)
    EditText mPasswordEdit2;
    private ProgressDialog mDialog;
    private boolean mIsRequesting = false;
    /**
     * 手机号码
     */
    private String mMobileNumber;
    /**
     * 密码
     */
    private String mPassword;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        mMobileNumber = getIntent().getStringExtra("mobileNumber");
    }

    @AfterViews
    void doAfterViews() {
        mMobileNumberText.setText(mMobileNumber);
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.common_register_tittle)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        time();
        submitBtn.setBackgroundColor(Color.parseColor(AppConfig.getInstance().topViewDef.backgroundColor));
    }

    /**
     * 计时
     */
    @Background
    void time() {
        mSendBtn.setClickable(false);
        for (int i = 60; i >= 0; i--) {
            if (i == 0) {
                changeTime("重新发送");
                mSendBtn.setClickable(true);
            } else {
                changeTime(i + "秒");
            }
            SystemClock.sleep(1000);
        }
    }

    /**
     * 改变UI显示时间
     *
     * @param time
     */
    @UiThread
    void changeTime(String time) {
        mSendBtn.setText(time);
    }

    /**
     * 点击回退按钮方法
     */
    @Click(R.id.actionbar_back_btn)
    void onClickBackBtn() {
        finish();
    }

    /**
     * 点击重新获取验证码
     */
    @Click(R.id.common_register_time)
    void onClicKSendBtn() {
        // 注册
        if (!mIsRequesting) {
            mIsRequesting = true;
            RegisterEvent registerEvent = new RegisterEvent();
            registerEvent.setMobilePhone(mMobileNumber);
            mDialog = ProgressDialog.show(this, "", "正在连网获取激活码...");
            doRegister(registerEvent);
        }
    }

    /**
     * 点击下一步按钮方法
     */
    @Click(R.id.common_next_action)
    void onClickNextBtn() {
        // 判断是否输入激活码
        String activeCode = mActiveCodeEdit.getText().toString().trim();
        if (TextUtils.isEmpty(activeCode)) {
            Toast.makeText(this, "请输入激活码!", Toast.LENGTH_LONG).show();
            return;
        }

        // 判断是否输入密码
        mPassword = mPasswordEdit1.getText().toString().trim();
        if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(this, "请输入密码!", Toast.LENGTH_LONG).show();
            return;
        }

        String password2 = mPasswordEdit2.getText().toString().trim();

        // 判断两次输入密码是否相同
        if (!mPassword.equals(password2)) {
            Toast.makeText(this, "两次输入密码不一致，请重新输入!", Toast.LENGTH_LONG).show();
            return;
        }

        // 设置密码
        if (!mIsRequesting) {
            mIsRequesting = true;
            RegisterActiveEvent registerActiveEvent = new RegisterActiveEvent();
            registerActiveEvent.setMobilePhone(mMobileNumber);
            registerActiveEvent.setActiveCode(activeCode);
            registerActiveEvent.setPassword(mPassword);
            mDialog = ProgressDialog.show(this, "", "正在设置密码...");
            doRegisterActive(registerActiveEvent);
        }

        // ////////////////////////////////测试///////////////////////////////
        // nextPage();
    }

    /**
     * 连网获取验证码
     *
     * @param resetPasswordEvent
     */
    @Background
    void doRegister(RegisterEvent registerEvent) {
        uiEventHandleFacade.handle(registerEvent);
    }

    /**
     * 连网设置密码
     *
     * @param resetPasswordActiveEvent
     */
    @Background
    void doRegisterActive(RegisterActiveEvent registerActiveEvent) {
        uiEventHandleFacade.handle(registerActiveEvent);
    }

    /**
     * 注册响应回调方法
     */
    @Override
    public void onHandleRegisterRespEvent(RegisterRespEvent event) {
        mDialog.dismiss();
        switch (event.getErrorCode()) {
            case 0:/* ErrorCode.SUCCESS.getValue() */
                time();
                break;
            case 16:/* ErrorCode.NO_NETWORK.getValue() */
                showErrorMessage(event.getMessage() + "(无网络！)");
                break;
            case 9:
            /* ErrorCode.TIMEOUT.getValue() */
                showErrorMessage(event.getMessage() + "(网络不给力！)");
                break;
            case 65535:/* ErrorCode.OTHER_ERROR.getValue() */
                showErrorMessage(event.getMessage() + "(网络出异常！)");
                break;
            default:
                showErrorMessage(event.getMessage());
                break;
        }
        mIsRequesting = false;
    }

    /**
     * 设置密码响应回调方法
     */
    @Override
    public void onHandleRegisterActiveRespEvent(RegisterActiveRespEvent event) {
        mDialog.dismiss();
        int respCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == respCode) {
            login();
        } else if (ErrorCode.NO_NETWORK.getValue() == respCode) {
            showErrorMessage(event.getMessage() + "(无网络！)");
        } else if (ErrorCode.TIMEOUT.getValue() == respCode) {
            showErrorMessage(event.getMessage() + "(网络不给力！)");
        } else if (ErrorCode.OTHER_ERROR.getValue() == respCode) {
            showErrorMessage(event.getMessage() + "(网络出异常！)");
        } else {
            showErrorMessage(event.getMessage());
        }
        mIsRequesting = false;
    }

    @UiThread
    void login() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity_.class);
        intent.putExtra("success", true);
        intent.putExtra("userName", mMobileNumber);
        intent.putExtra("password", mPassword);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 网络请求失败提示
     *
     * @param errorMessage
     */
    @UiThread
    void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

}
