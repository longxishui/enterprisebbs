package com.aspirecn.corpsocial.bundle.addrbook.listener;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetFriendListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.GetFriendListRespSubject.GetFriendListRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.EventListenerSubject
public class GetFriendListRespSubject extends
        EventSubject<GetFriendListRespEventListener, GetFriendListRespEvent> {

    @Override
    public void notifyListener(GetFriendListRespEvent event) {

        for (GetFriendListRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleGetFriendListRespEvent(event);
        }
    }

    public interface GetFriendListRespEventListener extends EventListener {
        void onHandleGetFriendListRespEvent(
                GetFriendListRespEvent event);
    }
}
