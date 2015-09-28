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
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordActiveEvent;
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordActiveRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordEvent;
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.ResetPasswordActiveRespSubject.RegisterActiveRespEventListener;
import com.aspirecn.corpsocial.bundle.common.listener.ResetPasswordRespSubject.ResetPasswordRespEventListener;
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
 * 重置密码：输入激活码、新密码
 *
 * @author lihaiqiang
 */
@EActivity(R.layout.common_reset_password_active_activity)
public class ResetPasswordActiveActivity extends EventFragmentActivity implements
        ResetPasswordRespEventListener, RegisterActiveRespEventListener {

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
    @ViewById(R.id.common_next_action)
    Button submitBtn;
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
    void initViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.common_register_tittle)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        mMobileNumberText.setText(mMobileNumber);
        mPasswordEdit1.setHint(R.string.reset_password_new_password);
        mPasswordEdit2.setHint(R.string.reset_password_new_password_agin);
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
                changeTime(i + "s");
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
     * 点击重新连网获取验证码
     */
    @Click(R.id.common_register_time)
    void onClicKSendBtn() {
        if (!mIsRequesting) {
            mIsRequesting = true;
            ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent();
            resetPasswordEvent.setMobilePhone(mMobileNumber);
            mDialog = ProgressDialog.show(this, "", "正在连网获取激活码...");
            doResetPassword(resetPasswordEvent);
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

        // 重置密码
        if (!mIsRequesting) {
            mIsRequesting = true;
            ResetPasswordActiveEvent resetPasswordEvent = new ResetPasswordActiveEvent();
            resetPasswordEvent.setMobilePhone(mMobileNumber);
            resetPasswordEvent.setActiveCode(activeCode);
            resetPasswordEvent.setNewPassword(mPassword);
            mDialog = ProgressDialog.show(this, "", "正在重新设置密码...");
            doResetPasswordActive(resetPasswordEvent);
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
    void doResetPassword(ResetPasswordEvent resetPasswordEvent) {
        uiEventHandleFacade.handle(resetPasswordEvent);
    }

    /**
     * 连网重置密码
     *
     * @param resetPasswordActiveEvent
     */
    @Background
    void doResetPasswordActive(ResetPasswordActiveEvent resetPasswordActiveEvent) {
        uiEventHandleFacade.handle(resetPasswordActiveEvent);
    }

    /**
     * 输入手机号码获取验证码响应方法
     */
    @Override
    public void onHandleResetPasswordRespEvent(ResetPasswordRespEvent event) {
        mDialog.dismiss();
        int respCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == respCode) {
            time();
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

    /**
     * 重置密码响应回调方法
     */
    @Override
    public void onHandleRegisterActiveRespEvent(
            ResetPasswordActiveRespEvent event) {
        mDialog.dismiss();
        int respCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == respCode) {
            // 登陆
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
