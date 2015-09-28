package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.QuitGroupEvent;
import com.aspirecn.corpsocial.bundle.im.listener.QuitGroupSubject.QuitGroupEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class QuitGroupSubject extends
        EventSubject<QuitGroupEventListener, QuitGroupEvent> {

    @Override
    public void notifyListener(QuitGroupEvent event) {
        for (QuitGroupEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleQuitGroupEvent(event);
        }
    }

    public interface QuitGroupEventListener extends EventListener {
        void onHandleQuitGroupEvent(QuitGroupEvent event);
    }
}
