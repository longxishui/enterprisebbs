package com.aspirecn.corpsocial.bundle.addrbook.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.uihandler.EventHandler;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corp.im.protocol.AddressBookList.AddressBook;

//import com.aspirecn.corp.im.protocol.AddFriend.UserCorpInfo;

/**
 * Created by Amos on 15-6-19.
 * 发送邀请
 */
@EventBusAnnotation.UIEventHandler(eventType = AddFriendEvent.class)
public class AddFriendEventHandler extends EventHandler implements
        IHandler<Null, AddFriendEvent> {

    @EventBusAnnotation.Component
    private UserDao dao ;

    @EventBusAnnotation.Autowired
    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    @Override
    public Null handle(final AddFriendEvent busEvent) {
//        CommandData commandData = new CommandData();
//
//        AddFriend.Builder builder = new AddFriend.Builder();
//        FriendInfo.Builder builder2 = new FriendInfo.Builder();
//        User user = dao.findUserDetail(Config.getInstance().getUserId());
//        List<FriendCorp> fcs = new ArrayList<FriendCorp>();
//        List<UserCorp> userCorps = user.getCorpList();
//
//
//        if (userCorps != null) {
//            for (UserCorp userCorp : userCorps) {
//                FriendCorp.Builder builder3 = new FriendCorp.Builder();
//                builder3.corpId(userCorp.getCorpId());
//                builder3.corpName(userCorp.getCorpName());
//                List<UserDept> userDepts = userCorp.getDeptList();
//                if (userDepts != null && userDepts.size() > 0) {
//                    builder3.duty(userDepts.get(0).getDuty());
//                }
//                fcs.add(builder3.build());
//            }
//        }
//
//        builder2.userid(user.getUserid());
//        builder2.name(user.getName());
//        builder2.cellphone(user.getCellphone());
//        builder2.headPic(user.getUrl());
//        builder2.signature(user.getSignature());
//        builder2.friendCorp(fcs);
//        builder.userInfo(builder2.build());
//
//        commandData.setCommandHeader(getCommandHeader(busEvent.getUser().getUserid()));
//        commandData.setData(builder.build().toByteArray());

//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst rst) {
//                if (rst.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    CommandData data = rst.getCommandRespData();
//                    if (data != null) {
//                        AddFriendResp resp = CommandData2AddFriendResp(data);
//
//                        if (resp != null
//                                && resp.errorCode.getValue() == ErrorCode.SUCCESS
//                                .getValue()) {
//
//                            List<User> users = dao.findByUserId(busEvent.getUser().getUserid());
//                            //本地没有这个用户
//                            if (users.size() == 0) {
//                                /**
//                                 * 保存用户为临时用户
//                                 */
//                                busEvent.getUser().setIsTemp(1);
//                                busEvent.getUser().setIsSameCorp(0);
//                                dao.createOrUpdataContact(busEvent.getUser());
//                            }
//
//                        }
//                    }
//                }
//
//                AddFriendRespEvent event = new AddFriendRespEvent(
//                        busEvent.getUser().getUserid(),
//                        rst.getErrorCode(),
//                        rst.getMessage());
//                eventListener.notifyListener(event);
//            }
//
//        });

        return new Null();
    }

//    private CommandHeader getCommandHeader(String userid) {
//        CommandHeader.Builder headBuilder = new CommandHeader.Builder();
//        headBuilder.commandType(CommandType.ADD_FRIEND);
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
//    private AddFriendResp CommandData2AddFriendResp(
//            CommandData data) {
//        try {
//            Wire wire = new Wire();
//            return wire.parseFrom(data.getData(),
//                    AddFriendResp.class);
//            // return ModifyFrequentlyContactResp.parseFrom(data.getData());
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送添加好友失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        } catch (IOException e) {
//            LogUtil.e(String.format(
//                    "向服务端发送添加常用好友失败：InvalidProtocolBufferException, %s",
//                    e.getMessage()), e);
//
//            return null;
//        }
//    }
}
