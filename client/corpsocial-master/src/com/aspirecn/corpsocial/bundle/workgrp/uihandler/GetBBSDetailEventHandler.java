package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import android.util.Log;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.net.HttpRequest;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSReplyInfoDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@UIEventHandler(eventType = GetBBSDetailEvent.class)
public class GetBBSDetailEventHandler implements
        IHandler<Null, GetBBSDetailEvent> {


    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final GetBBSDetailEvent args) {
        LogUtil.i("同事圈详情请求json：" + args.getJson());
        HttpRequest.request(WorkgrpConfig.GET_BBS_DETAIL, args.getJson(), new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode, String message) {
                GetBBSDetailRespEvent respEvent;
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        respEvent = handleRespData(httpMessage.data, args.getBbsId());
                        respEvent.setBbsId(args.getBbsId());
                        instance.notifyListener(respEvent);
                    } else {
                        respEvent = new GetBBSDetailRespEvent();
                        respEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
                        respEvent.setBbsId(args.getBbsId());
                        instance.notifyListener(respEvent);
                    }
                } else {
                    respEvent = new GetBBSDetailRespEvent();
                    respEvent.setErrorCode(errorCode);
                    respEvent.setBbsId(args.getBbsId());
                    instance.notifyListener(respEvent);
                }
            }
        });

        return null;
    }

    private GetBBSDetailRespEvent handleRespData(String resultData,
                                                 String bbsItemId) {
        GetBBSDetailRespEvent respEvent = new GetBBSDetailRespEvent();
        BBSItemDao itemDao = new BBSItemDao();
        BBSItemEntity bbsItemEntity = new Gson().fromJson(resultData,BBSItemEntity.class);
        bbsItemEntity.setFileInfoString(new Gson().toJson(bbsItemEntity.getFileInfo()));
        bbsItemEntity.setUserid(Config.getInstance().getUserId());
        bbsItemEntity.setGroupId(Config.getInstance().getCorpId());
        try {
            JSONObject jsonItem = new JSONObject(resultData);
            BBSReplyInfoDao replyInfoDao = new BBSReplyInfoDao();
            List<BBSReplyInfoEntity> listBBSReplyInfoEntities = replyInfoDao
                    .findByBBSId(bbsItemId);
            replyInfoDao.detele(listBBSReplyInfoEntities);
            if (jsonItem.get("status").equals("0")) {
                respEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
//                itemDao.deteleById(bbsItemEntity.getId());
//                BBSItemEntity dbBBSItemEntity = itemDao.findById(bbsItemEntity.getId());

//                itemDao.insert(bbsItemEntity);
//                itemDao.insertEntity(bbsItemEntity);
                JSONArray jsonReplyList = jsonItem.getJSONArray("bbsReplyInfoDatas");
                List<BBSReplyInfoEntity> bbsReplyInfoEntityList = new ArrayList<BBSReplyInfoEntity>();
                for (int j = 0; j < jsonReplyList.length(); j++) {
                    String jsonReplyItemString = jsonReplyList.getString(j);
                    BBSReplyInfoEntity bbsReplyInfoEntity = new Gson().fromJson(jsonReplyItemString,BBSReplyInfoEntity.class);
                    bbsReplyInfoEntity.setFileInfoString(new Gson().toJson(bbsReplyInfoEntity.getFileInfo()));
                    bbsReplyInfoEntity.setCorpId(Config.getInstance().getCorpId());
                    bbsReplyInfoEntity.setUserid(Config.getInstance().getUserId());
                    bbsReplyInfoEntity.setItemId(bbsItemEntity.getItemId());
                    replyInfoDao.insertEntity(bbsReplyInfoEntity);
                    bbsReplyInfoEntityList.add(bbsReplyInfoEntity);
                }
                respEvent.setBbsReplyInfoEntities(bbsReplyInfoEntityList);
            } else {
                /** 表示帖子已删除 */
                respEvent.setErrorCode(ErrorCode.OTHER_ERROR.getValue());
                itemDao.detele(bbsItemEntity);
            }
        } catch (JSONException e) {
            respEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
            e.printStackTrace();
        }
        return respEvent;
    }
    private void convertEntity(BBSItemEntity bbsItemEntity,BBSItemEntity target){
        target.setFileInfo(bbsItemEntity.getFileInfo());
        target.setUserid(bbsItemEntity.getUserid());
    }
}
