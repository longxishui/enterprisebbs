package com.aspirecn.corpsocial.bundle.common.uihandler;

import android.content.Intent;
import android.os.Bundle;

import com.aspirecn.corpsocial.bundle.addrbook.utils.AddrbookConfig;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListEvent;
import com.aspirecn.corpsocial.bundle.common.event.GetSelfCorpListRespEvent;
import com.aspirecn.corpsocial.bundle.common.event.QuitEvent;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-17.
 */
@EventBusAnnotation.UIEventHandler(eventType = GetSelfCorpListEvent.class)
public class GetSelfCorpListEventHandler extends EventHandler implements IHandler<Null, GetSelfCorpListEvent> {

    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();


    @Override
    public Null handle(final GetSelfCorpListEvent getSelfCorpListEvent) {
        if (AddrbookConfig.setWorking()) {
            processTask(getSelfCorpListEvent);
        }

        return new Null();
    }

    public void processTask(final GetSelfCorpListEvent getSelfCorpListEvent) {

        LogUtil.i("请求的json数据为：" + getSelfCorpListEvent.getJson());
//        HttpRequest.request("GET_SELF_CORP_LIST", getSelfCorpListEvent.getJson(), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                GetSelfCorpListRespEvent getSelfCorpListRespEvent = null;
//                if (result != null && result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    LogUtil.d("获得用户企业数据为：" + result.getMessage());
//                    getSelfCorpListRespEvent = handleResp(result.getMessage());
//
//                    instance.notifyListener(getSelfCorpListRespEvent);
//                } else {
//                    AddrbookConfig.reset();
//                    if (ConfigPersonal.getInt(ConfigPersonal.Key.CORP_STATUS) == 0) {//企业新奇从来没有下载成功过
//
//                        UiEventHandleFacade.getInstance().handle(new QuitEvent());
//                    } else {
//                        getSelfCorpListRespEvent = new GetSelfCorpListRespEvent();
//                        getSelfCorpListRespEvent.setErrorCode(result == null ? ErrorCode.NETWORK_FAILED.getValue() : result.getErrorCode());
//                        LogUtil.e("获得用户企业数据失败");
//
//                        netFaiNotify(getSelfCorpListRespEvent);
//                    }
//                }
//            }
//        });


    }
    private GetSelfCorpListRespEvent handleResp(String jsonResult){
        GetSelfCorpListRespEvent getSelfCorpListRespEvent = new GetSelfCorpListRespEvent();
        LogUtil.e("获取到的企业列表数据："+jsonResult);
        GetSelfCorpListRespBean respBean = new Gson().fromJson(jsonResult, GetSelfCorpListRespBean.class);
        if(respBean.rst==0){
            List<UserCorp> userCorpList = respBean.data;
            if(ConfigPersonal.getString(ConfigPersonal.Key.GETLISTCORP_KEY)==null) {
                ConfigPersonal.putString(ConfigPersonal.Key.GETLISTCORP_KEY, new Gson().toJson(respBean));
            }
            int muticount = 0;

            boolean hascorp = false;
            for (UserCorp userCorp : userCorpList) {
                if ("0".equals(userCorp.getStatus())) {
                    muticount++;
                    hascorp = true;
                    UiEventHandleFacade.getInstance().handle(new GetCorpViewDefEvent(userCorp.getCorpId()));
                }
            }
            ConfigPersonal.putInt(ConfigPersonal.Key.CORP_STATUS,1);
            ConfigPersonal.getInstance().setMutiCorp(muticount > 1 ? 1 : 0);
            getSelfCorpListRespEvent.setCorpList(userCorpList);
            if (hascorp) {
                notifyImModuleSyncCorpGroup();
                runNextevents(GetSelfCorpListEvent.class.getSimpleName());
            } else {
                UiEventHandleFacade.getInstance().handle(new QuitEvent());
            }
        }else{
            LogUtil.e("获取列表:respBean.message");
            //e.printStackTrace();
            getSelfCorpListRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
        }
        return getSelfCorpListRespEvent;
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
