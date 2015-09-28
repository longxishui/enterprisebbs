package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FetchFrequentlyContactsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.utils.UserNameSort;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 获取常用联系人事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = FetchFrequentlyContactsEvent.class)
public class FetchFrequentlyContactsEventHandler implements
        IHandler<List<User>, FetchFrequentlyContactsEvent> {

    @EventBusAnnotation.Component
    private UserDao dao;

    @Override
    public List<User> handle(FetchFrequentlyContactsEvent busEvent) {
        List<User> users=dao.findFrequent(null);
        List<User> susers=dao.merge(users);
        Comparator c=new UserNameSort();
        Collections.sort(susers, c);

        return susers;
//        List<FrequentlyContactVO> list = getFrequentlyContacts(Config.getInstance().getUserId());
//
//        return list;
    }


}
