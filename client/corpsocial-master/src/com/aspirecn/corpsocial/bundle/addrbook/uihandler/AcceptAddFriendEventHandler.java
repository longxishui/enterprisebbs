package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import android.content.Intent;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.UserEntity;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.bundle.im.facade.AddSystemMsgService;
import com.aspirecn.corpsocial.bundle.im.facade.SystemMessage;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

/**
 * Created by Amos on 15-6-19.
 * 接受好友邀请
 */
@EventBusAnnotation.UIEventHandler(eventType = AcceptAddFriendEvent.class)
public class AcceptAddFriendEventHandler extends EventHandler implements
        IHandler<Null, AcceptAddFriendEvent> {

    @EventBusAnnotation.Component
    FriendInviteDao idao ;
    @EventBusAnnotation.Component
    UserDao dao ;
    @EventBusAnnotation.Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();
    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    @Override
    public Null handle(final AcceptAddFriendEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        AcceptAddFriend.Builder builder = new AcceptAddFriend.Builder();
//        FriendInfo.Builder builder2 = new FriendInfo.Builder();
//
//        String userId = Config.getInstance().getUserId();
//        String corpId = Config.getInstance().getCorpId();
//        UserEntity user = dao.findByUserId(userId, corpId);
//        if (user == null) {
//            user = new UserEntity();
//            user.setCorpId(corpId);
//            user.setUserid(userId);
//            user.setName(Config.getInstance().getNickName());
//            user.setCellphone(Config.getInstance().getUserName());
//            user.setUrl(Config.getInstance().getHeadImageUrl());
//        }
//        builder2.userid(user.getUserid());
//        builder2.name(user.getName());
//
//
//        builder.accept(true);
//        builder.userInfo(builder2.build());

//        commandData.setCommandHeader(getCommandHeader(busEvent.getInvite().getUserid()));
//        commandData.setData(builder.build().toByteArray());

//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        AcceptAddFriendResp resp = CommandData2AcceptAddFriendResp(data);
//
//                        if (resp != null
//                                && resp.errorCode.getValue() == ErrorCode.SUCCESS
//                                .getValue()) {
//                            try {
//                                idao.update(busEvent.getInvite().getSeqNo(), 1);
//                                List<User> users = dao.findByUserId(busEvent.getInvite().getUserid());
//                                dao.updateToFriend(users);
//                            } catch (SQLException sex) {
//                                LogUtil.e("", sex);
//                            }
//                        }
//                    }
//
//                    AcceptAddFriendRespEvent event = new AcceptAddFriendRespEvent(
//                            rst.getErrorCode());
//                    eventListener.notifyListener(event);

//                    notifyImModuleNewFriend(busEvent.getInvite().getUserid());

                    SystemMessage msg = new SystemMessage();
                    msg.setSenderId(busEvent.getInvite().getUserid());
                    msg.setContent("您已同意对方的好友请求，现在你们可以聊天了");
                    OsgiServiceLoader.getInstance().getService(AddSystemMsgService.class).invoke(msg);
//                }
//            }

//        });

        return new Null();
    }

    private void notifyImModuleNewFriend(String userid) {
        Intent i = new Intent("imNotifyReceiver");
        i.putExtra("action", "newFriend");
        i.putExtra("userid", userid);
        appBroadcastManager.sendBroadcast(i);
    }

//    private CommandHeader getCommandHeader(String userid) {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.ACCEPT_ADD_FRIEND);
//        headBuilder.sendTime(System.currentTimeMillis());
//        headBuilder.compressType(CompressType.GZIP_COMPRESS);
//        headBuilder.encryptType(EncryptType.UNENCRPT);
//        headBuilder.messageId(CommonUtils.getUUID());
//        headBuilder.userId(Config.getInstance().getUserId());
//        headBuilder.corpId(Config.getInstance().getCorpId());
//        headBuilder.targetId(userid);
//        // headBuilder.setContentType(ContentType.TEXT);
//        CommandHeader header = headBuilder.build();
//
//        return header;
//    }
//
//    private AcceptAddFriendResp CommandData2AcceptAddFriendResp(
//            CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(),
//                    AcceptAddFriendResp.class);
//            // return ModifyFrequentlyContactResp.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送接受好友失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送接受好友失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }
}


