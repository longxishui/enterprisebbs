package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.util.DetailType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GetBBSDetailEvent extends BusEvent {
    /**
     * 帖子的id
     */
    private String bbsId;
    /**
     * 评论的获取起始位置,点赞不涉及
     */
    private int startPos;
    /**
     * 获取评论的条数,点赞不涉及
     */
    private int limit;
    /**
     * 获取的类型,如果是DetailType.REPLY则是评论详情，DetailType.PRAISE则是点赞详情.如果是DetailType.ALL,则两者都获取
     */
    private DetailType detailType;

    public GetBBSDetailEvent() {
        super();
    }

    public GetBBSDetailEvent(String bbsId, int startPos, int limit,
                             DetailType detailType) {
        super();
        this.bbsId = bbsId;
        this.startPos = startPos;
        this.limit = limit;
        this.detailType = detailType;
    }

    public String getBbsId() {
        return bbsId;
    }

    public void setBbsId(String bbsId) {
        this.bbsId = bbsId;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public DetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(DetailType detailType) {
        this.detailType = detailType;
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
            /** 请求参数 */
            jsonData.put("itemId", bbsId);
            jsonData.put("startPos", startPos);
            jsonData.put("limit", limit);
            if (detailType == DetailType.REPLY) {
                jsonData.put("detailType", 1);
            } else if (detailType == DetailType.PRAISE) {
                jsonData.put("detailType", 2);
            } else {
                jsonData.put("detailType", 3);
            }
        } catch (JSONException e) {
            LogUtil.e("组装jsonObject失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }

}
