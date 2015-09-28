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
public class FindContactEvent extends BusEvent {

    private String queryKey;
    //1 by id , others by username
    private String keyType;

    public FindContactEvent() {

    }
    public FindContactEvent(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
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
            jsonData.put("keyType", keyType);
        } catch (JSONException e) {
            LogUtil.e("组装findcontactevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
