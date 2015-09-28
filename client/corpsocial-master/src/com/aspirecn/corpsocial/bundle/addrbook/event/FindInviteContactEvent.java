package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amos on 15-6-24.
 */
public class FindInviteContactEvent extends BusEvent {

    private String queryKey;

    public FindInviteContactEvent() {

    }

    public FindInviteContactEvent(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
            jsonData.put("corpId", ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", Config.getInstance().getVeritifyCode());
            /** 请求参数 */
            jsonData.put("queryKey", queryKey);
            jsonData.put("keyType", "1");
        } catch (JSONException e) {
            LogUtil.e("组装FindInviteContactEvent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
