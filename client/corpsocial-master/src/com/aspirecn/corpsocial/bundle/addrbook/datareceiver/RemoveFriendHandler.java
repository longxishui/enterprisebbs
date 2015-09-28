package com.aspirecn.corpsocial.bundle.addrbook.datareceiver;

import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.common.eventbus.CommandData;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;

/**
 * Created by Amos on 15-6-26.
 */
@EventBusAnnotation.DataReceiveHandler(commandType = CommandData.CommandType.MODIFY_FREQUENTLY_CONTACT)
public class RemoveFriendHandler implements IHandler<Null, CommandData> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    private UserDao userDao = new UserDao();

    @Override
    public Null handle(CommandData args) {
//        String userId = args.getCommandHeader().userId;
//        userDao.canceledFriend(userId);
//        eventListener.notifyListener(new RemoveFriendRespEvent(userId, 0, 1));
        return new Null();
    }

}
