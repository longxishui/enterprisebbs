package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.event.AcceptGroupInviteEvent;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

import java.util.HashMap;
import java.util.Map;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 接收到邀请后反馈处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = AcceptGroupInviteEvent.class)
public class AcceptGroupInviteEventHandler implements
        IHandler<Null, AcceptGroupInviteEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    @Override
    public Null handle(final AcceptGroupInviteEvent busEvent) {
//        CommandData commandData = buildReqData(busEvent);
//        imNetClient.sendMessage(commandData, new IMessageStatusNotify() {
//            @Override
//            public void notify(MessageRst arg0) {
//                int errorCode = arg0.getErrorCode();
//
//                if (ErrorCode.SUCCESS.getValue() == errorCode) {
//                    handleAcceptSuccess(busEvent);
//                }
//                AcceptGroupInviteRespEvent acceptGroupInviteRespEvent = new AcceptGroupInviteRespEvent();
//
//                acceptGroupInviteRespEvent.setErrorCode(errorCode);
//                eventListener.notifyListener(acceptGroupInviteRespEvent);
//            }
//        });

        return new Null();
    }

    private void handleAcceptSuccess(
            AcceptGroupInviteEvent acceptGroupInviteEvent) {
        String userId = Config.getInstance().getUserId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        String groupId = acceptGroupInviteEvent.getGroupId();
        wheres.put("groupId", groupId);
        wheres.put("memberId", userId);
        wheres.put("belongUserId", userId);
        GroupMemberEntity findGroupMember = groupMemberDao.findByWhere(wheres);
        User contact = getUser(userId);

        if (findGroupMember == null) {
            GroupMemberEntity newGroupMember = new GroupMemberEntity();
            newGroupMember.setBelongUserId(userId);
            newGroupMember.setGroupId(groupId);
            newGroupMember.setHeadImgUrl(contact == null ? "" : contact.getUrl());
            newGroupMember.setMemberId(userId);
            newGroupMember.setMemberName(contact == null ? "" : contact
                    .getName());
            newGroupMember.setStatus(contact == null ? "" : contact
                    .getImStatus());
            newGroupMember.setInitialCode(contact == null ? "" : contact.getInitialKey());
            groupMemberDao.insert(newGroupMember);
        }
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
//        UserService service = (UserService) OsgiServiceLoader.getInstance()
//                .getService(UserService.class);
//        UserServiceParam param = new UserServiceParam();
//        param.setServie("FindContactService");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        param.setParams(map);
//        UserServiceResult<List<User>> result = service.invoke(param);
//        List<User> users = result.getData();
//        if (users != null && users.size() > 0) {
//            return users.get(0);
//        }
//        return null;

        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }

}
