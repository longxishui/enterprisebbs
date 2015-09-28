package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-25.
 */
@EventBusAnnotation.UIEventHandler(eventType = RemoveFriendEvent.class)
public class RemoveFriendEventHandler extends EventHandler implements
        IHandler<Null, RemoveFriendEvent> {

    @EventBusAnnotation.Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();
    @EventBusAnnotation.Component
    private UserDao dao ;

    @Override
    public Null handle(final RemoveFriendEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        ModifyFrequentlyContact.Builder builder = new ModifyFrequentlyContact.Builder();
//        ModifyFrequentlyContact.Contact.Builder contactBuilder = new ModifyFrequentlyContact.Contact.Builder();
//        contactBuilder.userId(busEvent.getUserid());
//        contactBuilder.action(ModifyFrequentlyContact.Action.DELETE);
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
//
//
//                            //dao.delete(Config.getInstance().getUserId(), busEvent.getUserid());
//                            dao.cancelFriend(busEvent.getUserid());
//                            DeleteChatEvent event = new DeleteChatEvent();
//                            event.setChatId(busEvent.getUserid());
//                            UiEventHandleFacade.getInstance().handle(event);
//                        }
//                    }
//                }
//                RemoveFriendRespEvent event = new RemoveFriendRespEvent(
//                        busEvent.getUserid(), rst.getErrorCode(), 0);
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
//                    "向服务端发送删除常用联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送删除常用联系人失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//            return null;
//        }
//    }
}
