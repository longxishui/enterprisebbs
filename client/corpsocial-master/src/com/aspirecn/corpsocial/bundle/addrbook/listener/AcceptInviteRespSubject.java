package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptInviteRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptInviteRespSubject.AcceptInviteRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.EventListenerSubject
public class AcceptInviteRespSubject extends
        EventSubject<AcceptInviteRespEventListener, AcceptInviteRespEvent> {

    @Override
    public void notifyListener(AcceptInviteRespEvent event) {
        for (AcceptInviteRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAcceptInviteRespEvent(event);
        }
    }

    public interface AcceptInviteRespEventListener extends EventListener {
        void onHandleAcceptInviteRespEvent(AcceptInviteRespEvent event);
    }
}
