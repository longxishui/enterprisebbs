package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddressBookUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.List;


/**
 * 统计通讯录模块未读消息数
 * Created by lihaiqiang on 15-6-29.
 */
@EventBusAnnotation.UIEventHandler(eventType = CalculateAddressBookUnReadMsgCountEvent.class)
public class CalculateAddressBookUnReadMsgCountEventHandler implements
        IHandler<Null, CalculateAddressBookUnReadMsgCountEvent> {

    @Override
    public Null handle(CalculateAddressBookUnReadMsgCountEvent args) {
        //统计未读好友邀请数
        int unReadInviteFriendCount = 0;

        try {
            String userId = Config.getInstance().getUserId();
            List<FriendInvite> friendInvites = new FriendInviteDao().queryInvite(userId, 0, 0);
            for (FriendInvite friendInvite : friendInvites) {
                if (friendInvite.getIsNew() == 1) {
                    unReadInviteFriendCount += 1;
                }
            }
        } catch (SQLException e) {
            LogUtil.e("查询未读邀请出错", e);
        }

        //发出通知
        AddressBookUnReadMsgCountRespEvent event = new AddressBookUnReadMsgCountRespEvent();
        event.setUnReadInviteFriendCount(unReadInviteFriendCount);
        EventListenerSubjectLoader.getInstance().notifyListener(event);

        return new Null();
    }

}
