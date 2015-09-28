package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接收到邀请后反馈处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = KickOutGroupMemberEvent.class)
public class KickOutGroupMemberEventHandler implements IHandler<Null, KickOutGroupMemberEvent> {

//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    @Autowired
    private GroupDao groupDao = new GroupDao();

    @Override
    public Null handle(final KickOutGroupMemberEvent busEvent) {

        return new Null();
    }
}
