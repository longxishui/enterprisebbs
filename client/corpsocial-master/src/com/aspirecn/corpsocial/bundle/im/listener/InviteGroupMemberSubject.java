package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.listener.InviteGroupMemberSubject.InviteGroupMemberEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class InviteGroupMemberSubject extends
        EventSubject<InviteGroupMemberEventListener, InviteGroupMemberEvent> {

    @Override
    public void notifyListener(InviteGroupMemberEvent event) {
        for (InviteGroupMemberEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleInviteGroupMemberEvent(event);
        }
    }

    public interface InviteGroupMemberEventListener extends EventListener {
        void onHandleInviteGroupMemberEvent(InviteGroupMemberEvent event);
    }
}
