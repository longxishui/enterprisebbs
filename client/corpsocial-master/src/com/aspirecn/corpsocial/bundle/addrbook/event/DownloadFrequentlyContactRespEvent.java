/**
 * @(#) VeritifyDataEvent.java Created on 2013-11-26
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;


/**
 * 从服务端下载常用联系人响应事件
 *
 * @author meixuesong
 */
public class DownloadFrequentlyContactRespEvent extends BusEvent {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
