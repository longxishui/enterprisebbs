package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetDeptChildrenEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DeptEntity;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * 根据指定部门ID，获取下级部门。如果部门ID为null，则返回根部门。
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = GetDeptChildrenEvent.class)
public class GetDeptChildrenEventHandler implements
        IHandler<List<DeptEntity>, GetDeptChildrenEvent> {
    @Override
    public List<DeptEntity> handle(GetDeptChildrenEvent busEvent) {
        return null;
//        DeptDao deptDao = new DeptDao();
//        return deptDao.findChildren(busEvent.getDeptId());
    }
}
