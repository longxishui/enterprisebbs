package com.aspirecn.corpsocial.bundle.im.uihandler;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.im.event.QuitGroupEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupOperateEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退出群组
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = QuitGroupEvent.class)
public class QuitGroupEventHandler implements IHandler<Null, QuitGroupEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    private GroupDao groupDao = new GroupDao();

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    private ChatDao chatDao = new ChatDao();

    private MsgDao msgDao = new MsgDao();

    @Override
    public Null handle(final QuitGroupEvent busEvent) {
        return new Null();
    }

}
