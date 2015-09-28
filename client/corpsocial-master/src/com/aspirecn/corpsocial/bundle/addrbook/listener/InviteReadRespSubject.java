package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.InviteReadRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.InviteReadRespSubject.InviteReadEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.EventListenerSubject
public class InviteReadRespSubject extends
        EventSubject<InviteReadEventListener, InviteReadRespEvent> {

    @Override
    public void notifyListener(InviteReadRespEvent event) {
        for (InviteReadEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleInviteReadEvent(event);
        }
    }

    public interface InviteReadEventListener extends EventListener {
        void onHandleInviteReadEvent(InviteReadRespEvent event);
    }
}
