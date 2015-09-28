package com.aspirecn.corpsocial.bundle.addrbook.facade;


import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 修改联系人服务
 *
 * @author lizhuo_a
 */
public interface UpdateContactService extends IOsgiService<Null, User> {

}
