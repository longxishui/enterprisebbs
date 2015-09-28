package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.SearchContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索联系人事件处理类
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = SearchContactEvent.class)
public class SearchContactEventHandler implements
        IHandler<List<ContactBriefVO>, SearchContactEvent> {
//    private ContactDao dao = new ContactDao();

    @Override
    public List<ContactBriefVO> handle(SearchContactEvent busEvent) {
//        return dao.findByKeyWord(Config.getInstance().getUserId(), busEvent.getKeyword());
        return new ArrayList<ContactBriefVO>();
    }

}
