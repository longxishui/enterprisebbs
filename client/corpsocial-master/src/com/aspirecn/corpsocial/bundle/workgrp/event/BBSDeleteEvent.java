package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class BBSDeleteEvent extends BusEvent {

    /**
     * 删除的是帖子类型为DeleteType.ITEM 删除的是评论类型为DeleteType.REPLY
     */
    private DeleteType deleteType;
    /**
     * 帖子的id
     */
    private String itemId;
    /**
     * 评论的id
     */
    private String replyId;
    /**
     * 模块栏目的id
     */
    private String groupId;

    public BBSDeleteEvent() {
        super();
    }

    public BBSDeleteEvent(DeleteType replyType, String itemId, String replyId, String groupId
    ) {
        super();
        this.deleteType = replyType;
        this.itemId = itemId;
        this.replyId = replyId;
        this.groupId = groupId;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public DeleteType getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(DeleteType deleteType) {
        this.deleteType = deleteType;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
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
            jsonData.put("itemId", itemId);
            jsonData.put("deleteType", deleteType == DeleteType.ITEM ? 1 : 2);
            jsonData.put("replyId", replyId);
        } catch (JSONException e) {
            LogUtil.e("组装jsonObject失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
