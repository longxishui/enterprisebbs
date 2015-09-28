package com.aspirecn.corpsocial.bundle.addrbook.facade.impl;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindDepartParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindDepartService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.DepartDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation;

import java.util.List;

/**
 * Created by chenbin on 2015/8/10.
 * 根据企业ID和deptcode获取子部门
 */
@OsgiAnnotation.OsgiService(serviceType = FindDepartService.class)
public class FindDepartServiceImpl implements FindDepartService {

    @EventBusAnnotation.Component
    private DepartDao departDao ;

    public List<Dept> invoke(FindDepartParam param) {
        String deptCode = param.getDeptCode();
        String corpId = param.getCorpId();
        List<Dept> depts = departDao.findChildren(deptCode, corpId);

        return depts;
    }
}
