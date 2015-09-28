package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberRespSubject.KickOutGroupMemberRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class KickOutGroupMemberRespSubject extends
        EventSubject<KickOutGroupMemberRespEventListener, KickOutGroupMemberRespEvent> {

    @Override
    public void notifyListener(KickOutGroupMemberRespEvent event) {
        for (KickOutGroupMemberRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleKickOutGroupMemberRespEvent(event);
        }
    }

    public interface KickOutGroupMemberRespEventListener extends EventListener {
        void onHandleKickOutGroupMemberRespEvent(KickOutGroupMemberRespEvent event);
    }
}
