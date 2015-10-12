package com.aspirecn.corpsocial.bundle.workgrp.uihandler;

import com.aspirecn.corpsocial.bundle.net.HttpCallBack;
import com.aspirecn.corpsocial.bundle.net.HttpRequest;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSGroupRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSReplyInfoDao;
import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.bundle.workgrp.util.HttpMessage;
import com.aspirecn.corpsocial.bundle.workgrp.util.WorkgrpConfig;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

@UIEventHandler(eventType = BBSDeleteEvent.class)
public class BBSDeleteEventHandler implements IHandler<Null, BBSDeleteEvent> {

    private EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();
    private BBSItemDao bbsItemDao;
    private BBSReplyInfoDao replyInfoDao;

    @Override
    public Null handle(final BBSDeleteEvent args) {
        if (bbsItemDao == null) {
            bbsItemDao = new BBSItemDao();
        }
        if (replyInfoDao == null) {
            replyInfoDao = new BBSReplyInfoDao();
        }

        HttpRequest.request(WorkgrpConfig.BBS_DELETE, args.getJson(), new HttpCallBack() {
            @Override
            public void notifyResult(int errorCode, String message) {
                GetBBSGroupRespEvent getBBSGroupRespEvent = new GetBBSGroupRespEvent();
                if (errorCode == ErrorCode.SUCCESS.getValue()) {
                    HttpMessage httpMessage = WorkgrpConfig.getHttpMessage(message);
                    if (httpMessage.httpResult == 0) {
                        if (args.getDeleteType() == DeleteType.ITEM) {
                            bbsItemDao.deteleById(args.getItemId());
                        } else {
                            replyInfoDao.deteleById(args.getReplyId());
                            bbsItemDao.refreshReplyTimes(args.getItemId(), -1);
                        }
                        BBSDeleteRespEvent bbsDeleteRespEvent = new BBSDeleteRespEvent();
                        bbsDeleteRespEvent.setErrorCode(errorCode);
                        bbsDeleteRespEvent.setBbsItemId(args.getItemId());
                        bbsDeleteRespEvent.setDeleteType(args.getDeleteType());
                        bbsDeleteRespEvent.setGroupId(args.getGroupId());
                        bbsDeleteRespEvent.setReplyId(args.getReplyId());
                        instance.notifyListener(bbsDeleteRespEvent);
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

}
