/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.presenter.LoginPresenter;
import com.aspirecn.corpsocial.bundle.common.presenter.LoginPresenterImpl;
import com.aspirecn.corpsocial.bundle.common.service.GotyeService;
import com.aspirecn.corpsocial.bundle.common.view.LoginView;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.StringUtils;
import com.aspiren.corpsocial.R;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shamanland.fonticon.FontIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Method;

/**
 * @author lizhuo_a
 */
@EActivity(R.layout.common_login_activity)
public class LoginActivity extends EventActivity implements LoginView {

    @ViewById(R.id.login_user_edit)
    EditText mtxtUserName;
    @ViewById(R.id.login_passwd_edit)
    EditText mtxtPassword;
    @ViewById(R.id.login_user_delete_btn)
    ImageButton mUserDeleteBtn;
    @ViewById(R.id.login_password_delete_btn)
    ImageButton mPasswordDeleteBtn;
    @ViewById(R.id.login_checkBox)
    CheckBox mRememberPasswordCb;
    @ViewById(R.id.login_numboard_gridview)
    GridView mNumkeyboardGridView;
    @ViewById(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @ViewById(R.id.login_login_btn)
    Button loginbtn;
    @ViewById(R.id.login_numkeyboard)
    Button login_numkey;
    @ViewById(R.id.login_logo)
    ImageView loginView;
    @ViewById(R.id.font_logo)
    FontIconView fontLogo;
    @ViewById(R.id.login_name)
    TextView logoName;
    @ViewById(R.id.login_reset_password)
    Button rePassWordBtn;
    @ViewById(R.id.login_button_register)
    Button registerBtn;
    private ProgressDialog dialog;
    private boolean isGettingVisitor;
    private boolean isRegisterSuccess = false;
    private LoginPresenter presenter = new LoginPresenterImpl(this);

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        EventListenerSubjectLoader.getInstance().registerListener(presenter);
    }

    @AfterViews
    void doAfterView() {
         GotyeAPI.getInstance().init(AspirecnCorpSocial.getContext(),Constant.QINJIA_APPKEY);

        AppConfig appConfig = AppConfig.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.app_icon)
                .showImageOnFail(R.drawable.app_icon)
                .build();
        ImageLoader.getInstance().displayImage(appConfig.preIcon, loginView, options);

//        fontLogo.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        logoName.setText(appConfig.appName);
        rePassWordBtn.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        registerBtn.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        loginbtn.setBackgroundColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mRememberPasswordCb.setChecked(true);

        mNumkeyboardGridView.setVisibility(View.GONE);

        mNumkeyboardGridView.setAdapter(new NumboardGridViewAdapter(
                LoginActivity.this));

        mNumkeyboardGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        mNumkeyboardGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 9) {
                    deleteText(mtxtUserName);
                } else if (arg2 == 11) {
                    mNumkeyboardGridView.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    loginbtn.setVisibility(View.VISIBLE);
                } else if (arg2 == 10) {
                    insertText(mtxtUserName, String.valueOf(0));
                } else {
                    insertText(mtxtUserName, String.valueOf(arg2 + 1));
                }
            }
        });

        mtxtUserName.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    //    mtxtUserName.setInputType(InputType.TYPE_NULL);
                    mNumkeyboardGridView.setVisibility(View.VISIBLE);

                    if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下 danielinbiti
                        mtxtUserName.setInputType(InputType.TYPE_NULL);
                    } else {
                        Class<EditText> cls = EditText.class;
                        Method method;
                        try {
                            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                            method.setAccessible(true);
                            method.invoke(mtxtUserName, false);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        try {
                            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                            method.setAccessible(true);
                            method.invoke(mtxtUserName, false);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }


                } else {
                    mNumkeyboardGridView.setVisibility(View.GONE);
                }
            }
        });

        mtxtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (!arg1) {

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(
                            mtxtPassword.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        // 判断是否注册或重设密码成功进行处理
        isRegisterSuccess = getIntent().getBooleanExtra("success", false);
        if (isRegisterSuccess) {
            mtxtUserName.setText(getIntent().getStringExtra("userName"));
            mtxtPassword.setText(getIntent().getStringExtra("password"));
            return;
        }

        Config config = Config.getInstance();
        mtxtUserName.setText(config.getUserName());
        int state = GotyeAPI.getInstance().isOnline();
        if (state == GotyeUser.NETSTATE_ONLINE&&GotyeAPI.getInstance().getLoginUser()!=null) {
            //已经登陆或离线可以直接跳到主界面
            doLoginSuccess();
            //启动service保存service长期活动
            Intent toService = new Intent(this, GotyeService.class);
            startService(toService);
        }
//        if (config.getAccountStatus() || config.getLoginStatus()) {
//            mtxtPassword.setText(config.getPassword());
//
//            doLogin(config.getUserName(), config.getPassword());
//
//            doLoginSuccess();
//        } else {
//            if (config.isRememberPassword()) {
//                mtxtPassword.setText(config.getPassword());
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRegisterSuccess) {
            doLoginButtonClicked();
        }
    }

    /**
     * 用户框文本变化监听
     *
     * @param text
     */
    @TextChange(R.id.login_user_edit)
    void onUserTextChanged(CharSequence text) {
        if (text.toString().length() > 0) {
            mUserDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mUserDeleteBtn.setVisibility(View.GONE);
        }
        mtxtPassword.setText("");
    }

    /**
     * 用户框文本变化监听
     *
     * @param text
     */
    @TextChange(R.id.login_passwd_edit)
    void onPasswordTextChanged(CharSequence text) {
        if (text.toString().length() > 0) {
            mPasswordDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mPasswordDeleteBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 点击用户框删除按钮
     */
    @Click(R.id.login_user_delete_btn)
    void onClickUserDeleteBtn() {
        mtxtUserName.setText("");
    }

    /**
     * 点击密码框删除按钮
     */
    @Click(R.id.login_password_delete_btn)
    void onClickPasswordDeleteBtn() {
        mtxtPassword.setText("");
    }

    // 点击跳转忘记密码页面
    @Click({R.id.login_reset_password})
    void doResetpassword() {
        startActivity(new Intent(this, ResetPasswordActivity_.class));
    }

    // 点击跳转注册页面
    @Click({R.id.login_button_register})
    void doRegister() {
        startActivity(new Intent(this, RegisterActivity_.class));
    }

    // 点击跳转注册页面
    @Click({R.id.login_visit_btn})
    void doVisitRegister() {
        doGetVisitAccount();
    }

    // 登录按钮点击事件
    @Click({R.id.login_login_btn})
    void doLoginButtonClicked() {
        String userName = mtxtUserName.getText().toString().trim();
        String password = mtxtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName))
            showToast("请输入手机号码");
            //else if (!StringUtils.isMobileNumber(userName))
        else if (userName.length() != 11)
            showToast("请输入正确的手机号码");
        else if (TextUtils.isEmpty(password))
            showToast("请输入密码");
        else {
            Config config = Config.getInstance();
            config.setUserName(userName);
            config.setPassword(password);

            dialog = ProgressDialog.show(this, "", "登录中...");
            doLogin(userName, password);
        }
    }

    /**
     * 登录成功后的处理
     */
    @UiThread
    void doLoginSuccess() {
        presenter.setRememberPassword(mRememberPasswordCb.isChecked());
        startActivity(new Intent(this, MainTabActivity_.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        EventListenerSubjectLoader.getInstance().unregisterListener(presenter);
        super.onDestroy();
        String password = mtxtPassword.getText().toString().trim();
        if (!password.equals(presenter.getPassword())) {
            presenter.setRememberPassword(false);
        }
    }

    /**
     * 获取EditText光标所在的位置
     */
    private int getEditTextCursorIndex(EditText mEditText) {
        return mEditText.getSelectionStart();
    }

    /**
     * 向EditText指定光标位置插入字符串
     */
    private void insertText(EditText mEditText, String mText) {
        mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
    }

    /**
     * 向EditText指定光标位置删除字符串
     */
    private void deleteText(EditText mEditText) {
        if (!StringUtils.isEmpty(mEditText.getText().toString())) {
            mEditText.getText().delete(getEditTextCursorIndex(mEditText) - 1, getEditTextCursorIndex(mEditText));
        }
    }

    /**
     * 登陆方法
     */
    @Background
    void doLogin(String username, String password) {
        presenter.login(username, password);
    }

    @Override
    public void loginSuccess() {
        if (dialog != null) {
            dialog.dismiss();
        }
        doLoginSuccess();
    }

    @Override
    public void loginFail(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        showToast(msg);
    }

    @Background
    void doGetVisitAccount() {
        if (isGettingVisitor) {
            return;
        }
        isGettingVisitor = true;
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        presenter.getVisitAccount(tm.getDeviceId());
    }

    @UiThread
    @Override
    public void getVisitAccountSuccess(String username, String password) {
        isGettingVisitor = false;
        mtxtUserName.setText(username);
        mtxtPassword.setText(password);
        dialog = ProgressDialog.show(this, "", "登录中...");
    }

    @Override
    public void getVisitAccountFail() {
        isGettingVisitor = false;
        showToast("网络无法连接 请稍后再试");
    }

    @UiThread
    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
