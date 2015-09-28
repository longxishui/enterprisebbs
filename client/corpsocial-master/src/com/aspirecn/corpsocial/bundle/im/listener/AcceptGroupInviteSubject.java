package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.AcceptGroupInviteEvent;
import com.aspirecn.corpsocial.bundle.im.listener.AcceptGroupInviteSubject.AcceptGroupInviteEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class AcceptGroupInviteSubject extends
        EventSubject<AcceptGroupInviteEventListener, AcceptGroupInviteEvent> {

    @Override
    public void notifyListener(AcceptGroupInviteEvent event) {
        for (AcceptGroupInviteEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAcceptGroupInviteEvent(event);
        }
    }

    public interface AcceptGroupInviteEventListener extends EventListener {
        void onHandleAcceptGroupInviteEvent(AcceptGroupInviteEvent event);
    }
}
