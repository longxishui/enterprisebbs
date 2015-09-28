package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetSelectContactCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.util.LogUtil;

/**
 * 根据部门，获取其联系人数量。
 * Created by chenziqiang on 15-5-14.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetSelectContactCountEvent.class)
public class GetSelectContactCountEventHandler implements IHandler<Integer, GetSelectContactCountEvent> {

    @EventBusAnnotation.Component
    UserDao contactdao ;

    @Override
    public Integer handle(GetSelectContactCountEvent event) {
//        ContactDao contactdao = new ContactDao();
//        return contactdao.getContactCountByDept(Config.getInstance().getUserId(),
//                event.getId(), true);

        int count = 0;
//        try {
//            count = contactdao.getContactCountByDept(Config.getInstance().getUserId(),
//                    event.getId(), event.getCorpId(), true);
//        } catch (Exception e) {
//            LogUtil.e("", e);
//        }
        return count;
    }
}
