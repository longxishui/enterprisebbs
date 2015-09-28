package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetCustomerServiceDataEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.CustomerServiceDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.FrequentlyContactVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.List;

/**
 * Created by chenziqiang on 15-1-15.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetCustomerServiceDataEvent.class)
public class GetCustomerServiceDataEventHandler implements
        IHandler<List<FrequentlyContactVO>, GetCustomerServiceDataEvent> {
    private CustomerServiceDao dao = new CustomerServiceDao();

    @Override
    public List<FrequentlyContactVO> handle(GetCustomerServiceDataEvent args) {
        return dao.findCustomerService(Config.getInstance().getUserId());
    }
}
