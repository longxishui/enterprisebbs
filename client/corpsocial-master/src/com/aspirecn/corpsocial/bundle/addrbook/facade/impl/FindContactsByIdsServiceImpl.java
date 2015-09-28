package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.Contact;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据联系人的ID列表，查询联系人信息
 *
 * @author meixuesong
 */
@OsgiService(serviceType = FindContactsByIdsService.class)
public class FindContactsByIdsServiceImpl implements FindContactsByIdsService {

    @EventBusAnnotation.Component
    private UserDao dao ;

    /**
     * params user id 列表
     *
     * @param params
     * @return
     */
    @Override
    public List<User> invoke(List<String> params) {
        List<User> results = new ArrayList<User>();
        try {
            List<User> list = dao.findFilterByContactIds(Config.getInstance().getUserId(), params);
            results.addAll(list);
        /*移除空数据*/
//            List<User> isNull =new ArrayList<User>();
//            isNull.add(null);
//            list.removeAll(isNull);
//            for (User item : list) {
//                results.add(toContact(item));
//            }
        } catch (SQLException e) {
            LogUtil.e("FindContactsByIdsServiceImpl查询出错:", e);
        }

        return results;
    }

    private Contact toContact(User user) {
        Contact contact = new Contact();
        contact.setDeptName(user.getDeptName());
        contact.setId(user.getUserid());
        contact.setName(user.getName());
        contact.setImStatus(user.getImStatus());
        contact.setHeadImgUrl(user.getUrl());
        contact.setInitialCode(user.getInitialKey());
        contact.setSignature(user.getSignature());
        contact.setMobilePhone(user.getInnerPhone());
        contact.setLoginName(user.getLoginName());
        contact.setUserType(user.getUserType());

        return contact;
    }

}
