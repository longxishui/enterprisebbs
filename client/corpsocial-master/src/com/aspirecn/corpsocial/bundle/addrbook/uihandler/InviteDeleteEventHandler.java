package com.aspirecn.corpsocial.bundle.addrbook.uihandler;


import com.aspirecn.corpsocial.bundle.addrbook.event.InviteDeleteEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * 删除好友邀请消息记录
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.UIEventHandler(eventType = InviteDeleteEvent.class)
public class InviteDeleteEventHandler extends EventHandler implements IHandler<Null, InviteDeleteEvent> {

    @EventBusAnnotation.Component
    private FriendInviteDao dao ;

    @Override
    public Null handle(final InviteDeleteEvent inviteReadEvent) {
        List<Integer> seqNoes = inviteReadEvent.getSeqNoes();
        for (int seq : seqNoes) {
            try {
                dao.deleteInvite(seq);
            } catch (SQLException ex) {
                LogUtil.e("删除邀请消息出错", ex);
            }
        }
        return new Null();
    }
}
