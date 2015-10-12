package com.aspirecn.corpsocial.bundle.common.uihandler;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.event.GetDepartListEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.CorpModified;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.event.CorpChangeEvent;
import com.aspirecn.corpsocial.bundle.common.event.CorpChangeRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListEvent;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amos on 15-7-14.
 */
@EventBusAnnotation.UIEventHandler(eventType = CorpChangeEvent.class)
public class CorpChangeEventHandler extends EventHandler implements IHandler<Null, CorpChangeEvent> {


    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    @Override
    public Null handle(final CorpChangeEvent corpChangeEvent) {
        LogUtil.i("请求的json数据为：" + corpChangeEvent.getJson());

//        HttpRequest.request("GET_SELF_CORP_LIST", corpChangeEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                CorpChangeRespEvent corpChangeRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得用户企业数据为：" + result.getMessage());
//                    corpChangeRespEvent = handleRespData(result.getMessage());
//                    instance.notifyListener(corpChangeRespEvent);
//                } else {
//                    corpChangeRespEvent = new CorpChangeRespEvent();
//                    corpChangeRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                    LogUtil.e("获得用户企业数据失败");
//                    netFaiNotify(corpChangeRespEvent);
//                }
//            }
//        });
        return new Null();
    }

    private CorpChangeRespEvent handleRespData(String jsonResult) {
        CorpChangeRespEvent corpChangeRespEvent = new CorpChangeRespEvent();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResult);
            //JSONObject jsonData = jsonRoot.getJSONObject("data");
            if (jsonRoot.has("data")) {
                JSONArray jsonListItem = jsonRoot.getJSONArray("data");
                ArrayList<UserCorp> userItems = new ArrayList<UserCorp>();

                JSONObject jsonItem = null;

                int itemsLen = jsonListItem.length();
                corpChangeRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());

                for (int i = 0; i < itemsLen; i++) {
                    jsonItem = jsonListItem.getJSONObject(i);

                    UserCorp userItem = (new Gson()).fromJson(jsonItem.toString(), UserCorp.class);
                    userItems.add(userItem);
                }

                ConfigPersonal.getInstance().setMutiCorp(userItems.size() > 1 ? 1 : 0);


                String corpId = ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED);
                boolean isExist = false;
                for (UserCorp userCorp : userItems) {
                    // 判断是否存在
                    if (userCorp.getCorpId().equals(corpId)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    ConfigPersonal.putString(ConfigPersonal.Key.CORP_ID_SELECTED, userItems.get(0).getCorpId());
                }

                GetSelfCorpListRespBean.createOrUpdate(userItems);


                List<CorpModified> cms = new ArrayList();

                for (UserCorp uc : userItems) {
                    if (uc.getStatus().equals("0")) {
                        UserCorp entity = GetSelfCorpListRespBean.findByCorpId(uc.getCorpId());
                        CorpModified cm = new CorpModified();
                        cm.setCorpId(entity.getCorpId());
                        cm.setLastModifiedTime(entity.getLastModifiedTime());
                        cms.add(cm);
                        //获取配置
                        UiEventHandleFacade.getInstance().handle(new GetCorpViewDefEvent(entity.getCorpId()));
                    }
                }
                if (cms.size() > 0) {
                    UiEventHandleFacade.getInstance().handle(new GetDepartListEvent(0, 1000, cms));
                }

                notifyImModuleSyncCorpGroup();

            }
        } catch (Exception e) {
            LogUtil.e("获取列表解析失败", e);
            //e.printStackTrace();
            corpChangeRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return corpChangeRespEvent;
    }


    /**
     * 通知同步公司群组列表
     */
    private void notifyImModuleSyncCorpGroup() {
        Intent loginStartIntent = new Intent("imNotifyReceiver");
        Bundle loginStartIntentBundle = new Bundle();
        loginStartIntentBundle.putString("action", "syncCorpGroup");
        loginStartIntent.putExtras(loginStartIntentBundle);

        appBroadcastManager.sendBroadcast(loginStartIntent);
    }
}
