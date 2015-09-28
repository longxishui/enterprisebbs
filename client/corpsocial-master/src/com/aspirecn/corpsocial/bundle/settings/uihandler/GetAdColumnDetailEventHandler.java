package com.aspirecn.corpsocial.bundle.settings.uihandler;

import com.aspirecn.corpsocial.bundle.settings.event.GetAdcolumnDetailEvent;
import com.aspirecn.corpsocial.bundle.settings.repository.AdcolumnDao;
import com.aspirecn.corpsocial.bundle.settings.repository.entiy.AdcolumnEntiy;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

/**
 * Created by chenziqiang on 15-3-24.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetAdcolumnDetailEvent.class)
public class GetAdColumnDetailEventHandler implements IHandler<AdcolumnEntiy, GetAdcolumnDetailEvent> {
    private AdcolumnDao dao = new AdcolumnDao();

    @Override
    public AdcolumnEntiy handle(GetAdcolumnDetailEvent args) {

        return dao.findByWhere("sid", args.getAdcolumnId());
    }
}
