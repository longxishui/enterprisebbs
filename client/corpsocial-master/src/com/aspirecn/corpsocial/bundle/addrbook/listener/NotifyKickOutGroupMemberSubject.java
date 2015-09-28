package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyKickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyKickOutGroupMemberSubject.NotifyKickOutGroupMemberEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class NotifyKickOutGroupMemberSubject extends
        EventSubject<NotifyKickOutGroupMemberEventListener, NotifyKickOutGroupMemberEvent> {

    @Override
    public void notifyListener(NotifyKickOutGroupMemberEvent event) {
        for (NotifyKickOutGroupMemberEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleKickOutGroupMemberEvent(event);
        }
    }

    public interface NotifyKickOutGroupMemberEventListener extends EventListener {
        void onHandleKickOutGroupMemberEvent(NotifyKickOutGroupMemberEvent event);
    }
}
