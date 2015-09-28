package com.aspirecn.corpsocial.bundle.addrbook.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amos on 15-7-1.
 */
public class FindMyEvent extends BusEvent {

    private String userid;

    //1 by id , others by username
    private String keyType = "1";

    public FindMyEvent() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public FindMyEvent(String userid) {
        this.userid = userid;
    }


    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
            jsonData.put("corpId", ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", Config.getInstance().getVeritifyCode());
            /** 请求参数 */
            jsonData.put("queryKey", userid);
            jsonData.put("keyType", keyType);
        } catch (JSONException e) {
            LogUtil.e("组装FindMyEvent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
