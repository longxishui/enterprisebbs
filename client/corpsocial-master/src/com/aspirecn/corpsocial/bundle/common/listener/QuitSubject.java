package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.bundle.common.listener.QuitSubject.QuitEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class QuitSubject extends EventSubject<QuitEventListener, QuitEvent> {

    @Override
    public void notifyListener(QuitEvent event) {
        for (QuitEventListener eventListener : eventListenerQueue) {
            eventListener.onQuitEvent(event);
        }
    }

    public interface QuitEventListener extends EventListener {
        void onQuitEvent(QuitEvent event);
    }

}
