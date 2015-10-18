package com.aspirecn.corpsocial.bundle.workgrp.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CreateOrModifyBBSEvent extends BusEvent {
    /**
     * 帖子的标题
     */
    private String title;
    /**
     * 帖子的内容
     */
    private String content;
    /**
     * 帖子所在的模块id
     */
    private String groupId;
    /**
     * 帖子的id,修改帖子的时候涉及
     */
    private String itemId;
    /**
     * 是否带附件
     */
    private boolean hasPic;
    /**
     * 附件的本地 绝对路径
     */
    private List<String> listFilePath;

    public CreateOrModifyBBSEvent() {
        super();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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
            jsonData.put("title", title);
            jsonData.put("content", content);
            jsonData.put("groupId", groupId);
            jsonData.put("itemId", itemId);
            jsonData.put("hasPic", hasPic);
        } catch (JSONException e) {
            LogUtil.e("组装bbslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }

}
