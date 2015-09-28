package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.JoinGroupApplyRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.JoinGroupApplyRespSubject.JoinGroupApplyRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class JoinGroupApplyRespSubject extends
        EventSubject<JoinGroupApplyRespEventListener, JoinGroupApplyRespEvent> {

    @Override
    public void notifyListener(JoinGroupApplyRespEvent event) {
        for (JoinGroupApplyRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleJoinGroupApplyRespEvent(event);
        }
    }

    public interface JoinGroupApplyRespEventListener extends EventListener {
        void onHandleJoinGroupApplyRespEvent(JoinGroupApplyRespEvent event);
    }

}
