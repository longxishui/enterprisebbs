package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupSizeService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;

/**
 * 根据部门deptid，获取其联系人数量
 */
@OsgiService(serviceType = GetGroupSizeService.class)
public class GetGroupSizeImp implements GetGroupSizeService {
//	private ContactDao contactDao = new ContactDao();
//	private DeptDao deptDao = new DeptDao();

    @EventBusAnnotation.Component
    private DepartDao departDao ;
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Integer invoke(String params) {
//		String id = deptDao.getIdByServerDeptId(Config.getInstance().getUserId(), params);
//		if (id == null) {
//			return 0;
//		}
//
//		return contactDao.getContactCountByDept(Config.getInstance().getUserId(), id, true);

        int count = 0;
        try {
            DepartEntity entity = departDao.findDeptById(params);
//            count = dao.getContactCountByDept(Config.getInstance().getUserId(), entity.getCode(), entity.getCorpId(), true);
        } catch (Exception e) {
            LogUtil.e("GetGroupSizeService出错", e);
        }

        return count;
    }
}
