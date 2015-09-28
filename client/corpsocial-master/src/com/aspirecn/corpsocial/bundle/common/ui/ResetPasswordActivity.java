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
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordEvent;
import com.aspirecn.corpsocial.bundle.common.event.ResetPasswordRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.ResetPasswordRespSubject.ResetPasswordRespEventListener;
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
 * 重置密码：输入手机号码获取激活码
 *
 * @author lihaiqiang
 */
@EActivity(R.layout.common_reset_password_activity)
public class ResetPasswordActivity extends EventFragmentActivity implements
        ResetPasswordRespEventListener {

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
                title(getString(R.string.reset_password_title1)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
    }

    /**
     * 点击下一步按钮方法
     */
    @Click(R.id.register_next_action)
    void onClickNextBtn() {
        mMobileNumber = mMobileNumberEdit.getText().toString().trim();

        // 判断手机号是否有效
        if (TextUtils.isEmpty(mMobileNumber)) {
            // 提示：未填手机号
            Toast.makeText(this,
                    getString(R.string.reset_password_phone_number_empty_tips),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobileNumber(mMobileNumber)) {
            // 提示：手机号不正确
            Toast.makeText(this,
                    getString(R.string.reset_password_phone_number_error_tips),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 重置密码
        if (!mIsRequesting) {
            mIsRequesting = true;
            ResetPasswordEvent resetPasswordEvent = new ResetPasswordEvent();
            resetPasswordEvent.setMobilePhone(mMobileNumber);
            mDialog = ProgressDialog.show(this, "", "正在连网获取激活码...");
            doResetPassword(resetPasswordEvent);
        }

        // TODO////////////////////////////测试////////////////////////
        // nextPage();
    }

    /**
     * 输入手机号码获取验证码
     *
     * @param resetPasswordEvent
     */
    @Background
    void doResetPassword(ResetPasswordEvent resetPasswordEvent) {
        uiEventHandleFacade.handle(resetPasswordEvent);
    }

    /**
     * 获取验证码响应回调方法
     */
    @Override
    public void onHandleResetPasswordRespEvent(ResetPasswordRespEvent event) {
        mDialog.dismiss();
        int respCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == respCode) {
            if (mIsRequesting)
                nextPage();
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
     * 网络请求失败提示
     *
     * @param errorMessage
     */
    @UiThread
    void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * 跳到下一页
     */
    private void nextPage() {
        Intent intent = new Intent(this, ResetPasswordActiveActivity_.class);
        intent.putExtra("mobileNumber", mMobileNumber);
        startActivity(intent);
    }

}
