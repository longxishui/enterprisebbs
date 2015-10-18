package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.net.HttpRequest;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.ReplyType;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.BitmapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UIEventHandler(eventType = BBSReplyEvent.class)
public class BBSReplyEventHandler implements IHandler<Null, BBSReplyEvent> {


    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final BBSReplyEvent args) {

        HttpCallBack httpCallBack = new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode,String message) {
                BBSReplyRespEvent bbsReplyRespEvent = new BBSReplyRespEvent();
                bbsReplyRespEvent.setGroupId(args.getGroupId());
                bbsReplyRespEvent.setBbsId(args.getItemId());
                bbsReplyRespEvent.setType(args.getReplyType());
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        bbsReplyRespEvent.setType(args.getReplyType());
                        bbsReplyRespEvent.setReplyId(Config.getInstance().getUserId());
                        bbsReplyRespEvent.setErrorCode(ErrorCode.SUCCESS.getValue());
                    } else {
                        bbsReplyRespEvent.setErrorCode(ErrorCode.NETWORK_FAILED.getValue());
                    }
                } else {
                    bbsReplyRespEvent.setErrorCode(errorCode);
                }
                instance.notifyListener(bbsReplyRespEvent);
            }
        };
        if (args.getReplyType() == ReplyType.REPLY && args.isHasPic()) {
            List<File> fileList = new ArrayList<File>();
            for(String path :args.getListFilePath()){
                File mFile = BitmapUtil.uploadFile(path);
                fileList.add(mFile);
            }
            HttpRequest.request(WorkgrpConfig.CREATE_OR_MODIFY_ITEM, args.getJson(), fileList, httpCallBack);
        } else {
            HttpRequest.request(WorkgrpConfig.CREATE_OR_MODIFY_ITEM, args.getJson(), httpCallBack);
        }

        return new Null();
    }


}