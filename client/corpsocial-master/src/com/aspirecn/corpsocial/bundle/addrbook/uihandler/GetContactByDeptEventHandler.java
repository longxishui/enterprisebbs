package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByDeptEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * 获取指定部门的联系人。
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = GetContactByDeptEvent.class)
public class GetContactByDeptEventHandler implements
        IHandler<List<ContactBriefVO>, GetContactByDeptEvent> {

//    private ContactDao dao = new ContactDao();

    @Override
    public List<ContactBriefVO> handle(GetContactByDeptEvent busEvent) {
        return null;
//        return dao.findByDeptId(Config.getInstance().getUserId(), busEvent.getDeptId());
    }
}
