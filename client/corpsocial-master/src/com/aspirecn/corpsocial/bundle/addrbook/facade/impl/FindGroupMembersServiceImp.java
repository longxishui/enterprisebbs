package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindGroupMembersService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupSizeParam;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据部门deptid获取包括子部门的用户
 */
@OsgiService(serviceType = FindGroupMembersService.class)
public class FindGroupMembersServiceImp implements FindGroupMembersService {
//	private ContactDao contactDao = new ContactDao();
//	private DeptDao deptDao = new DeptDao();

    @EventBusAnnotation.Component
    private UserDao dao ;
    @EventBusAnnotation.Component
    private DepartDao departDao ;

    @Override
    public List<User> invoke(GetGroupSizeParam params) {
//		List<String> list = new ArrayList<String>();
//
//		String id = deptDao.getIdByServerDeptId(Config.getInstance().getUserId(),params.getDeptId());
//		if (id == null) {
//			return new ArrayList<Contact>();
//		}
//
//		list.add(id);
//		List<ContactBriefVO> queryResult = contactDao.findByDepts(Config.getInstance().getUserId(),
//                list, params.getStart(), params.getLimit());
//
//		List<Contact> result = new ArrayList<Contact>();
//		for(ContactBriefVO item : queryResult) {
//			result.add(item.toContact());
//		}
//
//		return result;


        List<User> users = new ArrayList<User>();
        String deptId = params.getDeptId();
        int start = params.getStart();
        int count = params.getLimit();
        try {
            DepartEntity entity = departDao.findDeptById(deptId);
            List<String> depts = new ArrayList();
            depts.add(entity.getCode());
//            users.addAll(dao.findByDeptCodes(Config.getInstance().getUserId(), depts, entity.getCorpId(), start, count));

        } catch (Exception ex) {
            LogUtil.e("FindGroupMembersServiceImpl查询出错", ex);
        }
        return users;
    }

}
