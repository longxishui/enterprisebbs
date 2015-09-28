package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.DownloadFrequentlyContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.DownloadFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;

/**
 * 下载常用联系人
 *
 * @author meixuesong
 */
@UIEventHandler(eventType = DownloadFrequentlyContactEvent.class)
public class DownloadFrequentlyContactEventHandler implements
        IHandler<Null, DownloadFrequentlyContactEvent> {

//    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @EventBusAnnotation.Component
    private UserDao dao;

    @Override
    public Null handle(DownloadFrequentlyContactEvent busEvent) {
//        CommandData commandData = new CommandData();
//        GetFrequentlyContact.Builder builder = new GetFrequentlyContact.Builder();
//        commandData.setCommandHeader(getCommandHeader());
//        commandData.setData(builder.build().toByteArray());

//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        GetFrequentlyContactResp resp = CommandData2GetFrequentlyContactResp(data);
//                        if (resp != null) {
//
//                            List<String> userIds = resp.userId;
//                            List<User> existfreqs=dao.findFrequent(null);
//                            for (String userId : userIds) {
//                                //dao.save(Config.getInstance().getUserId(), userId);
//                                List<User> users= dao.findByUserId(userId);
//                                dao.updateToFrequent(users);
//                            }
//
//                            //删除在其他设备上已删除的常用联系人
//                            for(User user:existfreqs){
//                                boolean exist=false;
//                                for (String userId : userIds) {
//                                    if(user.getUserid().equals(userId)){
//                                        exist=true;
//                                        break;
//                                    }
//                                }
//                                if(!exist){
//                                    dao.cancelFrequent(user);
//                                }
//                            }
//
//                        }
//                    }
//                } else {
//                    LogUtil.e(String.format("从服务端获取联系人失败：%d",
//                            rst.getErrorCode()));
//                }
//
//                notifyRefreshEnd(rst.getErrorCode());
//            }
//        });

        return new Null();
    }

//    private CommandHeader getCommandHeader() {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.GET_FREQUENTLY_CONTACT);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.GZIP_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        // headBuilder.setContentType(ContentType.TEXT);
//        CommandHeader header = headBuilder.build();
//
//        return header;
//    }
//
//    private GetFrequentlyContactResp CommandData2GetFrequentlyContactResp(
//            CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(),
//                    GetFrequentlyContactResp.class);
//            // return GetFrequentlyContactResp.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "从服务端获取联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }

    private void notifyRefreshEnd(int status) {
        DownloadFrequentlyContactRespEvent event = new DownloadFrequentlyContactRespEvent();
        event.setErrorCode(status);
        eventListener.notifyListener(event);
    }
}
