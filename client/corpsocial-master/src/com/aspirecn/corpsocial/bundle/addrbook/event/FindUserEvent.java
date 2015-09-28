package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-17.
 */
public class FindUserEvent extends BusEvent {

    private BusEvent event;

    public FindUserEvent() {
    }

    public FindUserEvent(BusEvent event) {
        this.event = event;
    }

    public BusEvent getEvent() {
        return event;
    }

    public void setEvent(BusEvent event) {
        this.event = event;
    }
}
