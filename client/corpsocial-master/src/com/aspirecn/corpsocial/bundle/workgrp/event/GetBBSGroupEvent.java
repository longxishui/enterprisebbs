package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GetBBSGroupEvent extends BusEvent {

    private String corpId;

    public GetBBSGroupEvent() {

    }

    public GetBBSGroupEvent(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * 组装的json数据
     */
    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
            jsonData.put("corpId", Config.getInstance().getCorpId());
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", "123");
            jsonData.put("lastModifiedTime", Config.getInstance().getBBSGroupLastModifyTime());
        } catch (JSONException e) {
            LogUtil.e("组装GetBBSGroupEvent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
