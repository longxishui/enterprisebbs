package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.ContactEntity;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO.PartTimeDeptInfo;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取联系人列表简要信息。
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = GetContactsEvent.class)
public class GetContactsEventHandler implements
        IHandler<List<ContactVO>, GetContactsEvent> {

    @Override
    public List<ContactVO> handle(GetContactsEvent busEvent) {
        List<ContactVO> result = new ArrayList<ContactVO>();
//        ContactDao dao = new ContactDao();
//        List<ContactEntity> entities = dao.findUsers(Config.getInstance().getUserId(), busEvent.getContactIds());
//        if (entities != null && entities.size() > 0) {
//            for (ContactEntity entity : entities) {
//                ContactVO vo = getContactVO(entity);
//                result.add(vo);
//            }
//
//        }
        return result;
    }

    /**
     * 将Contact实体转换成VO对象
     *
     * @param entity
     * @return
     */
    private ContactVO getContactVO(ContactEntity entity) {
        ContactVO contact = new ContactVO();
        contact.setId(entity.getId());
        contact.setDeptId(entity.getDeptId());
        contact.setDeptName(entity.getDeptName());
        contact.setDuty(entity.getDuty());
        contact.setEmail(entity.getEmail());
        contact.setGender(entity.getGender());
        contact.setJobNumber(entity.getJobNumber());
        contact.setHeadImageUrl(entity.getHeadImageUrl());
        contact.setMobilePhone(entity.getMobilePhone());
        contact.setInnerPhoneNumber(entity.getInnerPhoneNumber());
        contact.setName(entity.getName());
        contact.setBirthday(entity.getBirthday());
//        List<PartTimeDeptInfo> partTimeDeptInfos =
//                PartTimeDeptUtil.getPartTimeInfo(entity.getPartTimeDept());
//        contact.setPartTimeDeptInfo(partTimeDeptInfos);
        contact.setSignature(entity.getSignature());

        return contact;
    }
}
