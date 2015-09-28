package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.ApproveGroupApplyRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.ApproveGroupApplyRespSubject.ApproveGroupApplyRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class ApproveGroupApplyRespSubject extends
        EventSubject<ApproveGroupApplyRespEventListener, ApproveGroupApplyRespEvent> {

    @Override
    public void notifyListener(ApproveGroupApplyRespEvent event) {
        for (ApproveGroupApplyRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleApproveGroupApplyRespEvent(event);
        }
    }

    public interface ApproveGroupApplyRespEventListener extends EventListener {
        void onHandleApproveGroupApplyRespEvent(ApproveGroupApplyRespEvent event);
    }
}
