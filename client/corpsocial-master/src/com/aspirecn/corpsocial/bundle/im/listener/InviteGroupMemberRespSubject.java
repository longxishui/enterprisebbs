package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.InviteGroupMemberRespSubject.InviteGroupMemberRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class InviteGroupMemberRespSubject extends
        EventSubject<InviteGroupMemberRespEventListener, InviteGroupMemberRespEvent> {

    @Override
    public void notifyListener(InviteGroupMemberRespEvent event) {
        for (InviteGroupMemberRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleInviteGroupMemberRespEvent(event);
        }
    }

    public interface InviteGroupMemberRespEventListener extends EventListener {
        void onHandleInviteGroupMemberRespEvent(InviteGroupMemberRespEvent event);
    }
}
