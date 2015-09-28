package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by Amos on 15-6-17.
 */
public class FindUserByMobileEvent extends BusEvent {

    private String mobile;
    private boolean misty;

    public FindUserByMobileEvent() {
    }

    public FindUserByMobileEvent(String mobile, boolean misty) {
        this.mobile = mobile;
        this.misty = misty;
    }

    public boolean isMisty() {
        return misty;
    }

    public void setMisty(boolean misty) {
        this.misty = misty;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
