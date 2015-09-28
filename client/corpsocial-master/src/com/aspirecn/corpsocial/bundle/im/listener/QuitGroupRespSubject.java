package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.QuitGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.QuitGroupRespSubject.QuitGroupRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class QuitGroupRespSubject extends
        EventSubject<QuitGroupRespEventListener, QuitGroupRespEvent> {

    @Override
    public void notifyListener(QuitGroupRespEvent event) {
        for (QuitGroupRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleQuitGroupRespEvent(event);
        }
    }

    public interface QuitGroupRespEventListener extends EventListener {
        void onHandleQuitGroupRespEvent(QuitGroupRespEvent event);
    }
}
