package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.NetStatueChangeEvent;
import com.aspirecn.corpsocial.bundle.im.listener.NetStatueChangeSubject.NetStatueChangeEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class NetStatueChangeSubject extends
        EventSubject<NetStatueChangeEventListener, NetStatueChangeEvent> {

    @Override
    public void notifyListener(NetStatueChangeEvent event) {
        for (NetStatueChangeEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleNetStatueChangeEvent(event);
        }
    }

    public interface NetStatueChangeEventListener extends EventListener {
        void onHandleNetStatueChangeEvent(NetStatueChangeEvent event);
    }

}
