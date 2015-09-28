package com.aspirecn.corpsocial.bundle.common.view;

/**
 * Created by yinghuihong on 15/6/9.
 */
public interface LoginView {

    void loginSuccess();

    void loginFail(String msg);

    void getVisitAccountSuccess(String username, String password);

    void getVisitAccountFail();
}
