package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFrequentlyContactsRespSubject.RemoveFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class RemoveFrequentlyContactsRespSubject
        extends
        EventSubject<RemoveFrequentlyContactRespEventListener, RemoveFrequentlyContactRespEvent> {

    @Override
    public void notifyListener(RemoveFrequentlyContactRespEvent event) {
        for (RemoveFrequentlyContactRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRemoveFrequentlyContactRespEvent(event);
        }
    }

    public interface RemoveFrequentlyContactRespEventListener extends EventListener {
        void onHandleRemoveFrequentlyContactRespEvent(
                RemoveFrequentlyContactRespEvent event);
    }
}
