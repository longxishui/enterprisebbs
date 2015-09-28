package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFriendRespSubject.RemoveFriendRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.EventListenerSubject
public class RemoveFriendRespSubject extends
        EventSubject<RemoveFriendRespEventListener, RemoveFriendRespEvent> {

    @Override
    public void notifyListener(RemoveFriendRespEvent event) {
        for (RemoveFriendRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleRemoveFriendRespEvent(event);
        }
    }

    public interface RemoveFriendRespEventListener extends EventListener {
        void onHandleRemoveFriendRespEvent(
                RemoveFriendRespEvent event);
    }
}
