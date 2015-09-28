package com.aspirecn.corpsocial.bundle.settings.event;

import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONObject;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class DownloadAdcolumnEvent extends BusEvent {
    private long lastModieTime;
    private String data;

    public long getLastModieTime() {
        return lastModieTime;
    }

    public void setLastModieTime(long lastModieTime) {
        this.lastModieTime = lastModieTime;
    }


    public String getData() {
        JSONObject datajson = new JSONObject();
        try {
            datajson.put("corpId", ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
            datajson.put("userId", Config.getInstance().getUserId());
            datajson.put("veritify", Config.getInstance().getVeritifyCode());
//            datajson.put("data", adcolumnData());
            datajson.put("start", 0);
            datajson.put("limit", 0);
            datajson.put("fromTime", getLastModieTime() + "");
            datajson.put("direction", "0");
        } catch (Exception e) {
            LogUtil.e("jsonError=" + e.getMessage());
        }
        this.data = datajson.toString();
        return data;
    }

    private String adcolumnData() {
        JSONObject datajsonB = new JSONObject();
        try {
            datajsonB.put("start", 0);
            datajsonB.put("limit", 0);
            datajsonB.put("fromTime", getLastModieTime() + "");
            datajsonB.put("direction", "0");
        } catch (Exception e) {
            LogUtil.e("jsonAdcolumnDataError=" + e.getMessage());
        }
        String string = datajsonB.toString();

        return string;
    }


}
