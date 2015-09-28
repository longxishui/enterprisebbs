package com.aspirecn.corpsocial.bundle.im.view;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;

/**
 * Created by yinghuihong on 15/7/16.
 */
public interface ChatView {

    void showToast(String msg);

    void notifyDataChanged(Chat chat, boolean showLastItem);

    void setSpeakPower(Chat chat, boolean isSpeak);

    void initPullToRefreshListView(Chat chat);

    void startP2PChatSettingActivity(String mChatId, String mCorpId, String mChatName);

    void startGroupChatSettingActivity(String mChatId, String mCorpId, String mChatName);

    void takeAPhoto(String pictureFilePath);

    void doFinish();

    void startRecordVoice(final int mThemeColor, final String voiceFilePath);

    void stopRecordVoice(final int mThemeColor);

    void startLocationActivity(String path);

    void closeCustomDialog();
}
