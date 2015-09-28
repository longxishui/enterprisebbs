package com.aspirecn.corpsocial.bundle.im.uihandler;

import com.aspirecn.corpsocial.bundle.im.domain.SearchItem;
import com.aspirecn.corpsocial.bundle.im.domain.SearchItem.SearchType;
import com.aspirecn.corpsocial.bundle.im.event.SearchChatEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.GroupDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * 搜索会话处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = SearchChatEvent.class)
public class SearchChatEventHandler implements
        IHandler<List<SearchItem>, SearchChatEvent> {

    // private OsgiServiceLoader osgiServiceLoader = OsgiServiceLoader
    // .getInstance();

    private GroupDao groupDao = new GroupDao();

    private ChatDao chatDao = new ChatDao();

    @Override
    public LinkedList<SearchItem> handle(SearchChatEvent busEvent) {
        String searchKeyWord = busEvent.getSearchKeyWord();
        LinkedList<SearchItem> searchResult = new LinkedList<SearchItem>();
        String userId = Config.getInstance().getUserId();

        // SearchContactService searchContactService = (SearchContactService)
        // osgiServiceLoader
        // .getService(SearchContactService.class);
        // List<Contact> contactList =
        // searchContactService.invoke(searchKeyWord);
        // for (Contact contact : contactList) {
        // SearchItem searchItem = new SearchItem();
        // searchItem.setId(contact.getId());
        // searchItem.setTitle(contact.getName());
        // searchItem.setType(ChatType.P2P.value);
        //
        // searchResult.addLast(searchItem);
        // }

        List<ChatEntity> chatList = chatDao.searchChatByKeyword(userId,
                ChatType.P2P.value, searchKeyWord, "lastUpdateTime DESC");

        if (chatList.size() > 0) {
            SearchItem contactTitle = new SearchItem();
            contactTitle.setTitle("联系人");
            searchResult.addLast(contactTitle);
        }

        for (ChatEntity chatEntity : chatList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId(chatEntity.getChatId());
            searchItem.setTitle(chatEntity.getChatName());
            searchItem.setType(SearchType.P2PChat);

            searchResult.addLast(searchItem);
        }

        List<GroupEntity> groupList = groupDao.searchGroupByKeyword(userId,
                searchKeyWord, "lastUpdateTime DESC");

        if (groupList.size() > 0) {
            SearchItem groupTitle = new SearchItem();
            groupTitle.setTitle("群组");
            searchResult.addLast(groupTitle);
        }

        for (GroupEntity groupEntity : groupList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId(groupEntity.getGroupId());
            searchItem.setTitle(groupEntity.getName());
            int groupType = groupEntity.getGroupType();

            if (GroupType.DEPRT.getValue() == groupType
                    || GroupType.DEPT.getValue() == groupType) {
                searchItem.setType(SearchType.CorpGroupChat);
            } else if (GroupType.GROUP.getValue() == groupType) {
                searchItem.setType(SearchType.CustomGroupChat);
            }

            searchResult.addLast(searchItem);
        }

        return searchResult;
    }
}
