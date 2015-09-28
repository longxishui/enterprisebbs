package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import android.os.Environment;
import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSItemEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.BitmapUtil;
import com.aspirecn.corpsocial.common.util.HttpRequest;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@UIEventHandler(eventType = CreateOrModifyBBSEvent.class)
public class CreateOrModifyBBSEventHandler implements
        IHandler<Null, CreateOrModifyBBSEvent> {


    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final CreateOrModifyBBSEvent args) {
        HttpCallBack httpCallBack = new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode,String message) {
                CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent;
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    LogUtil.e("获得同事圈CreateOrModifyBBSRespEvent数据为：" + message);
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    BBSItemDao bbsItemDao = new BBSItemDao();
                    if (httpMessage.httpResult == 0) {
                        if (!TextUtils.isEmpty(args.getItemId())) {
                            BBSItem bbsItem = bbsItemDao.findItemById(args.getItemId());
                            BBSItemEntity bbsItemEntity = bbsItem.getBbsItemEntity();
                            bbsItemEntity.setContent(args.getContent());
                            bbsItemEntity.setCreatorId(Config.getInstance().getUserId());
                            bbsItemEntity.setCreatorName(Config.getInstance().getNickName());
                            bbsItemEntity.setTitle(args.getTitle());
                            bbsItemEntity.setGroupId(args.getGroupId());
                            bbsItemEntity.setUserid(Config.getInstance().getUserId());
                            createOrModifyBBSRespEvent = handleRespData(httpMessage.data, bbsItemEntity);
                            createOrModifyBBSRespEvent.setErrorCode(errorCode);
                            createOrModifyBBSRespEvent.setGroupId(args.getGroupId());
                            createOrModifyBBSRespEvent.setBbsItemEntity(bbsItemEntity);
                            if (args.isHasPic()) {
                                FileInfoEntity fileInfoEntity = new FileInfoEntity();
                                fileInfoEntity.setUrl(args.getPath());
                                fileInfoEntity.setUserid(Config.getInstance().getUserId());
                                fileInfoEntity.setId(args.getItemId());
                                bbsItemEntity.setFileInfo(new Gson().toJson(fileInfoEntity));
                                bbsItemDao.update(bbsItemEntity);
                                createOrModifyBBSRespEvent.setFileInfoEntity(fileInfoEntity);
                            }
                        } else {
                            createOrModifyBBSRespEvent = handleRespData(httpMessage.data, null);
                            createOrModifyBBSRespEvent.setGroupId(args.getGroupId());
                        }
                    } else {
                        createOrModifyBBSRespEvent = new CreateOrModifyBBSRespEvent();
                        createOrModifyBBSRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
                        createOrModifyBBSRespEvent.setItemId(args.getItemId());
                        createOrModifyBBSRespEvent.setGroupId(args.getGroupId());
                    }
                } else {
                    createOrModifyBBSRespEvent = new CreateOrModifyBBSRespEvent();
                    createOrModifyBBSRespEvent.setErrorCode(errorCode);
                    createOrModifyBBSRespEvent.setItemId(args.getItemId());
                    createOrModifyBBSRespEvent.setGroupId(args.getGroupId());
                }
                instance.notifyListener(createOrModifyBBSRespEvent);
            }
        };
        if (args.isHasPic()) {
            File mFile;
            if (args.getPath().startsWith(Environment
                    .getExternalStorageDirectory().getAbsolutePath())) {
                mFile = BitmapUtil.uploadFile(args.getPath());
            } else {
                try {
                    String picturePath = ImageLoader.getInstance().getDiskCache().get(args.getPath()).getAbsolutePath();
                    mFile = BitmapUtil.uploadFile(picturePath);
                } catch (Exception e) {
                    mFile = null;
                }
            }
            HttpRequest.request(WorkgrpConfig.CREATE_OR_MODIFY_ITEM, args.getJson(), mFile, httpCallBack);
        } else {
            HttpRequest.request(WorkgrpConfig.CREATE_OR_MODIFY_ITEM, args.getJson(), httpCallBack);
        }
//        IMessageStatusNotify messageStatusNotify = new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst messageRst) {
//                int errorCode = messageRst.getErrorCode();
//                float percent = messageRst.getPercent();
//                if (ErrorCode.SUCCESS.getValue() == errorCode) {
//                    if (!TextUtils.isEmpty(args.getItemId())) {
//                        BBSItemDao itemDao = new BBSItemDao();
//                        BBSItemEntity bbsItemEntity = itemDao.findById(args
//                                .getItemId());
//                        bbsItemEntity.setTitle(args.getTitle());
//                        bbsItemEntity.setContent(args.getContent());
//                        if (TextUtils.isEmpty(args.getPath())) {
//                            bbsItemEntity.setFileInfo("");
//                        } else {
//                            FileInfoEntity fileInfoEntity = new FileInfoEntity();
//                            fileInfoEntity.setUserid(Config.getInstance().getUserId());
//                            fileInfoEntity.setId(args.getItemId());
//                            fileInfoEntity.setUrl(args.getPath());
//                            fileInfoEntity.setOrginalUrl("");
//                            bbsItemEntity.setFileInfo(new Gson().toJson(fileInfoEntity));
//                        }
//                        itemDao.update(bbsItemEntity);
//                    }
//                }
//                if (percent == 1.0 || !args.isHasPic()) {
//                    try {
//                        CreateOrModifyBBSRespEvent event = new CreateOrModifyBBSRespEvent();
//                        Wire wire = new Wire();
//                        CreateOrModifyBBSResp resp = wire.parseFrom(
//                                messageRst.getCommandRespData().getData(),
//                                CreateOrModifyBBSResp.class);
//                        // CreateOrModifyBBSResp resp =
//                        // CreateOrModifyBBSResp.parseFrom(messageRst.getCommandRespData().getData());
//                        event.setItemId(resp.itemId);
//                        event.setGroupId(args.getGroupId());
//                        event.setErrorCode(ErrorCode.SUCCESS.getValue());
//                        instance.notifyListener(event);
//                    } catch (InvalidProtocolBufferException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    CreateOrModifyBBSRespEvent event = new CreateOrModifyBBSRespEvent();
//                    event.setErrorCode(errorCode);
//                    event.setItemId(args.getItemId());
//                    event.setGroupId(args.getGroupId());
//                    instance.notifyListener(event);
//                }
//            }
//            };
        return new Null();
    }

    private CreateOrModifyBBSRespEvent handleRespData(String message, BBSItemEntity bbsItemEntity) {
        CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent = new CreateOrModifyBBSRespEvent();
        try {
            JSONObject jsonData = new JSONObject(message);
            createOrModifyBBSRespEvent.setItemId(jsonData.getString("itemId"));
            if (bbsItemEntity != null) {
                bbsItemEntity.setCreateTime(jsonData.getLong("createTime"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createOrModifyBBSRespEvent;
    }

}
