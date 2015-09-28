package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * 修改头像事件
 *
 * @author liangjian
 */
public class ModifyHeadImgEvent extends BusEvent {

    /**
     * 头像地址
     */
    private String headImageUrl;

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

}
