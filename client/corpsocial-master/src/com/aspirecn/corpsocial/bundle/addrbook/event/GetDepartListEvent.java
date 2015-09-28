package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.bundle.addrbook.event.vo.CorpModified;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Amos on 15-6-16.
 */
public class GetDepartListEvent extends BusEvent {

    /**
     * 开始行
     */
    private int start;
    /**
     * 限制行数
     */
    private int limit;

    /**
     * 企业更新时间
     */
    private List<CorpModified> lastModifiedList;

    public GetDepartListEvent() {

    }

    public GetDepartListEvent(int start, int limit, List<CorpModified> lastModifiedList) {
        this.start = start;
        this.limit = limit;
        this.lastModifiedList = lastModifiedList;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<CorpModified> getLastModifiedList() {
        return lastModifiedList;
    }

    public void setLastModifiedList(List<CorpModified> lastModifiedList) {
        this.lastModifiedList = lastModifiedList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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
            JSONArray array = new JSONArray();
            if (lastModifiedList != null) {
                int index = 0;
                for (CorpModified modified : lastModifiedList) {
                    JSONObject object = new JSONObject();
                    object.put("lastModifiedTime", modified.getLastModifiedTime());
                    object.put("corpId", modified.getCorpId());
                    array.put(index++, object);
                }
            }
            jsonData.put("lastModifiedList", array);
        } catch (JSONException e) {
            LogUtil.e("组装addresslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
