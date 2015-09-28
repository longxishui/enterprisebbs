package com.aspirecn.corpsocial.bundle.settings.uihandler;

import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgEvent;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * 头像上传类
 *
 * @author liangjian
 */
@UIEventHandler(eventType = ModifyHeadImgEvent.class)
public class ModifyHeadImgEventHandler implements
        IHandler<Null, ModifyHeadImgEvent> {
    private EventListenerSubjectLoader eventListenSubject = EventListenerSubjectLoader
            .getInstance();

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();
    private ModifyHeadImgRespEvent mModifyHeadImgRespEvent = new ModifyHeadImgRespEvent();

    @Override
    public Null handle(ModifyHeadImgEvent args) {
        final String headImageUrl = args.getHeadImageUrl();
        return new Null();
    }
}
