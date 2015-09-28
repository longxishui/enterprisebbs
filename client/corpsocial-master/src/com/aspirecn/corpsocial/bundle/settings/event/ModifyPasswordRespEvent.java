/**
 * @(#) LoginEvent.java Created on 2013-11-18
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 修改密码事件
 *
 * @author lizhuo_a
 */
public class ModifyPasswordRespEvent extends BusEvent {

    private int errorCode;

    private String errorInfo;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

}
