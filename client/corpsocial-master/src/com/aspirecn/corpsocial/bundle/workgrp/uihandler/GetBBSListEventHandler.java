package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSListEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSListRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@UIEventHandler(eventType = GetBBSListEvent.class)
public class GetBBSListEventHandler implements IHandler<Null, GetBBSListEvent> {

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final GetBBSListEvent getBBSListEvent) {
        if (getBBSListEvent.getCorpId() == null) {
            getBBSListEvent.setCorpId(ConfigPersonal.getString(ConfigPersonal.Key.CORP_ID_SELECTED));
        }
        LogUtil.i("请求的json数据为：" + getBBSListEvent.getJson());
        HttpRequest.request(WorkgrpConfig.GET_BBS_LIST, getBBSListEvent.getJson(), new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode,String message) {
                GetBBSListRespEvent getBBSListRespEvent;
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    LogUtil.e("获得同事圈数据为：" + message);
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        getBBSListRespEvent = handleRespData(httpMessage.data, getBBSListEvent.getCorpId());
                        getBBSListRespEvent.setGroupId(getBBSListEvent.getGroupId());
                        instance.notifyListener(getBBSListRespEvent);
                    } else {
                        getBBSListRespEvent = new GetBBSListRespEvent();
                        getBBSListRespEvent.setErrCode(ErrorCode.NETWORK_FAILED.getValue());
                        getBBSListRespEvent.setGroupId(getBBSListEvent.getGroupId());
                        netFaiNotify(getBBSListRespEvent);
                    }
                } else {
                    getBBSListRespEvent = new GetBBSListRespEvent();
                    getBBSListRespEvent.setErrCode(errorCode);
                    getBBSListRespEvent.setGroupId(getBBSListEvent.getGroupId());
                    netFaiNotify(getBBSListRespEvent);
                }
            }
        });
        return new Null();
    }

    private void netFaiNotify(GetBBSListRespEvent getBBSListRespEvent) {
        LogUtil.e("同步同事圈失败");
        instance.notifyListener(getBBSListRespEvent);
    }

    private GetBBSListRespEvent handleRespData(String jsonResult, String corpId) {
        GetBBSListRespEvent getBBSListRespEvent = new GetBBSListRespEvent();
        try {
            JSONObject jsonData = new JSONObject(jsonResult);
            JSONArray jsonListItem = jsonData.getJSONArray("list");
            ArrayList<BBSItem> mBbsItems = new ArrayList<BBSItem>();
            BBSItemEntity bbsItemEntity;
            BBSItemDao itemDao = new BBSItemDao();
            int itemsLen = jsonListItem.length();
            if (itemsLen > 0) {
                getBBSListRespEvent.setErrCode(ErrorCode.SUCCESS.getValue());
            } else {
                getBBSListRespEvent.setErrCode(ErrorCode.NETWORK_FAILED.getValue());
            }
            for (int i = 0; i < itemsLen; i++) {
                JSONObject jsonItem = jsonListItem.getJSONObject(i);
               String jsonItemString = jsonListItem.getString(i);
                BBSItem mBbsItem = new BBSItem();
                    bbsItemEntity = new Gson().fromJson(jsonItemString,BBSItemEntity.class);
                bbsItemEntity.setFileInfo(new Gson().toJson(bbsItemEntity.getFileInfoData()));
                bbsItemEntity.setUserid(Config.getInstance().getUserId());
                bbsItemEntity.setCorpId(Config.getInstance().getCorpId());
                if(bbsItemEntity.getStatus().equals("0")){
                    JSONObject jsonDetail = jsonItem.getJSONObject("detail");
                    JSONArray jsonPraiseList = jsonDetail.getJSONArray("praiseInfos");
                    ArrayList<String> praiseList = new ArrayList<String>();
                    StringBuffer sb = new StringBuffer();
                    int len = jsonPraiseList.length();
                    for (int m = 0; m < len; m++) {
                        JSONObject jsonPraiseItem = jsonPraiseList.getJSONObject(m);
                        String praiseId = jsonPraiseItem.getString("userid");
                        sb.append(praiseId);
                        praiseList.add(praiseId);
                        if (m != (len - 1)) {
                            sb.append("-");
                        }
                    }
                    bbsItemEntity.setPraiseUserIds(sb.toString());
                    mBbsItem.setBbsItemEntity(bbsItemEntity);
                    mBbsItems.add(mBbsItem);
                    itemDao.insertEntity(bbsItemEntity);
                } else {
                    itemDao.deteleById(bbsItemEntity.getId());
                }
            }
            getBBSListRespEvent.setBbsItems(mBbsItems);
        } catch (Exception e) {
            LogUtil.i("获取列表解析失败");
            e.printStackTrace();
        }
        return getBBSListRespEvent;
    }
}
