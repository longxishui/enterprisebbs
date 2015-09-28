package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 */
public interface FindCorpService extends IOsgiService<List<UserCorp>, Null> {
}
