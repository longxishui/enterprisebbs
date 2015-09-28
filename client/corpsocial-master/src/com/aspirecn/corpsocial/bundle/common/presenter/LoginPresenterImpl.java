package com.aspirecn.corpsocial.bundle.common.presenter;

import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetVisitAccountEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetVisitAccountRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.LoginEvent;
import com.aspirecn.corpsocial.bundle.common.event.LoginRespEvent;
import com.aspirecn.corpsocial.bundle.common.view.LoginView;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.SysInfo;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.gotye.api.GotyeAPI;

/**
 * Created by yinghuihong on 15/6/9.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView view;

    public LoginPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public void login(String username, String password) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setUsername(username);
        loginEvent.setPassword(password);

        SysInfo sysInfo = SysInfo.getInstance();
        loginEvent.setVersion(sysInfo.getVersionName());
        loginEvent.setPlateform(sysInfo.getOsPlatform());

        UiEventHandleFacade.getInstance().handle(loginEvent);
    }

    @Override
    public void onHandleLoginRespEvent(LoginRespEvent event) {
        if (event.getRespCode() == ErrorCode.SUCCESS.getValue()) {
            view.loginSuccess();
        } else {
            view.loginFail(event.getMessage().toString());
        }
    }

    @Override
    public void getVisitAccount(String deviceId) {
        GetVisitAccountEvent getVisitAccountEvent = new GetVisitAccountEvent();
        getVisitAccountEvent.setDeviceToken(deviceId);
        UiEventHandleFacade.getInstance().handle(getVisitAccountEvent);
    }

    @Override
    public void onHandleGetVisitAccountRespEvent(GetVisitAccountRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {

            view.getVisitAccountSuccess(event.getUserName(), event.getPassword());

            // 保存
            Config config = Config.getInstance();
            config.setUserName(event.getUserName());
            config.setPassword(event.getPassword());

            // 自动登录
            login(event.getUserName(), event.getPassword());

        } else {
            view.getVisitAccountFail();
        }
    }


    @Override
    public String getPassword() {
        return Config.getInstance().getPassword();
    }

    @Override
    public void setRememberPassword(boolean t) {
        Config.getInstance().setRememberPassword(t);
    }

    @Override
    public void onGetSelfCorpListRespEvent(GetSelfCorpListRespEvent event) {

    }
}
