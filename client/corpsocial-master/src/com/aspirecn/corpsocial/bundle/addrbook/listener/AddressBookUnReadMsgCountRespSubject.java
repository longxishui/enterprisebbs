package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddressBookUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddressBookUnReadMsgCountRespSubject.AddressBookUnReadMsgCountRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class AddressBookUnReadMsgCountRespSubject extends
        EventSubject<AddressBookUnReadMsgCountRespEventListener, AddressBookUnReadMsgCountRespEvent> {

    @Override
    public void notifyListener(AddressBookUnReadMsgCountRespEvent event) {
        for (AddressBookUnReadMsgCountRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAddressBookUnReadMsgCountRespEvent(event);
        }
    }

    public interface AddressBookUnReadMsgCountRespEventListener extends EventListener {
        void onHandleAddressBookUnReadMsgCountRespEvent(
                AddressBookUnReadMsgCountRespEvent event);
    }

}
