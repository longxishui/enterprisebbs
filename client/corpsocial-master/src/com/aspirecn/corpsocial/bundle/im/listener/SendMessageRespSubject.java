package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.SendMessageRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.SendMessageRespSubject.SendMessageRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class SendMessageRespSubject extends
        EventSubject<SendMessageRespEventListener, SendMessageRespEvent> {

    @Override
    public void notifyListener(SendMessageRespEvent event) {
        for (SendMessageRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleSendMessageRespEvent(event);
        }
    }

    public interface SendMessageRespEventListener extends EventListener {
        void onHandleSendMessageRespEvent(SendMessageRespEvent event);
    }

}
