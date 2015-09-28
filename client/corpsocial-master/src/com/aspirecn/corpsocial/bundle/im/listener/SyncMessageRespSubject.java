package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.SyncMessageRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class SyncMessageRespSubject extends
        EventSubject<SyncMessageRespSubject.SyncMessageRespEventListener, SyncMessageRespEvent> {

    @Override
    public void notifyListener(SyncMessageRespEvent event) {
        for (SyncMessageRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleSyncMessageRespEvent(event);
        }
    }

    public interface SyncMessageRespEventListener extends EventListener {
        void onHandleSyncMessageRespEvent(SyncMessageRespEvent event);
    }

}
