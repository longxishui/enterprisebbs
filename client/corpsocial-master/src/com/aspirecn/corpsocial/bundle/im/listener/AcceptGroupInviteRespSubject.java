package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.AcceptGroupInviteRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.AcceptGroupInviteRespSubject.AcceptGroupInviteRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class AcceptGroupInviteRespSubject extends
        EventSubject<AcceptGroupInviteRespEventListener, AcceptGroupInviteRespEvent> {

    @Override
    public void notifyListener(AcceptGroupInviteRespEvent event) {
        for (AcceptGroupInviteRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAcceptGroupInviteRespEvent(event);
        }
    }

    public interface AcceptGroupInviteRespEventListener extends EventListener {
        void onHandleAcceptGroupInviteRespEvent(AcceptGroupInviteRespEvent event);
    }
}
