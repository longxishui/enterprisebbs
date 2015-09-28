package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.InviteFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.InviteFriendSubject.InviteFriendEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-19.
 */
@EventBusAnnotation.EventListenerSubject
public class InviteFriendSubject extends
        EventSubject<InviteFriendEventListener, InviteFriendEvent> {

    @Override
    public void notifyListener(InviteFriendEvent event) {
        for (InviteFriendEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleInviteFriendEvent(event);
        }
    }

    public interface InviteFriendEventListener extends EventListener {
        void onHandleInviteFriendEvent(InviteFriendEvent event);
    }
}
