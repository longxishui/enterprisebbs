package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactIdsByDeptIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * 根据部门列表，获取这些部门所有下级（直至树叶级）的联系人简要信息
 * Created by chenziqiang on 15-5-14.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetContactIdsByDeptIdsEvent.class)
public class GetContactIdsByDeptIdsEventHandler implements IHandler<List<ContactBriefVO>, GetContactIdsByDeptIdsEvent> {
    @Override
    public List<ContactBriefVO> handle(GetContactIdsByDeptIdsEvent args) {
//        ContactDao dao = new ContactDao();
//        return dao.findByDepts(Config.getInstance().getUserId(), args.getDeptIds());
        return null;
    }
}
