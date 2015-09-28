package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;

import java.util.List;

/**
 * 根据条件查询联系人服务
 *
 * @author lizhuo_a
 */
public interface SearchContactService extends IOsgiService<List<User>, SearchContactParam> {

}
