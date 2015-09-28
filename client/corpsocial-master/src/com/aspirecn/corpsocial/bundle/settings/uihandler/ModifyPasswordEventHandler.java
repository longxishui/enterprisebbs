package com.aspirecn.corpsocial.bundle.settings.uihandler;

import com.aspirecn.corpsocial.bundle.settings.event.ModifyPasswordEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;


/**
 * 修改密码处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = ModifyPasswordEvent.class)
public class ModifyPasswordEventHandler implements
        IHandler<Null, ModifyPasswordEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader eventListenSubject = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(ModifyPasswordEvent args) {

        return new Null();
    }
}
