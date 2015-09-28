package com.aspirecn.corpsocial.bundle.im.presenter;

import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.FindContactBatchRespSubject;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupSubject;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupSubject;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberSubject;
import com.aspirecn.corpsocial.bundle.im.listener.QuitGroupSubject;
import com.aspirecn.corpsocial.bundle.im.listener.ReceiveMessageSubject;
import com.aspirecn.corpsocial.bundle.im.listener.SendMessageRespSubject;
import com.aspirecn.corpsocial.bundle.im.listener.SyncMessageRespSubject;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;

/**
 * Created by yinghuihong on 15/7/16.
 */
public interface ChatPresenter extends
        SendMessageRespSubject.SendMessageRespEventListener,
        ReceiveMessageSubject.ReceiveMessageEventListener,
        CreateUpdateGroupSubject.CreateUpdateGroupEventListener,
        DismissGroupSubject.DismissGroupEventListener,
        KickOutGroupMemberSubject.KickOutGroupMemberEventListener,
        SyncMessageRespSubject.SyncMessageRespEventListener,
        CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener,
        AddFriendRespSubject.AddFriendRespEventListener,
        QuitGroupSubject.QuitGroupEventListener,
        FindContactBatchRespSubject.FindContactBatchRespEventListener {

    UiEventHandleFacade uiEventHandleFacade = UiEventHandleFacade.getInstance();

    void initData(String corpId, String chatId, String chatName, int chatType);

    void doLoadChat();

    void onClickActionbarRightBtn();

    void sendTextMsg(String textMst);

    void takeAPhoto();

    void sendPictureMsg();

    void sendPictureMsg(String path);

    void sendLocationMsg(String imagePath, String latitude, String longitude);

    void sendVideoMsg(String videoFilePath);

    void refreshMsgList(boolean showLastItem);

    void onClickSendAgain(int position);

    void startRecordVoice();

    void stopRecordVoice();

    void sendVoiceMsg(int time);

    void onClickLocationSelectBtn();

    void onRestart();

    void doDeleteMessage(String msgId);

    int getThemeColor();
}
