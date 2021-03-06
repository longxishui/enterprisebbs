package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.net.HttpRequest;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSGroupDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.BBSGroupNet;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

@UIEventHandler(eventType = GetBBSGroupEvent.class)
public class GetBBSGroupEventHandler implements IHandler<Null, GetBBSGroupEvent> {

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final GetBBSGroupEvent getGroupListEvent) {
        LogUtil.i("bbsGroupHandler开始请求数据："+getGroupListEvent.getJson());
        if (getGroupListEvent.getCorpId() == null) {
            getGroupListEvent.setCorpId(ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
        }
        HttpRequest.request(WorkgrpConfig.GET_BBS_GROUP, getGroupListEvent.getJson(), new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode, String message) {
                GetBBSGroupRespEvent getBBSGroupRespEvent = new GetBBSGroupRespEvent();
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    LogUtil.i("获得同事圈数据为：" + message);
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        getBBSGroupRespEvent = handleRespData(httpMessage.data, getGroupListEvent.getCorpId());
                        getBBSGroupRespEvent.setErrcode(errorCode);
                    } else {
                        getBBSGroupRespEvent.setErrcode(ErrorCode.NETWORK_FAILED.getValue());
                    }
                } else {
                    getBBSGroupRespEvent.setChange(false);
                    getBBSGroupRespEvent.setErrcode(errorCode);
                }
                instance.notifyListener(getBBSGroupRespEvent);
            }
        });

        return new Null();
    }

    private GetBBSGroupRespEvent handleRespData(String jsonString, String corpId) {
        GetBBSGroupRespEvent bbsGroupRespEvent = new GetBBSGroupRespEvent();
        LogUtil.i("获取bbsgroup:" + jsonString);
        BBSGroupNet bbsGroupNet = new Gson().fromJson(jsonString,BBSGroupNet.class);
        BBSGroupDao bbsGroupDao = new BBSGroupDao();
        Config.getInstance().setBBSGroupLastModifyTime(bbsGroupNet.lastModifiedTime);
        if(bbsGroupNet.list.size()==0){
            bbsGroupRespEvent.setChange(false);
            bbsGroupRespEvent.setErrcode(ErrorCode.SUCCESS.getValue());
        }else {
            for (BBSGroupEntity bbsGroupEntity : bbsGroupNet.list) {
                bbsGroupDao.insertEntity(bbsGroupEntity);
            }
            bbsGroupRespEvent.setBbsGroups(bbsGroupNet.list);
            bbsGroupRespEvent.setChange(true);
            bbsGroupRespEvent.setErrcode(ErrorCode.SUCCESS.getValue());
        }
        return bbsGroupRespEvent;
    }
}
