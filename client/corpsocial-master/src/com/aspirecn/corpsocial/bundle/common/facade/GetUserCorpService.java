package com.aspirecn.corpsocial.bundle.common.facade;

import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.eventbus.IOsgiService;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.List;

/**
 * Created by Amos on 15-6-18.
 */
public interface GetUserCorpService extends IOsgiService<List<UserCorp>, Null> {
}
