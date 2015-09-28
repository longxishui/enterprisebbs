package com.aspirecn.corpsocial.bundle.im.repository.entity;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindGroupMembersService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupSizeParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.GetGroupSizeService;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 群会话设置
 *
 * @author lizhuo_a
 */
public class GroupChatSetting extends ChatSetting implements Serializable {

    private static final long serialVersionUID = -907686537025782738L;

    //群组
    private GroupEntity groupEntity;
    //新消息通知开关
    private boolean newMsgNotify;
    //是否是管理员
    private boolean isAdmin;

    /**
     * 发言限制类型：0-不限制，1-限制只允许部分人发言
     *
     * @return
     */
    public String doGetLimitType() {
        return groupEntity.getLimitType();
    }

    public String doGetGroupId() {
        return groupEntity.getGroupId();
    }

    public String doGetGroupName() {
        return groupEntity.getName();
    }

    public int doGetGroupType() {
        return groupEntity.getGroupType();
    }

    public int doGetMemberCount() {
        //非自建群
        if (groupEntity.getGroupType() != GroupType.GROUP.getValue()) {
//            UserServiceParam param = new UserServiceParam();
//            param.setServie("GetGroupSizeService");
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("deptId", groupEntity.getGroupId());
//            param.setParams(map);
//            UserService service = (UserService) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class);
//            UserServiceResult result = service.invoke(param);
//
//            return (Integer) result.getData();

            return (Integer) OsgiServiceLoader.getInstance().getService(GetGroupSizeService.class).invoke(groupEntity.getGroupId());

        }
        return groupEntity.getMemberList().size();
    }

    /**
     * 获取成员列表
     *
     * @return
     */
    public List<GroupMemberEntity> doGetMemberList(int index, int count) {
        String groupId = groupEntity.getGroupId();
        int groupType = groupEntity.getGroupType();

        List<GroupMemberEntity> groupMemberList = new ArrayList<GroupMemberEntity>();

        if (GroupType.GROUP.getValue() == groupType) {
            Map<String, Object> wheres = new HashMap<String, Object>();
            wheres.put("belongUserId", Config.getInstance().getUserId());
            wheres.put("groupId", groupId);

            GroupMemberDao groupMemberDao = new GroupMemberDao();
            groupMemberList = groupMemberDao.findAllByWhereAndIndex(wheres, index, count, "");
        } else if (GroupType.DEPRT.getValue() == groupType || GroupType.DEPT.getValue() == groupType) {
//            UserService service = (UserService) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class);
//            UserServiceParam param = new UserServiceParam();
//            Map map = new HashMap();
//            map.put("deptId", groupId);
//            map.put("start", index);
//            map.put("limit", count);
//            param.setServie("FindGroupMembersService");
//            param.setParams(map);
//            UserServiceResult result = service.invoke(param);
//            List<User> contactList = (List) result.getData();
            GetGroupSizeParam param = new GetGroupSizeParam();
            param.setDeptId(groupId);
            param.setStart(index);
            param.setLimit(count);

            List<User> contactList = (List<User>) OsgiServiceLoader.getInstance().getService(FindGroupMembersService.class).invoke(param);

            for (User contact : contactList) {
                GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
                groupMemberEntity.setGroupId(groupId);
                groupMemberEntity.setHeadImgUrl(contact.getUrl());
                groupMemberEntity.setMemberId(contact.getUserid());
                groupMemberEntity.setMemberName(contact.getName());
                groupMemberEntity.setStatus(contact.getImStatus());
                groupMemberEntity.setInitialCode(contact.getInitialKey());
                groupMemberEntity.setCanCommunicate(contact.canCommunicate());
                groupMemberList.add(groupMemberEntity);
            }
        }
        return groupMemberList;
    }

    public List<String> doGetAdminIds() {
        return groupEntity.getAdminList();
    }

    public List<String> doGetMemberIds() {
        //非自建群
        if (groupEntity.getGroupType() != GroupType.GROUP.getValue()) {
//            UserServiceParam param = new UserServiceParam();
//            param.setServie("GetGroupIdsService");
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("deptId", groupEntity.getGroupId());
//            param.setParams(map);
//            UserService service = (UserService) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class);
//            UserServiceResult result = service.invoke(param);
//
//            return (List<String>) result.getData();

            return (List<String>) OsgiServiceLoader.getInstance().getService(GetGroupIdsService.class).invoke(groupEntity.getGroupId());
        }
        return groupEntity.getMemberList();
    }

    public boolean isNewMsgNotify() {
        return newMsgNotify;
    }

    public void setNewMsgNotify(boolean newMsgNotify) {
        this.newMsgNotify = newMsgNotify;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

}
