package com.aspirecn.corpsocial.bundle.addrbook.facade;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;

import java.util.List;

public interface FindContactsByIdsService extends IOsgiService<List<User>, List<String>> {

}
