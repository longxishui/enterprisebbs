package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amos on 15-6-16.
 */
public class GetFriendListEvent extends BusEvent {

    /**
     * 开始行
     */
    private int start;
    /**
     * 限制行数
     */
    private int limit;

    private long lastModifiedTime;

    private long lastFriendTime;

    public GetFriendListEvent() {

    }

    public GetFriendListEvent(int start, int limit, long lastModifiedTime, long lastFriendTime) {
        this.start = start;
        this.limit = limit;
        this.lastModifiedTime = lastModifiedTime;
        this.lastFriendTime = lastFriendTime;
    }

    public long getLastFriendTime() {
        return lastFriendTime;
    }

    public void setLastFriendTime(long lastFriendTime) {
        this.lastFriendTime = lastFriendTime;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
            jsonData.put("corpId", ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", Config.getInstance().getVeritifyCode());
            /** 请求参数 */
            jsonData.put("start", start);
            jsonData.put("limit", limit);
            jsonData.put("lastModifiedTime", lastModifiedTime);
            jsonData.put("lastFriendTime", lastFriendTime);
        } catch (JSONException e) {
            LogUtil.e("组装addresslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
