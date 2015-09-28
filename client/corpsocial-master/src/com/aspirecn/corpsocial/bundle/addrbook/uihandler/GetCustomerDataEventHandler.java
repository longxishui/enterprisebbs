package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetCustomerDataEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.CustomerServiceDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.CustomerServiceEntity;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;


/**
 * Created by chenziqiang on 15-1-15.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetCustomerDataEvent.class)
public class GetCustomerDataEventHandler implements
        IHandler<CustomerServiceEntity, GetCustomerDataEvent> {
    private CustomerServiceDao dao = new CustomerServiceDao();

    @Override
    public CustomerServiceEntity handle(GetCustomerDataEvent args) {
        return dao.findById(args.getId());
    }
}
