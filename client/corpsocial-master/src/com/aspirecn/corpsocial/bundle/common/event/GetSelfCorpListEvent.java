package com.aspirecn.corpsocial.bundle.common.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amos on 15-6-17.
 */
@EventBusAnnotation.SingleTask
public class GetSelfCorpListEvent extends BusEvent {

    public String getJson() {
        JSONObject jsonData = new JSONObject();
        try {
            /** 通用消息头 */
//            jsonData.put("corpId", ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
            jsonData.put("userId", Config.getInstance().getUserId());
            jsonData.put("veritify", Config.getInstance().getVeritifyCode());


        } catch (JSONException e) {
            LogUtil.e("组装addresslistevent失败");
            e.printStackTrace();
        }
        return jsonData.toString();
    }
}
