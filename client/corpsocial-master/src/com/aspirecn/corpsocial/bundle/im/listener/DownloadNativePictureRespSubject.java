package com.aspirecn.corpsocial.bundle.im.listener;

import com.aspirecn.corpsocial.bundle.im.event.DownloadNativePictureRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.DownloadNativePictureRespSubject.DownloadNativePictureRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;

@EventListenerSubject
public class DownloadNativePictureRespSubject extends
        EventSubject<DownloadNativePictureRespEventListener, DownloadNativePictureRespEvent> {

    @Override
    public void notifyListener(DownloadNativePictureRespEvent event) {
        for (DownloadNativePictureRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleDownloadNativePictureRespEvent(event);
        }
    }

    public interface DownloadNativePictureRespEventListener extends EventListener {
        void onHandleDownloadNativePictureRespEvent(DownloadNativePictureRespEvent event);
    }
}
