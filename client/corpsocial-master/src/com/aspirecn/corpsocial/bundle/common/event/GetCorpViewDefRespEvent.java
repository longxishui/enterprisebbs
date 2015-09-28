package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.eventbus.BusEvent;

/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 获取配置信息后的通知
 */
public class GetCorpViewDefRespEvent extends BusEvent {

    /**
     * 通用字体颜色
     */
    public int fontColor;

    @Override
    public String toString() {
        return "GetCorpViewDefRespEvent{" +
                "fontColor=" + fontColor +
                '}';
    }
}
