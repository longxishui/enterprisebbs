package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFrequentlyContactsRespSubject.AddFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class AddFrequentlyContactsRespSubject
        extends
        EventSubject<AddFrequentlyContactRespEventListener, AddFrequentlyContactRespEvent> {

    @Override
    public void notifyListener(AddFrequentlyContactRespEvent event) {
        for (AddFrequentlyContactRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAddFrequentlyContactRespEvent(event);
        }
    }

    public interface AddFrequentlyContactRespEventListener extends EventListener {
        void onHandleAddFrequentlyContactRespEvent(
                AddFrequentlyContactRespEvent event);
    }
}
