package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberSubject.KickOutGroupMemberEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class KickOutGroupMemberSubject extends
        EventSubject<KickOutGroupMemberEventListener, KickOutGroupMemberEvent> {

    @Override
    public void notifyListener(KickOutGroupMemberEvent event) {
        for (KickOutGroupMemberEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleKickOutGroupMemberEvent(event);
        }
    }

    public interface KickOutGroupMemberEventListener extends EventListener {
        void onHandleKickOutGroupMemberEvent(KickOutGroupMemberEvent event);
    }
}
