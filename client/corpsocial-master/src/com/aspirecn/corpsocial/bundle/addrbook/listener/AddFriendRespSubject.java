package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFriendRespSubject.AddFriendRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-19.
 */
@EventBusAnnotation.EventListenerSubject
public class AddFriendRespSubject extends
        EventSubject<AddFriendRespEventListener, AddFriendRespEvent> {

    @Override
    public void notifyListener(AddFriendRespEvent event) {
        for (AddFriendRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleAddFriendRespEvent(event);
        }
    }

    public interface AddFriendRespEventListener extends EventListener {
        void onHandleAddFriendRespEvent(AddFriendRespEvent event);
    }
}
