package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchGroupMemberParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.SearchGroupMemberService;
import com.aspirecn.corpsocial.bundle.im.event.SearchGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.repository.GroupMemberDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;

import java.util.LinkedList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 搜索会话处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = SearchGroupMemberEvent.class)
public class SearchGroupMemberEventHandler implements
        IHandler<List<GroupMemberEntity>, SearchGroupMemberEvent> {

    private GroupMemberDao groupMemberDao = new GroupMemberDao();

    @Override
    public List<GroupMemberEntity> handle(SearchGroupMemberEvent busEvent) {
        String searchKeyWord = busEvent.getSearchKeyWord();
        String groupId = busEvent.getGroupId();
        int groupType = busEvent.getGroupType();

        List<GroupMemberEntity> searchResult = new LinkedList<GroupMemberEntity>();
        //自建群
        if (GroupType.GROUP.getValue() == groupType) {
            String userId = Config.getInstance().getUserId();
            searchResult = groupMemberDao.searchGroupMemberByKeyword(groupId,
                    userId, searchKeyWord, "seqId DESC");
        }
        //公司群
        else if (GroupType.DEPRT.getValue() == groupType || GroupType.DEPT.getValue() == groupType) {
//            UserService service = (UserService) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class);
//            UserServiceParam param = new UserServiceParam();
//            Map map = new HashMap();
//            map.put("deptId", groupId);
//            map.put("key", searchKeyWord);
//            param.setServie("SearchGroupMemberService");
//            param.setParams(map);
//
//            UserServiceResult result = service.invoke(param);
//            List<User> contactList = (List) result.getData();
            SearchGroupMemberParam param = new SearchGroupMemberParam();
            param.setDeptId(groupId);
            param.setSearchKeyWord(searchKeyWord);
            List<User> contactList = (List<User>) OsgiServiceLoader.getInstance().getService(SearchGroupMemberService.class).invoke(param);

            for (User contact : contactList) {
                GroupMemberEntity groupMemberEntity = new GroupMemberEntity();
                groupMemberEntity.setGroupId(groupId);
                groupMemberEntity.setHeadImgUrl(contact.getUrl());
                groupMemberEntity.setMemberId(contact.getUserid());
                groupMemberEntity.setMemberName(contact.getName());
                groupMemberEntity.setStatus(contact.getImStatus());
                groupMemberEntity.setInitialCode(contact.getInitialKey());
                searchResult.add(groupMemberEntity);
            }
        }

        return searchResult;
    }
}
