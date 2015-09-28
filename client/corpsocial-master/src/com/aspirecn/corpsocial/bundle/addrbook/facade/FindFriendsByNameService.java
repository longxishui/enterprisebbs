package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;

import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 */
public interface FindFriendsByNameService extends IOsgiService<List<User>, String> {
}
