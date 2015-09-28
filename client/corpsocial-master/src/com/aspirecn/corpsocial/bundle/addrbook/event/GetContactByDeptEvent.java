package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

public class GetContactByDeptEvent extends BusEvent {
    private String deptId;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
