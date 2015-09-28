package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByLoginNamesService;
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
@OsgiService(serviceType = FindContactsByLoginNamesService.class)
public class FindContactsByLoginNamesServiceImpl implements FindContactsByLoginNamesService {

    @EventBusAnnotation.Component
    private UserDao dao;

    @Override
    public List<User> invoke(List<String> params) {

        List<User> users = new ArrayList<User>();
        try {
            users.addAll(dao.findByContactLoginNames(Config.getInstance().getUserId(), params));
        } catch (SQLException ex) {
            LogUtil.e("FindContactsByLoginNamesServiceImpl查询出错", ex);
        }

        return users;
//		List<ContactBriefVO> list = contactDao.findByContactLoginNames(Config.getInstance().getUserId(), params);
//		/*移除空数据*/
//        List<ContactBriefVO> isNull =new ArrayList<ContactBriefVO>();
//        isNull.add(null);
//        list.removeAll(isNull);
//
//        List<Contact> results = new ArrayList<Contact>();
//		for (ContactBriefVO item : list) {
//			results.add(item.toContact());
//		}
//
//		return results;
    }

}
