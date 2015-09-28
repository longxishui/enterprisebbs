package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchGroupMemberParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchGroupMemberService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据关键字和部门deptid获取企业级讨论组的用户列表
 */
@OsgiService(serviceType = SearchGroupMemberService.class)
public class SearchGroupMemberServiceImpl implements SearchGroupMemberService {

//    private DeptDao deptDao = new DeptDao();
//    private ContactDao contactDao = new ContactDao();
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public List<User> invoke(SearchGroupMemberParam params) {
//		List<Contact> contactList = new ArrayList<Contact>();
//		String deptId = deptDao.getIdByServerDeptId(Config.getInstance().getUserId(), params.getDeptId());
//
//		List<ContactBriefVO> list = contactDao.findByKeyWordInDept(Config.getInstance().getUserId(), params.getSearchKeyWord(), deptId);
//		for(ContactBriefVO item : list) {
//			contactList.add(item.toContact());
//		}
//		return contactList;

        List<User> users = new ArrayList<User>();
        try {
            //users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), key, deptId, null, 0, null,1));
//            users.addAll(dao.findByKeyWord(Config.getInstance().getUserId(), params.getSearchKeyWord(), params.getDeptId(), null, 0, null, 1, true));

        } catch (Exception ex) {
            LogUtil.e("SearchGroupMemberService查询出错", ex);
        }
        return users;
    }
}
