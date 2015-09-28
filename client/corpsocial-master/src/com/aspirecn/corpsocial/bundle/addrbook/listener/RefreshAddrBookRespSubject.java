package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshAddrBookRespSubject.RefreshAddrbookRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RefreshAddrBookRespSubject
        extends
        EventSubject<RefreshAddrbookRespEventListener, RefreshAddrbookRespEvent> {

    @Override
    public void notifyListener(RefreshAddrbookRespEvent event) {

        for (RefreshAddrbookRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRefreshAddrbookRespEvent(event);
        }
    }

    public interface RefreshAddrbookRespEventListener extends EventListener {
        void onHandleRefreshAddrbookRespEvent(
                RefreshAddrbookRespEvent event);
    }
}
