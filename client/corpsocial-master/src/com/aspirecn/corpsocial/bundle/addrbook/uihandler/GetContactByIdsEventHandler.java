package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import android.util.Log;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 根据联系人的ID列表，查询联系人。
 * Created by chenziqiang on 15-5-14.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetContactByIdsEvent.class)
public class GetContactByIdsEventHandler implements IHandler<List<ContactBriefVO>, GetContactByIdsEvent> {
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public List<ContactBriefVO> handle(GetContactByIdsEvent event) {
        List<ContactBriefVO> results = new ArrayList<ContactBriefVO>();
        try {
            List<User> list = dao.findFilterByContactIds(Config.getInstance().getUserId(), event.getIds());
        /*移除空数据*/
            List<User> isNull = new ArrayList<User>();
            isNull.add(null);
            list.removeAll(isNull);
            for (User item : list) {
                results.add(toContact(item));
            }
        } catch (SQLException e) {
            LogUtil.e("FindContactsByIdsServiceImpl查询出错:", e);
        }
        return results;

    }

    private ContactBriefVO toContact(User user) {
        ContactBriefVO contact = new ContactBriefVO();
        contact.setDeptName(user.getDeptName());
        contact.setId(user.getUserid());
        contact.setName(user.getName());
        contact.setImStatus(user.getImStatus());
        contact.setHeadImageUrl(user.getUrl());
        contact.setInitialCode(user.getInitialKey());
        contact.setSignature(user.getSignature());
        contact.setMobilePhone(user.getInnerPhone());
        contact.setLoginName(user.getLoginName());
        contact.setUserType(user.getUserType());

        return contact;
    }
}
