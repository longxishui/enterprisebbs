package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.listener.RefreshImMainTabSubject.RefreshImMainTabEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RefreshImMainTabSubject extends
        EventSubject<RefreshImMainTabEventListener, RefreshImMainTabEvent> {

    @Override
    public void notifyListener(RefreshImMainTabEvent event) {
        for (RefreshImMainTabEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRefreshImMainTabEvent(event);
        }
    }

    public interface RefreshImMainTabEventListener extends EventListener {
        void onHandleRefreshImMainTabEvent(RefreshImMainTabEvent event);
    }

}
