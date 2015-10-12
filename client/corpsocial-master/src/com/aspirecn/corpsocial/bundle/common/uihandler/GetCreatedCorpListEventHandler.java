package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.domain.Corp;
import com.aspirecn.corpsocial.bundle.common.event.GetCreatedCorpListEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetCreatedCorpListRespEvent;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetCreatedCorpListEvent.class)
public class GetCreatedCorpListEventHandler extends EventHandler implements IHandler<Null, GetCreatedCorpListEvent> {

    @Override
    public Null handle(final GetCreatedCorpListEvent getCreatedCorpListEvent) {
        LogUtil.i("请求的json数据为：" + getCreatedCorpListEvent.getJson());
//        HttpRequest.request("GET_CREATED_CORP_LIST", getCreatedCorpListEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                GetCreatedCorpListRespEvent getCreatedCorpListRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得通讯录数据为：" + result.getMessage());
//                    getCreatedCorpListRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(getCreatedCorpListRespEvent);
//                } else {
//                    getCreatedCorpListRespEvent = new GetCreatedCorpListRespEvent();
//                    getCreatedCorpListRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("同步通讯录失败");
//                    netFaiNotify(getCreatedCorpListRespEvent);
//                }
//            }
//        });
        return new Null();
    }

    private GetCreatedCorpListRespEvent handleRespData(String jsonResult) {
        GetCreatedCorpListRespEvent getCreatedCorpListRespEvent = new GetCreatedCorpListRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray jsonListItem = jsonRoot.getJSONArray("data");
            ArrayList<Corp> userItems = new ArrayList<Corp>();

            JSONObject jsonItem = null;

            int itemsLen = jsonListItem.length();
            if (itemsLen > 0) {
                getCreatedCorpListRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
            } else {
                getCreatedCorpListRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
            }
            for (int i = 0; i < itemsLen; i++) {
                jsonItem = jsonListItem.getJSONObject(i);

                Corp corpItem = (new Gson()).fromJson(jsonItem.toString(), Corp.class);
                userItems.add(corpItem);
            }
            getCreatedCorpListRespEvent.setCorpList(userItems);
        } catch (Exception e) {
            LogUtil.i("获取列表解析失败");
            e.printStackTrace();
        }
        return getCreatedCorpListRespEvent;
    }

}
