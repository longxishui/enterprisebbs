package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 修改个性签名事件
 *
 * @author wangdeng
 */
public class ModifySignatureRespEvent extends BusEvent {
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
