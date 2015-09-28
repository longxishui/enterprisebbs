package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GetBBSListEvent extends BusEvent {
    /**
     * 获取帖子的起始位置
     */
    private int startPos;
    /**
     * 获取帖子的条数
     */
    private int limit;
    /**
     * 获取帖子集合的模块栏目id
     */
    private String groupId = "-1";
    /**
     * 本地最后一条帖子的创建时间
     */
    private long lastCreateTime;
    /**
     * 本地最后的帖子修改时间
     */
    private long lastModifyTime;
    /**
     * 是否是与我相关
     */
    private boolean isConcernMe = false;

    private String corpId;

    public GetBBSListEvent() {
        super();
    }

    public GetBBSListEvent(int startPos, int limit, String groupId, int lastCreateTime, int lastModifyTime) {
        super();
        this.startPos = startPos;
        this.limit = limit;
        this.groupId = groupId;
        this.lastCreateTime = lastCreateTime;
        this.lastModifyTime = lastModifyTime;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public long getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(long lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isConcernMe() {
        return isConcernMe;
    }

    public void setConcernMe(boolean isConcernMe) {
        this.isConcernMe = isConcernMe;
    }

    @Override
    public String toString() {
        return "GetBBSListEvent [startPos=" + startPos + ", limit=" + limit
                + ", groupId=" + groupId + "]";
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
            jsonData.put("startPos", startPos);
            jsonData.put("limit", limit);
            jsonData.put("lastModifiedTime", lastModifyTime);
//            jsonData.put("direction",direction);
            jsonData.put("createTime", lastCreateTime);
            jsonData.put("concernMe", isConcernMe);
            jsonData.put("groupId", groupId);
        } catch (JSONException e) {
            LogUtil.e("组装bbslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }

}
