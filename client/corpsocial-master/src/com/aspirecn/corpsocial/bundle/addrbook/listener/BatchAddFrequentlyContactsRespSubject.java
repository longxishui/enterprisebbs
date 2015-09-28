package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.BatchAddFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.BatchAddFrequentlyContactsRespSubject.BatchAddFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class BatchAddFrequentlyContactsRespSubject
        extends
        EventSubject<BatchAddFrequentlyContactRespEventListener, BatchAddFrequentlyContactRespEvent> {

    @Override
    public void notifyListener(BatchAddFrequentlyContactRespEvent event) {
        for (BatchAddFrequentlyContactRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAddFrequentlyContactRespEvent(event);
        }
    }

    public interface BatchAddFrequentlyContactRespEventListener extends EventListener {
        void onHandleAddFrequentlyContactRespEvent(
                BatchAddFrequentlyContactRespEvent event);
    }
}
