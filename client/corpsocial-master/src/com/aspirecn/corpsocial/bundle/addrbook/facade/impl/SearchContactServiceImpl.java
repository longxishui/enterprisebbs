package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchContactParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchContactService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据关键字获取
 */
@OsgiService(serviceType = SearchContactService.class)
public class SearchContactServiceImpl implements SearchContactService {

    //private ContactDao dao = new ContactDao();
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public List<User> invoke(SearchContactParam param) {
//		List<ContactBriefVO> list = dao.findByKeyWord(Config.getInstance().getUserId(), searchKeyWord);
//
//		List<Contact> result = new ArrayList<Contact>();
//		for(ContactBriefVO item : list) {
//			result.add(item.toContact());
//		}
//
//		return result;

        List<User> users = new ArrayList<User>();
        try {
            users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), param.getKey(), param.getDeptId(), param.getCorpId(), -1, null, 0));

        } catch (SQLException ex) {
            LogUtil.e("SearchContactServiceImpl出错", ex);
        }
        return users;
    }

}
