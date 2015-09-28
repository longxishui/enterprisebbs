package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.ReceiveMessageEvent;
import com.aspirecn.corpsocial.bundle.im.listener.ReceiveMessageSubject.ReceiveMessageEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ReceiveMessageSubject extends
        EventSubject<ReceiveMessageEventListener, ReceiveMessageEvent> {

    @Override
    public void notifyListener(ReceiveMessageEvent event) {
        for (ReceiveMessageEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleReceiveMessageEvent(event);
        }
    }

    public interface ReceiveMessageEventListener extends EventListener {
        void onHandleReceiveMessageEvent(ReceiveMessageEvent event);
    }

}
