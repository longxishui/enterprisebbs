package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.ui.chatitemview.LocationMsgItem;
import com.aspirecn.corpsocial.common.ui.chatitemview.MsgItem;
import com.aspirecn.corpsocial.common.ui.chatitemview.PictureMsgItem;
import com.aspirecn.corpsocial.common.ui.chatitemview.TextMsgItem;
import com.aspirecn.corpsocial.common.ui.chatitemview.TextTipsMsgItem;
import com.aspirecn.corpsocial.common.ui.chatitemview.VoiceMsgItem;

import java.util.LinkedList;

/**
 * 聊天主界面适配器
 *
 * @author lihaiqiang
 */
public class ChatAdapter extends BaseAdapter {

    private Context mContext;

    private Chat mChat;

    private MsgItem.OnClickSendAgainListener mClickListener;

    private MsgItem.OnLongClickHeadImgListener mLongClickListener;

    public ChatAdapter(Context context) {
        mContext = context;
    }

    public ChatAdapter(Context context, Chat chat) {
        mContext = context;
        mChat = chat;

    }

    public void setData(LinkedList<MsgEntity> msgEntities) {
        if (mChat == null) {
            mChat = new Chat();
        }
        mChat.setMsgList(msgEntities);
    }

    @Override
    public int getCount() {
        if (mChat != null && mChat.getMsgList() != null) {
            return mChat.getMsgList().size();
        }
        return 0;
    }

    @Override
    public MsgEntity getItem(int position) {
        return mChat.getMsgList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        return mChat.getMsgList().get(position).getContentType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        int itemType = getItemViewType(position);

        MsgItem msgItem;
        //文本
        if (itemType == ContentType.TEXT.getValue()) {
            msgItem = TextMsgItem.getInstance();
        }
        //语音
        else if (itemType == ContentType.VOICE.getValue()) {
            msgItem = VoiceMsgItem.getInstance();
        }
        //图片
        else if (itemType ==ContentType.PICTURE.getValue()) {
            msgItem = PictureMsgItem.getInstance();
        }
        //位置
        else if (itemType == ContentType.POSITION.getValue()) {
            msgItem = LocationMsgItem.getInstance();
        }
        //系统提示信息
        else if (itemType == ContentType.SYSTEM.getValue()) {
            msgItem = TextTipsMsgItem.getInstance();
        }
        //扩展类型
        else {
            msgItem = TextTipsMsgItem.getInstance();
        }

        convertView = msgItem.getView(mContext, convertView);
        msgItem.fillContent(mContext, convertView, this, mChat, position, mLongClickListener, mClickListener);
        return convertView;
    }


    public void setOnClickSendAgainListener(MsgItem.OnClickSendAgainListener listener) {
        this.mClickListener = listener;
    }


    public void setOnLongClickHeadImgListener(MsgItem.OnLongClickHeadImgListener listener) {
        this.mLongClickListener = listener;
    }


}
