package com.aspirecn.corpsocial.bundle.common.presenter;

import com.aspirecn.corpsocial.bundle.common.listener.GetSelfCorpListRespSubject;
import com.aspirecn.corpsocial.bundle.common.listener.GetVisitAccountRespSubject;
import com.aspirecn.corpsocial.bundle.common.listener.LoginRespSubject;

/**
 * Created by yinghuihong on 15/6/9.
 */
public interface LoginPresenter extends LoginRespSubject.LoginRespEventListener,
        GetVisitAccountRespSubject.GetVisitAccountRespEventListener,
        GetSelfCorpListRespSubject.GetSelfCorpListRespListener {

    void login(String username, String password);

    void getVisitAccount(String deviceId);

    String getPassword();

    void setRememberPassword(boolean t);

}
