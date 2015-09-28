package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.DismissGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupRespSubject.DismissGroupRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class DismissGroupRespSubject extends
        EventSubject<DismissGroupRespEventListener, DismissGroupRespEvent> {

    @Override
    public void notifyListener(DismissGroupRespEvent event) {
        for (DismissGroupRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleDismissGroupRespEvent(event);
        }
    }

    public interface DismissGroupRespEventListener extends EventListener {
        void onHandleDismissGroupRespEvent(DismissGroupRespEvent event);
    }
}
