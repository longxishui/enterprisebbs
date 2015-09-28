package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chenbin on 2015/7/25.
 */
public class FindGroupMemberContactEvent extends BusEvent {

    /**
     * 企业ID
     */
    public String corpId;

    /**
     * 人员ID列表
     */
    public List<String> userIds;

    public FindGroupMemberContactEvent() {

    }

    public FindGroupMemberContactEvent(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
            jsonData.put("corpId", Config.getInstance().getCorpId());
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", Config.getInstance().getVeritifyCode());
            /** 请求参数 */
            JSONArray array = new JSONArray();
            for (String userid : userIds) {
                array.put(userid);
            }
            jsonData.put("userIdList", array);

        } catch (JSONException e) {
            LogUtil.e("组装findcontactevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
