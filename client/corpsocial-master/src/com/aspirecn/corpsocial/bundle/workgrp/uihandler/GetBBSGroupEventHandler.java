package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.net.HttpRequest;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSGroupDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSGroupEntity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@UIEventHandler(eventType = GetBBSGroupEvent.class)
public class GetBBSGroupEventHandler implements IHandler<Null, GetBBSGroupEvent> {

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final GetBBSGroupEvent getGroupListEvent) {
        LogUtil.i("bbsGroupHandler开始请求");
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
                    getBBSGroupRespEvent.setErrcode(errorCode);
                }
                instance.notifyListener(getBBSGroupRespEvent);
            }
        });

        return new Null();
    }

    private GetBBSGroupRespEvent handleRespData(String jsonString, String corpId) {
        GetBBSGroupRespEvent bbsGroupRespEvent = new GetBBSGroupRespEvent();
        bbsGroupRespEvent.setChange(false);
        LogUtil.i("获取bbsgroup:" + jsonString);
        try {
            JSONObject jsonRoot = new JSONObject(jsonString);
            JSONArray jsonGroupList = jsonRoot.getJSONArray("list");
            ArrayList<BBSGroupEntity> mBbsGroups = new ArrayList<BBSGroupEntity>();
            BBSGroupDao bbsGroupDao = new BBSGroupDao();
            BBSGroupEntity entity = null;
            bbsGroupDao.clearAllData(corpId);

            for (int j = 0; j < jsonGroupList.length(); j++) {
                String jsonGroupItem = jsonGroupList.getString(j);
                entity = new Gson().fromJson(jsonGroupItem,BBSGroupEntity.class);
                bbsGroupDao.insertEntity(entity);
                mBbsGroups.add(entity);
            }
            bbsGroupRespEvent.setBbsGroups(mBbsGroups);
            bbsGroupRespEvent.setChange(true);
            bbsGroupRespEvent.setErrcode(ErrorCode.SUCCESS.getValue());
        } catch (Exception e) {
            bbsGroupRespEvent.setChange(false);
            e.printStackTrace();
        }
        return bbsGroupRespEvent;
    }

}
