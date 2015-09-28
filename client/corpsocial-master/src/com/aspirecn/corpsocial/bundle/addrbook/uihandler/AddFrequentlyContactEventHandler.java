package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddFrequentlyContactEvent;
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
 * 添加常用联系人事件处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = AddFrequentlyContactEvent.class)
public class AddFrequentlyContactEventHandler implements
        IHandler<Null, AddFrequentlyContactEvent> {
    @Autowired
//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    //private FrequentlyContactDao dao = new FrequentlyContactDao();

    @EventBusAnnotation.Component
    private UserDao dao;

    @Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final AddFrequentlyContactEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        ModifyFrequentlyContact.Builder builder = new ModifyFrequentlyContact.Builder();
//        ModifyFrequentlyContact.Contact.Builder contactBuilder = new ModifyFrequentlyContact.Contact.Builder();
//        contactBuilder.userId(busEvent.getContactId());
//        contactBuilder.action(ModifyFrequentlyContact.Action.ADD);
//        ModifyFrequentlyContact.Contact contact = contactBuilder.build();
//        List<ModifyFrequentlyContact.Contact> contactList = new ArrayList<ModifyFrequentlyContact.Contact>();
//        contactList.add(contact);
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
//                            List<User> users= dao.findByUserId(busEvent.getContactId());
//                            dao.updateToFrequent(users);
//                            //dao.save(Config.getInstance().getUserId(), busEvent.getContactId());
//                        }
//                    }
//                }
//
//                AddFrequentlyContactRespEvent event = new AddFrequentlyContactRespEvent(
//                        busEvent.getContactId(), rst.getErrorCode());
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
