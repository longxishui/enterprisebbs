package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 * 根据部门deptid获取包括子子部门的用户id列表
 */
@OsgiAnnotation.OsgiService(serviceType = GetGroupIdsService.class)
public class GetGroupIdsServiceImpl implements GetGroupIdsService {

    @EventBusAnnotation.Component
    private DepartDao departDao ;
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public List<String> invoke(String deptId) {
        List<String> ids = new ArrayList<String>();
        try {
            DepartEntity entity = departDao.findDeptById(deptId);

//            ids.addAll(dao.getContactIdsByDept(Config.getInstance().getUserId(), entity.getCode(), entity.getCorpId(), true));
        } catch (Exception e) {
            LogUtil.e("GetGroupSizeService出错", e);
        }
        return ids;
    }
}
