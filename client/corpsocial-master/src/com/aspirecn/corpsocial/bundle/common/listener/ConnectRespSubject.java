package com.aspirecn.corpsocial.bundle.common.listener;

import com.aspirecn.corpsocial.bundle.common.event.ConnectRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by yinghuihong on 15/3/31.
 */
@EventBusAnnotation.EventListenerSubject
public class ConnectRespSubject extends EventSubject<ConnectRespSubject.ConnectRespEventListener, ConnectRespEvent> {

    @Override
    public void notifyListener(ConnectRespEvent event) {
        for (ConnectRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleConnectRespEvent(event);
        }
    }

    public interface ConnectRespEventListener extends EventListener {
        void onHandleConnectRespEvent(ConnectRespEvent event);
    }
}
