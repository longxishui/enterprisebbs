package com.aspirecn.corpsocial.bundle.addrbook.datareceiver;

//import com.aspirecn.corp.im.protocol.AddFriend;

import android.content.Intent;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptInviteRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FriendInviteEntity;
import com.aspirecn.corpsocial.bundle.im.facade.AddSystemMsgService;
import com.aspirecn.corpsocial.bundle.im.facade.SystemMessage;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Amos on 15-6-25.
 * 接受到邀请好友反馈
 */
@EventBusAnnotation.DataReceiveHandler(commandType = CommandData.CommandType.ACCEPT_ADD_FRIEND)
public class AcceptInviteHandler implements IHandler<Null, CommandData> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();


    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    private UserDao userDao = new UserDao();

    private FriendInviteDao inviteDao = new FriendInviteDao();

    @Override
    public Null handle(CommandData args) {

//        try {

//            AcceptAddFriend addFriend = new Wire().parseFrom(
//                    args.getData(), AcceptAddFriend.class);
//
//            String userid = addFriend.userInfo.userid;
//
//            boolean accept = addFriend.accept;
//
//            if (accept) {
//                List<User> users = userDao.findByUserId(userid);
//                if (users != null && users.size() > 0) {
//                    userDao.updateToFriend(users);
//                }
//
//                FriendInviteEntity entity = inviteDao.findUnaccept(userid);
//                if (entity != null) {
//                    try {
//                        inviteDao.update(entity.getSeqNo(), 1);
//                    } catch (Exception e) {
//                        LogUtil.e("", e);
//                    }
//                }
//
//                eventListener.notifyListener(new AcceptInviteRespEvent(userid));
//
////                notifyImModuleNewFriend(userid);
//                // FIXME notify address book module refresh friend list
//                SystemMessage msg = new SystemMessage();
//                msg.setSenderId(userid);
//                msg.setContent("对方已同意您的请求，现在你们可以聊天了");
//                OsgiServiceLoader.getInstance().getService(AddSystemMsgService.class).invoke(msg);
//            }
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e("处理好友邀请失败", e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new Null();
    }

    private void notifyImModuleNewFriend(String userid) {
        Intent i = new Intent("imNotifyReceiver");
        i.putExtra("action", "newFriend");
        i.putExtra("userid", userid);
        appBroadcastManager.sendBroadcast(i);
    }
}
