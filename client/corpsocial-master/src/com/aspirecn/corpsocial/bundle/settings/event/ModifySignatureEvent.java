package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 修改个性签名
 *
 * @author wangdeng
 */
public class ModifySignatureEvent extends BusEvent {

    /**
     * 修改签名
     */
    private String Signature;

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        this.Signature = signature;
    }
}
