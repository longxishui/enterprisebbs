package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.TextMsgViewHolder;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspiren.corpsocial.R;

public class TextMsgItem extends MsgItem {

    private static MsgItem sInstance;

    public static MsgItem getInstance() {
        if (sInstance == null) {
            sInstance = new TextMsgItem();
        }
        return sInstance;
    }


    public View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof TextMsgViewHolder)) {
            TextMsgViewHolder holder = new TextMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_text_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            holder.receiveLayout = convertView
                    .findViewById(R.id.common_chat_item_text_receive_layout_rl);
            holder.receiveHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_text_receive_head_iv);
            holder.receiveNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_text_receive_name_tv);
            holder.receiveRelationIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_receive_relation_icon_iv);
            holder.receiveContentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_text_receive_content_tv);

            holder.sendLayout = convertView
                    .findViewById(R.id.common_chat_item_text_send_layout_rl);
            holder.sendHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_text_send_head_iv);
            holder.sendNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_text_send_name_tv);
            holder.sendContentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_text_send_content_tv);
            holder.sendAgainBT = (Button) convertView
                    .findViewById(R.id.common_chat_item_text_send_again_bt);
            holder.sendSendingPB = (ProgressBar) convertView
                    .findViewById(R.id.common_chat_item_text_sending_pb);

            convertView.setTag(holder);
        }
        return convertView;
    }

    /**
     * 设置文本消息
     *
     * @param position
     * @param convertView
     */
    public void fillContent(Context context, View convertView, BaseAdapter adapter, Chat chat, int position,
                            final OnLongClickHeadImgListener mLongClickListener,
                            final OnClickSendAgainListener mClickListener) {
        TextMsgViewHolder holder = (TextMsgViewHolder) convertView.getTag();
        final MsgEntity msg = (MsgEntity) adapter.getItem(position);

        setCommonInfo(holder, msg, mLongClickListener);

        SpannableString spannableString = FaceConversionUtil.getInstace()
                .getExpressionString(context, msg.getContent());

        // 自己发送的文本消息
        if (msg.isOwned()) {
            setHeadImageAndName(holder.sendHeadIconIV, msg, holder.sendNameTV, null);
            holder.receiveRelationIconIV.setVisibility(View.GONE);
            holder.sendContentTV.setText(spannableString);
            holder.sendContentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));
            setMsgStatus(position, msg, holder, mClickListener);
        }
        // 接收到的文本消息
        else {
            setHeadImageAndName(holder.receiveHeadIconIV, msg, holder.receiveNameTV, holder.receiveRelationIconIV);
            holder.receiveContentTV.setText(spannableString);
            holder.receiveContentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));
        }
    }

}
