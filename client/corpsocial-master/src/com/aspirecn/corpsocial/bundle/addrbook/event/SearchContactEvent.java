/**
 * @(#) VeritifyDataEvent.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;


/**
 * 搜索联系人事件
 *
 * @author meixuesong
 */
public class SearchContactEvent extends BusEvent {
    private String keyword;

    //0 all include same corp and friend, 1 same corp 2 friend
    private int type;

    private String corpId;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
