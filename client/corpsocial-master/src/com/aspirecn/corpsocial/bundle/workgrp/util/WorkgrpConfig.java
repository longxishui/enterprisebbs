package com.aspirecn.corpsocial.bundle.workgrp.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by duyinzhou on 2015/5/18.
 */
public class WorkgrpConfig {
    public static final String GET_BBS_LIST = "GET_BBS_LIST";
    public static final String GET_BBS_GROUP = "GET_BBS_GROUP";
    public static final String CREATE_OR_MODIFY_ITEM = "CREATE_OR_MODIFY_ITEM";
    public static final String BBS_DELETE = "BBS_DELETE";
    public static final String GET_BBS_DETAIL = "GET_BBS_DETAIL";
    public static final String BBS_REPLY = "BBS_REPLY";


    public static HttpMessage getHttpMessage(String jsonData) {
        HttpMessage httpMessage = new HttpMessage();
        Log.e("WorkgrpConfig","获取到的json为："+jsonData);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String rst = jsonObject.getString("rst");
            if (rst.equals("0") || rst.equals("true")) {
                httpMessage.data = jsonObject.getString("data");
                httpMessage.httpResult = 0;
            } else {
                httpMessage.httpResult = 1;
            }
            httpMessage.errorMessage = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return httpMessage;
    }
}
