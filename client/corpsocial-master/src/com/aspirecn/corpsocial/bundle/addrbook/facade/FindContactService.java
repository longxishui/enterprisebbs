package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;

/**
 * 根据ID获取单个联系人服务
 *
 * @author lizhuo_a
 */
public interface FindContactService extends IOsgiService<User, String> {

}
