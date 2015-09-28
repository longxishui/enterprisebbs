package com.aspirecn.corpsocial.bundle.addrbook.uihandler;


import com.aspirecn.corpsocial.bundle.addrbook.event.InviteReadEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteReadRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.UIEventHandler(eventType = InviteReadEvent.class)
public class InviteReadEventHandler extends EventHandler implements IHandler<Null, InviteReadEvent> {

    @EventBusAnnotation.Component
    private FriendInviteDao dao ;

    @Override
    public Null handle(final InviteReadEvent inviteReadEvent) {
        List<Integer> seqs = inviteReadEvent.getSeqs();
        List<Integer> success = new ArrayList<Integer>();
        for (int seq : seqs) {
            try {
                dao.updateReaded(seq);
                success.add(seq);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        InviteReadRespEvent event = new InviteReadRespEvent();
        event.setSeqs(success);
        instance.notifyListener(event);
        return new Null();
    }
}
