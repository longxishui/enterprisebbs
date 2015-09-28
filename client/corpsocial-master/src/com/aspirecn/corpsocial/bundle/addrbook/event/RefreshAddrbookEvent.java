/**
 * @(#) VeritifyDataEvent.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;


/**
 * 刷新通讯录事件
 *
 * @author lizhuo_a
 */
public class RefreshAddrbookEvent extends BusEvent {
    private int pageSize = 200;

    private String userId;

    private boolean refreshUpdateTime = true;

    private int offset = 0;

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRefreshUpdateTime() {
        return refreshUpdateTime;
    }

    public void setRefreshUpdateTime(boolean refreshUpdateTime) {
        this.refreshUpdateTime = refreshUpdateTime;
    }
}
