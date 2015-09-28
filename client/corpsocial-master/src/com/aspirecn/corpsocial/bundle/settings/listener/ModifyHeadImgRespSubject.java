package com.aspirecn.corpsocial.bundle.settings.listener;

import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyHeadImgRespSubject.ModifyHeadPortraitRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.EventListenerSubject;
import com.aspirecn.corpsocial.common.eventbus.EventListener;
import com.aspirecn.corpsocial.common.eventbus.EventSubject;


@EventListenerSubject
public class ModifyHeadImgRespSubject
        extends
        EventSubject<ModifyHeadPortraitRespEventListener, ModifyHeadImgRespEvent> {
    @Override
    public void notifyListener(ModifyHeadImgRespEvent event) {
        for (ModifyHeadPortraitRespEventListener eventListener : eventListenerQueue) {
            eventListener.onHandleModifyHeadPortraitRespEvent(event);
        }
    }

    public interface ModifyHeadPortraitRespEventListener extends EventListener {
        void onHandleModifyHeadPortraitRespEvent(
                ModifyHeadImgRespEvent event);
    }
}
