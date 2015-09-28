package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactDetailEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.ContactEntity;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FrequentlyContactEntity;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO.PartTimeDeptInfo;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * 获取联系人详细信息。
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = GetContactDetailEvent.class)
public class GetContactDetailEventHandler implements
        IHandler<ContactVO, GetContactDetailEvent> {

    @Override
    public ContactVO handle(GetContactDetailEvent busEvent) {
//        ContactDao dao = new ContactDao();
//        ContactEntity entity = dao.findByUserId(busEvent.getContactId());
//        if (entity != null) {
//            ContactVO vo = getContactVO(entity);
//
//            FrequentlyContactDao fdao = new FrequentlyContactDao();
//            FrequentlyContactEntity fe = fdao.query(Config.getInstance().getUserId(), entity.getId());
//            vo.setFrequent(fe != null);
//
//            return vo;
//        } else {
            return new ContactVO();
//        }
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
