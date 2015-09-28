package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.ImUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.ImUnReadMsgCountRespSubject.ImUnReadMsgCountRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

/**
 * Created by yinghuihong on 15/8/27.
 */
@EventBusAnnotation.EventListenerSubject
public class ImUnReadMsgCountRespSubject extends EventSubject<ImUnReadMsgCountRespEventListener, ImUnReadMsgCountRespEvent> {

    @Override
    public void notifyListener(ImUnReadMsgCountRespEvent event) {
        for (ImUnReadMsgCountRespEventListener listener : eventListenerQueue) {
            listener.onHandleIMUnReadMsgCountRespEvent(event);
        }
    }

    public interface ImUnReadMsgCountRespEventListener extends EventListener {
        void onHandleIMUnReadMsgCountRespEvent(ImUnReadMsgCountRespEvent event);
    }
}
