package com.aspirecn.corpsocial.bundle.common.facade.impl;

import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.bundle.common.facade.QuitService;
import com.aspirecn.corpsocial.common.eventbus.OsgiAnnotation.OsgiService;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

@OsgiService(serviceType = QuitService.class)
public class QuitServiceImpl implements QuitService {

    @Override
    public Void invoke(Void params) {
        UiEventHandleFacade instance = UiEventHandleFacade.getInstance();
        instance.handle(new QuitEvent());
        return null;
    }

}
