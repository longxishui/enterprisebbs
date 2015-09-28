package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetAddressListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.GetAddressListRespSubject.GetAddressListRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.EventListenerSubject
public class GetAddressListRespSubject extends
        EventSubject<GetAddressListRespEventListener, GetAddressListRespEvent> {

    @Override
    public void notifyListener(GetAddressListRespEvent event) {

        for (GetAddressListRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetAddressListRespEvent(event);
        }
    }

    public interface GetAddressListRespEventListener extends EventListener {
        void onHandleGetAddressListRespEvent(
                GetAddressListRespEvent event);
    }
}
