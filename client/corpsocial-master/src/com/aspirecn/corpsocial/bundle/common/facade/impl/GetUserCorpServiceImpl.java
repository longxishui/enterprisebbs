package com.aspirecn.corpsocial.bundle.common.facade.impl;

import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.facade.GetUserCorpService;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.List;

/**
 * Created by Amos on 15-6-18.
 */
@OsgiAnnotation.OsgiService(serviceType = GetUserCorpService.class)
public class GetUserCorpServiceImpl implements GetUserCorpService {

    @Override
    public List<UserCorp> invoke(Null param) {
            return GetSelfCorpListRespBean.find(Config.getInstance().getUserId());
    }
}
