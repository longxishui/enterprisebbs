package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.JoinGroupApplyEvent;
import com.aspirecn.corpsocial.bundle.im.listener.JoinGroupApplySubject.JoinGroupApplyEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class JoinGroupApplySubject extends
        EventSubject<JoinGroupApplyEventListener, JoinGroupApplyEvent> {

    @Override
    public void notifyListener(JoinGroupApplyEvent event) {
        for (JoinGroupApplyEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleJoinGroupApplyEvent(event);
        }
    }

    public interface JoinGroupApplyEventListener extends EventListener {
        void onHandleJoinGroupApplyEvent(JoinGroupApplyEvent event);
    }

}
