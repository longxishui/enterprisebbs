package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;

/**
 * 根据userid获取用户详细信息
 */
@OsgiService(serviceType = FindContactService.class)
public class FindContactServiceImpl implements FindContactService {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public User invoke(String contactId) {
//        ContactEntity contactEntity = contactDao.findByUserId(contactId);
//        if (contactEntity != null) {
//            return contactEntity.toContact();
//        } else {
//            RefreshAddrbookEvent refreshAddrbookEvent = new RefreshAddrbookEvent();
//            refreshAddrbookEvent.setUserId(contactId);
//            refreshAddrbookEvent.setRefreshUpdateTime(false);
//            UiEventHandleFacade.getInstance().handle(refreshAddrbookEvent);
//        }
//
//        return null;
        User user = dao.findUserDetail(contactId);
        return user;
    }
}
