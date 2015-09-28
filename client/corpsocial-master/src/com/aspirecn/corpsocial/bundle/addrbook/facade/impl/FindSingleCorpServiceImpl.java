package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.facade.FindSingleCorpService;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;

/**
 * Created by chenbin on 2015/8/10.
 * 获取用户的usercorp信息
 */
@OsgiAnnotation.OsgiService(serviceType = FindSingleCorpService.class)
public class FindSingleCorpServiceImpl implements FindSingleCorpService {

    @Override
    public UserCorp invoke(String corpId) {
        UserCorp uc = GetSelfCorpListRespBean.findByCorpId(corpId);

        return uc;
    }
}
