package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.VoiceMsgViewHolder;
import com.aspirecn.corpsocial.common.util.MediaRecordAndPlayer;
import com.aspiren.corpsocial.R;

import cn.trinea.android.common.util.JSONUtils;

public class VoiceMsgItem extends MsgItem {


    private static MsgItem sInstance;

    public static MsgItem getInstance() {
        if (sInstance == null) {
            sInstance = new VoiceMsgItem();
        }
        return sInstance;
    }

    public View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof VoiceMsgViewHolder)) {
            VoiceMsgViewHolder holder = new VoiceMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_voice_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            holder.receiveLayout = convertView
                    .findViewById(R.id.common_chat_item_voice_receive_layout_rl);
            holder.receiveHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_voice_receive_head_iv);
            holder.receiveNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_voice_receive_name_tv);
            holder.receiveRelationIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_receive_relation_icon_iv);
            holder.receiveContentLayout = convertView
                    .findViewById(R.id.common_chat_item_voice_receive_content_layout_ll);
            holder.receiveVoiceIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_voice_receive_voice_icon_tv);
            holder.receiveVoiceTimeTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_voice_receive_voice_time_tv);

            holder.sendLayout = convertView
                    .findViewById(R.id.common_chat_item_voice_send_layout_rl);
            holder.sendHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_voice_send_head_iv);
            holder.sendNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_voice_send_name_tv);
            holder.sendContentLayout = convertView
                    .findViewById(R.id.common_chat_item_voice_send_content_layout_ll);
            holder.sendVoiceIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_voice_send_voice_icon_tv);
            holder.sendVoiceTimeTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_voice_send_voice_time_tv);
            holder.sendAgainBT = (Button) convertView
                    .findViewById(R.id.common_chat_item_voice_send_again_bt);
            holder.sendSendingPB = (ProgressBar) convertView
                    .findViewById(R.id.common_chat_item_voice_sending_pb);

            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    public void fillContent(Context context, View convertView, BaseAdapter adapter, Chat chat, int position, OnLongClickHeadImgListener longClickListener, OnClickSendAgainListener clickListener) {

        final VoiceMsgViewHolder holder = (VoiceMsgViewHolder) convertView.getTag();
        final MsgEntity msg = (MsgEntity) adapter.getItem(position);

        setCommonInfo(holder, msg, longClickListener);

        //自己发出的消息
        if (msg.isOwned()) {
            setHeadImageAndName(holder.sendHeadIconIV, msg, holder.sendNameTV, null);
            holder.receiveRelationIconIV.setVisibility(View.GONE);
            String voiceTime = JSONUtils.getString(msg.getExtraInfo(), "voiceTime", "0\"");
            holder.sendVoiceTimeTV.setText(voiceTime);
            holder.sendContentLayout.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));
            setMsgStatus(position, msg, holder, clickListener);
        }
        //收到的消息
        else {
            setHeadImageAndName(holder.receiveHeadIconIV, msg, holder.receiveNameTV, holder.receiveRelationIconIV);
            String voiceTime = JSONUtils.getString(msg.getExtraInfo(), "voiceTime", "0\"");
            holder.receiveVoiceTimeTV.setText(voiceTime);
            holder.receiveContentLayout.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));
        }
        //语音播放设置
        setPlayVoice(msg, holder);
    }

    /**
     * 语音播放设置
     *
     * @param msg
     * @param holder
     */
    private void setPlayVoice(final MsgEntity msg, final VoiceMsgViewHolder holder) {
        final int direction = msg.isOwned() ? 1 : 0;//是自己的消息为右侧
        final String filePath = msg.getContent();

        final MediaRecordAndPlayer player = MediaRecordAndPlayer.getInstance();

        final ImageView iconIv = msg.isOwned() ? holder.sendVoiceIconIV : holder.receiveVoiceIconIV;
        player.refreshVoiceAnimation(filePath, iconIv, direction);

        View view = msg.isOwned() ? holder.sendContentLayout : holder.receiveContentLayout;
        //点击播放或暂停语音
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.startPlay(filePath, iconIv, direction);
            }
        });
    }
}
