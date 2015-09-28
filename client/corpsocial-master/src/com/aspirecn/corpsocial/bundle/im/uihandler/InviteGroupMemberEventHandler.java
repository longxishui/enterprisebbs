package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 邀请成员加入群处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = InviteGroupMemberEvent.class)
public class InviteGroupMemberEventHandler implements IHandler<Null, InviteGroupMemberEvent> {

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    @Override
    public Null handle(final InviteGroupMemberEvent busEvent) {

        return new Null();
    }


}
