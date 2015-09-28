package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;

import java.util.List;

/**
 * @author lizhuo_a
 */
public interface SearchGroupMemberService extends IOsgiService<List<User>, SearchGroupMemberParam> {

}
