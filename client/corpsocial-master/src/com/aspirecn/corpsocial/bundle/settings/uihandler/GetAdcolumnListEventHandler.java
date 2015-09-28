package com.aspirecn.corpsocial.bundle.settings.uihandler;

import com.aspirecn.corpsocial.bundle.settings.event.GetAdcolumnListEvent;
import com.aspirecn.corpsocial.bundle.settings.repository.AdcolumnDao;
import com.aspirecn.corpsocial.bundle.settings.repository.vo.AdcolumnListVO;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * Created by chenziqiang on 15-3-24.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetAdcolumnListEvent.class)
public class GetAdcolumnListEventHandler implements IHandler<List<AdcolumnListVO>, GetAdcolumnListEvent> {

    AdcolumnDao dao = new AdcolumnDao();

    @Override
    public List<AdcolumnListVO> handle(GetAdcolumnListEvent args) {
        return dao.getAdcolumnListVO(args.getIndex(), args.getCount());
    }
}
