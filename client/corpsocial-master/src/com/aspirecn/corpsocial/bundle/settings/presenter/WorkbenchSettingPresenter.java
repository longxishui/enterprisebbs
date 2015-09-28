package com.aspirecn.corpsocial.bundle.settings.presenter;

import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshAddrBookRespSubject;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyHeadImgRespSubject;

/**
 * Created by chenziqiang on 15-8-24.
 */
public interface WorkbenchSettingPresenter extends
        ModifyHeadImgRespSubject.ModifyHeadPortraitRespEventListener,
        RefreshAddrBookRespSubject.RefreshAddrbookRespEventListener {
    /**
     * 初始化
     */
    void doAfterViews();

    void updateVersion();
}
