package com.aspirecn.corpsocial.bundle.addrbook.datareceiver;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindInviteContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.FriendInviteDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FriendInviteEntity;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Amos on 15-6-19.
 * 接收邀请信息
 */
@EventBusAnnotation.DataReceiveHandler(commandType = CommandData.CommandType.ADD_FRIEND)
public class InviteFriendHandler implements IHandler<Null, CommandData> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    private FriendInviteDao dao = new FriendInviteDao();

    private UserDao userDao = new UserDao();

    @Override
    public Null handle(CommandData args) {

//        try {
//            CommandHeader commandHeader = args.getCommandHeader();
//            AddFriend addFriend = new Wire().parseFrom(
//                    args.getData(), AddFriend.class);
//
//
//            FriendInfo friendInfo = addFriend.userInfo;
//            String corpName = "";
//            String corpId = "";
//            String userid = friendInfo.userid;
//            String username = friendInfo.name;
//            String descri = addFriend.descri;
//            String signature = friendInfo.signature;
//
//            FriendInviteEntity entity = dao.findUnaccept(userid);
//            if (entity == null) {
//                FriendInviteEntity old = dao.findAccept(userid);
//                FriendInvite invite = new FriendInvite();
//                invite.setSignature(signature);
//                invite.setCreateTime((new java.util.Date()).getTime());
//                invite.setStatus(0);
//                invite.setUserid(userid);
//                invite.setUsername(username);
//                invite.setSmallUrl(friendInfo.headPic);
//                List<FriendCorp> friendCorps = friendInfo.friendCorp;
//                JsonArray array = new JsonArray();
//                for (FriendCorp friendCorp : friendCorps) {
//                    JsonObject j = new JsonObject();
//                    j.addProperty("corpId", friendCorp.corpId);
//                    j.addProperty("corpName", friendCorp.corpName);
//                    j.addProperty("duty", friendCorp.duty);
//                    array.add(j);
//                }
//                invite.setCorpInfo((new Gson()).toJson(array));
//
////                List<User> users = userDao.findByUserId(userid);
////                if (users != null && users.size() > 0) {
////                    invite.setUser(users.get(0));
////                    invite.setCorpName(users.get(0).getCorpName());
////                }
//                User user = userDao.findUserDetail(userid);
//                if (user != null) {
//                    invite.setUser(user);
//                }
//
//
//                //dao.create(invite);
//                if (user != null && user.getIsFriend() == 2) {//曾经是好友，被删除了
//                    List<User> users = userDao.findByUserId(userid);
//                    userDao.updateToFriend(users);
//                } else {
//                    dao.createAndRemoveInvite(invite, old);
//                }
////                eventListener.notifyListener(new InviteFriendEvent(invite));
//                // FIXME 刷新未读消息提醒，刷新“新的好友”列表
//
//                /**
//                 * async get contact from server,save user info, update invite info,and then notify
//                 */
//                if (user == null) {
//                    UiEventHandleFacade.getInstance().handle(new FindInviteContactEvent(userid));
//                } else {
//                    eventListener.notifyListener(new InviteFriendEvent(invite));
//                }
//
//                //统计通讯录模块未读消息数
//                UiEventHandleFacade.getInstance().handle(new CalculateAddressBookUnReadMsgCountEvent());
//            }
//        } catch (InvalidProtocolBufferException e) {
//            LogUtil.e("处理好友邀请失败", e);
//        } catch (IOException e) {
//            LogUtil.e("处理好友邀请失败", e);
//        } catch (SQLException sex) {
//            LogUtil.e("处理好友邀请失败", sex);
//        }

        return new Null();
    }

}
