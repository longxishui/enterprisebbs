package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.CustomerServiceRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.CustomerServiceRespSubject.CustomerServiceRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by chenziqiang on 15-1-14.
 */
@EventListenerSubject
public class CustomerServiceRespSubject extends
        EventSubject<CustomerServiceRespEventListener, CustomerServiceRespEvent> {

    @Override
    public void notifyListener(CustomerServiceRespEvent event) {
        for (CustomerServiceRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleCustomerServiceRespEvent(event);
        }
    }

    public interface CustomerServiceRespEventListener extends EventListener {
        void onHandleCustomerServiceRespEvent(CustomerServiceRespEvent event);
    }
}
