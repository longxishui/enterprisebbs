package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.UpdateContactService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;

/**
 * 更新用户
 */
@OsgiAnnotation.OsgiService(serviceType = UpdateContactService.class)
public class UpdateContactServiceImpl implements UpdateContactService {

    //private ContactDao contactDao = new ContactDao();
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null invoke(User params) {
//        ContactEntity entity = contactDao.findByUserId(params.getId());
//        entity.setHeadImageUrl(params.getHeadImgUrl());
//        entity.setSignature(params.getSignature());
//        contactDao.update(entity);
        dao.updateContact(params);
        return null;
    }


}
