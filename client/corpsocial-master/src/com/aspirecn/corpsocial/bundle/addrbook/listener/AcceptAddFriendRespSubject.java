package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptAddFriendRespSubject.AcceptAddFriendRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-19.
 */
@EventBusAnnotation.EventListenerSubject
public class AcceptAddFriendRespSubject extends
        EventSubject<AcceptAddFriendRespEventListener, AcceptAddFriendRespEvent> {

    @Override
    public void notifyListener(AcceptAddFriendRespEvent event) {
        for (AcceptAddFriendRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAcceptAddFriendRespEvent(event);
        }
    }

    public interface AcceptAddFriendRespEventListener extends EventListener {
        void onHandleAcceptAddFriendRespEvent(AcceptAddFriendRespEvent event);
    }
}
