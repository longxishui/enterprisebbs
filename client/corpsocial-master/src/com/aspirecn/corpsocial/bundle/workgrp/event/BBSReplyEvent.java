package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.bundle.workgrp.util.ReplyType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BBSReplyEvent extends BusEvent {
    /**
     * 帖子的回应，如果是回复则类型为ReplyType.REPLY，如果是点赞则为ReplyType.PARISE
     */
    private ReplyType replyType;
    /**
     * 点赞或回复对应的帖子id
     */
    private String itemId;
    /**
     * 点赞时为空，评论时的评论内容
     */
    private String content;
    /**
     * 是否有附件
     */
    private boolean hasPic;
    /**
     * 附件在本地的绝对路径，上传时需使用
     */
    private List<String> listFilePath;
    /**
     * 回复或点赞对应 的帖子的模块id
     */
    private String groupId;

    public BBSReplyEvent() {
        super();
    }

    public ReplyType getReplyType() {
        return replyType;
    }

    public void setReplyType(ReplyType replyType) {
        this.replyType = replyType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
    }

    public List<String> getListFilePath() {
        return listFilePath;
    }

    public void setListFilePath(List<String> listFilePath) {
        this.listFilePath = listFilePath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
            jsonData.put("content", content);
            jsonData.put("hasPic", hasPic);
            jsonData.put("replyType", replyType == ReplyType.REPLY ? 1 : 2);
            jsonData.put("replyId", Config.getInstance().getUserId());
        } catch (JSONException e) {
            LogUtil.e("组装bbslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }

}
