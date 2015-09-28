package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.facade.FindCorpService;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 * 获取用户的企业列表信息
 */
@OsgiAnnotation.OsgiService(serviceType = FindCorpService.class)
public class FindCorpServiceImpl implements FindCorpService {
    public List<UserCorp> invoke(Null n) {
        List<UserCorp> userCorps = new ArrayList<>();
        try {
            LogUtil.i("aspire----"+Config.getInstance().getUserId());
            userCorps.addAll(GetSelfCorpListRespBean.find(Config.getInstance().getUserId()));
        } catch (Exception ex) {
            LogUtil.e("FindCorpServiceImpl查询出错", ex);
        }
        return userCorps;
    }
}
