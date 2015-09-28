/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.event.RegisterEvent;
import com.aspirecn.corpsocial.bundle.common.event.RegisterRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.RegisterRespSubject.RegisterRespEventListener;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.util.StringUtils;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 注册账号：输入手机号获取验证码
 *
 * @author chenziqiang
 */
@EActivity(R.layout.common_register_activity)
public class RegisterActivity extends EventFragmentActivity implements
        RegisterRespEventListener {

    // 手机号码输入框
    @ViewById(R.id.register_phone)
    EditText mMobileNumberEdit;
    @ViewById(R.id.register_name)
    TextView logoName;
    @ViewById(R.id.register_next_action)
    Button submitBtn;
    @ViewById(R.id.login_logo)
    ImageView loginView;
//    @ViewById(R.id.register_logo)
//    FontIconView loginView;
    private ProgressDialog mDialog;
    private boolean mIsRequesting = false;
    private String mMobileNumber;

    @AfterViews
    void initViews() {
        AppConfig appConfig = AppConfig.getInstance();
        if (appConfig != null) {
//            loginView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .showImageForEmptyUri(R.drawable.app_icon)
                    .showImageOnFail(R.drawable.app_icon)
                    .build();
            ImageLoader.getInstance().displayImage(appConfig.preIcon, loginView, options);
            if (!StringUtils.isEmpty(appConfig.appName))
                logoName.setText(appConfig.appName);
            submitBtn.setBackgroundColor(Color.parseColor(appConfig.topViewDef.backgroundColor));
        }

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.register_phone)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
    }

    /**
     * 返回按钮方法
     */
    @Click(R.id.actionbar_back_btn)
    void onClickBackBtn() {
        finish();
    }

    /**
     * 点击下一步按钮方法
     */
    @Click(R.id.register_next_action)
    void onClickNextBtn() {
        mMobileNumber = mMobileNumberEdit.getText().toString().trim();
        // 未填手机号
        if (TextUtils.isEmpty(mMobileNumber)) {
            showShortToast("请输入手机号码");
            return;
        }
        //手机号不正确
        //if (!StringUtils.isMobileNumber(mMobileNumber)) {
        if (mMobileNumber.length() != 11) {
            showShortToast("请输入正确的手机号码");
            return;
        }
        doRequest();
        // TODO////////////////////////////测试用////////////////////////
        // nextPage();
    }

    /**
     * 封装注册请求对象并执行连网注册
     */
    private void doRequest() {
        if (!mIsRequesting) {
            mIsRequesting = true;
            RegisterEvent registerEvent = new RegisterEvent();
            registerEvent.setMobilePhone(mMobileNumber);
            mDialog = ProgressDialog.show(this, "", "正在连网获取激活码...");
            mDialog.setCancelable(true);
            doRegister(registerEvent);
        }
    }

    /**
     * 连网获取验证码
     */
    @Background
    void doRegister(RegisterEvent registerEvent) {
        uiEventHandleFacade.handle(registerEvent);
    }

    /**
     * 连网响应方法
     */
    @Override
    public void onHandleRegisterRespEvent(RegisterRespEvent event) {
        mDialog.dismiss();
        int respCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == respCode) {
            if (mIsRequesting)
                nextPage();
        } else if (ErrorCode.NO_NETWORK.getValue() == respCode) {
            showLongToast(event.getMessage() + "(无网络！)");
        } else if (ErrorCode.TIMEOUT.getValue() == respCode) {
            showLongToast(event.getMessage() + "(网络不给力！)");
        } else if (ErrorCode.OTHER_ERROR.getValue() == respCode) {
            showLongToast(event.getMessage() + "(网络出异常！)");
        } else {
            showLongToast(event.getMessage());
        }
        mIsRequesting = false;
    }

    /**
     * 网络请求失败提示
     *
     * @param msg
     */
    @UiThread
    void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @UiThread
    void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳到下一页
     */
    private void nextPage() {
        Intent intent = new Intent(this, RegisterActiveActivity_.class);
        intent.putExtra("mobileNumber", mMobileNumber);
        startActivity(intent);
    }

}
