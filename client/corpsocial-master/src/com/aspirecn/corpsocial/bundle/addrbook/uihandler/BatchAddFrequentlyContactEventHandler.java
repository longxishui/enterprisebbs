package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.BatchAddFrequentlyContactEvent;
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
import java.util.List;

/**
 * 批量添加常用联系人事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = BatchAddFrequentlyContactEvent.class)
public class BatchAddFrequentlyContactEventHandler implements
        IHandler<Null, BatchAddFrequentlyContactEvent> {
    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    @EventBusAnnotation.Component
    private UserDao dao;

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final BatchAddFrequentlyContactEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        ModifyFrequentlyContact.Builder builder = new ModifyFrequentlyContact.Builder();
//        List<ModifyFrequentlyContact.Contact> contactList = new ArrayList<ModifyFrequentlyContact.Contact>();
//
//        List<String> contactIds = busEvent.getContactIds();
//        for (String id : contactIds) {
//            ModifyFrequentlyContact.Contact.Builder contactBuilder = new ModifyFrequentlyContact.Contact.Builder();
//            contactBuilder.userId(id);
//            contactBuilder.action(ModifyFrequentlyContact.Action.ADD);
//            ModifyFrequentlyContact.Contact contact = contactBuilder.build();
//            contactList.add(contact);
//        }
//        builder.contact(contactList);
//
//        commandData.setCommandHeader(getCommandHeader());
//        commandData.setData(builder.build().toByteArray());

//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        ModifyFrequentlyContactResp resp = CommandData2ModifyFrequentlyContactResp(data);
//                        if (resp != null
//                                && resp.errorCode.getValue() == ErrorCode.SUCCESS
//                                .getValue()) {
//
//                            List<String> list = busEvent.getContactIds();
//                            for (String id : list) {
//                                try {
//                                    List<User> users = dao.findByUserId(id);
//                                    dao.updateToFrequent(users);
//                                }catch(Exception ex){}
//                            }
//                        }
//                    }
//                }
//
//                BatchAddFrequentlyContactRespEvent event = new BatchAddFrequentlyContactRespEvent(
//                        busEvent.getContactIds(), rst.getErrorCode());
//                eventListener.notifyListener(event);
//            }
//
//        });

        return new Null();
    }

//    private CommandHeader getCommandHeader() {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.MODIFY_FREQUENTLY_CONTACT);
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
//    private ModifyFrequentlyContactResp CommandData2ModifyFrequentlyContactResp(
//            CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(),
//                    ModifyFrequentlyContactResp.class);
//            // return ModifyFrequentlyContactResp.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送添加常用联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送添加常用联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }
}
