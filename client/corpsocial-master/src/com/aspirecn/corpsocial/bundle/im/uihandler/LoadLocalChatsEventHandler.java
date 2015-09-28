package com.aspirecn.corpsocial.bundle.im.uihandler;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.event.CalculateImUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoadLocalChatsEvent;
import com.aspirecn.corpsocial.bundle.im.repository.ChatDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.AppBroadcastManager;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Autowired;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 加载本地对话列表处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LoadLocalChatsEvent.class)
public class LoadLocalChatsEventHandler implements IHandler<List<Chat>, LoadLocalChatsEvent> {

    @Autowired
    private ChatDao chatDao = new ChatDao();

    private AppBroadcastManager appBroadcastManager = AppBroadcastManager
            .getInstance();

    private OsgiServiceLoader osgiServiceLoader = OsgiServiceLoader
            .getInstance();

    @Override
    public LinkedList<Chat> handle(LoadLocalChatsEvent busEvent) {
        LinkedList<Chat> chats = new LinkedList<Chat>();
        int index = busEvent.getIndex();
        int count = busEvent.getCount();

        int totalUnReadCount = 0;

        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("belongUserId", Config.getInstance().getUserId());
        wheres.put("display", true);
        List<ChatEntity> findChats = chatDao.findAllByWhereAndIndex(wheres,
                index, count, "moveToTopTime DESC, lastUpdateTime DESC");

        for (ChatEntity chatEntity : findChats) {
            int chatType = chatEntity.getChatType();
            String chatName = chatEntity.getChatName();
            if (ChatType.P2P.value == chatType && TextUtils.isEmpty(chatName)) {
                String chatId = chatEntity.getChatId();
                User contact = getUser(chatId);
                if (contact != null) {
                    chatEntity.setChatName(contact.getName());
                }
            }
            Chat chat = new Chat();
            chat.setChatEntity(chatEntity);
            chats.addLast(chat);

            totalUnReadCount += chat.doGetUnReadCount();
        }
        notifyCommonModuleUnReadCount(totalUnReadCount, index);

        return chats;
    }

    private void notifyCommonModuleUnReadCount(int unReadCount, int index) {
//        Intent unReadCountIntent = new Intent("commonNotifyReceiver");
//        Bundle unReadCountIntentBundle = new Bundle();
//        unReadCountIntentBundle.putString("action", "imUnReadCountNotify");
//
//        Bundle unReadCountData = new Bundle();
//        unReadCountData.putBoolean("totalCount", index == 0 ? true : false);
//        unReadCountData.putInt("unReadCount", unReadCount);
//        unReadCountIntentBundle.putBundle("data", unReadCountData);
//
//        unReadCountIntent.putExtras(unReadCountIntentBundle);
//        appBroadcastManager.sendBroadcast(unReadCountIntent);
        UiEventHandleFacade.getInstance().handle(new CalculateImUnReadMsgCountEvent());
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
