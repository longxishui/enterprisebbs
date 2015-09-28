package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.PictureMsgViewHolder;
import com.aspiren.corpsocial.R;

import java.io.File;
import java.util.ArrayList;

import cn.trinea.android.common.util.JSONUtils;

public class PictureMsgItem extends MsgItem {

    private static MsgItem sInstance;

    public static MsgItem getInstance() {
        if (sInstance == null) {
            sInstance = new PictureMsgItem();
        }
        return sInstance;
    }

    public View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof PictureMsgViewHolder)) {
            PictureMsgViewHolder holder = new PictureMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_picture_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            holder.receiveLayout = convertView
                    .findViewById(R.id.common_chat_item_picture_receive_layout_rl);
            holder.receiveHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_picture_receive_head_iv);
            holder.receiveNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_picture_receive_name_tv);
            holder.receiveRelationIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_receive_relation_icon_iv);
            holder.receivePictureIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_picture_receive_picture_iv);
            holder.receivePercentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_picture_receive_percent_tv);

            holder.sendLayout = convertView
                    .findViewById(R.id.common_chat_item_picture_send_layout_rl);
            holder.sendHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_picture_send_head_iv);
            holder.sendNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_picture_send_name_tv);
            holder.sendPictureIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_picture_send_picture_iv);
            holder.sendPercentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_picture_send_percent_tv);
            holder.sendAgainBT = (Button) convertView
                    .findViewById(R.id.common_chat_item_picture_send_again_bt);
            holder.sendSendingPB = (ProgressBar) convertView
                    .findViewById(R.id.common_chat_item_picture_sending_pb);

            convertView.setTag(holder);

            picDefaultSizePix = getViewHeight(holder.receivePictureIV);
        }
        return convertView;
    }

    @Override
    public void fillContent(final Context context, View convertView, BaseAdapter adapter, final Chat chat, int position, OnLongClickHeadImgListener longClickListener, OnClickSendAgainListener clickListener) {

        PictureMsgViewHolder holder = (PictureMsgViewHolder) convertView.getTag();
        final MsgEntity msg = (MsgEntity) adapter.getItem(position);

        setCommonInfo(holder, msg, longClickListener);

        //自己发出的消息
        if (msg.isOwned()) {
            setHeadImageAndName(holder.sendHeadIconIV, msg, holder.sendNameTV, null);
            holder.receiveRelationIconIV.setVisibility(View.GONE);
            double p = 100 * JSONUtils.getDouble(msg.getExtraInfo(), "percent", 0);
            if (msg.getStatus() == MsgEntity.MsgStatus.Sending.value) {
                holder.sendPercentTV.setText(String.valueOf(String.format("%.1f", p) + "%"));
                holder.sendPercentTV.setSelected(true);
            } else {
                holder.sendPercentTV.setText("");
                holder.sendPercentTV.setSelected(false);
            }
            holder.sendPercentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPictures(context, chat, msg);
                }
            });
            holder.sendPercentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));

            setImage(getPictureMsgUrl(msg), holder.sendPictureIV);

            setMsgStatus(position, msg, holder, clickListener);
        }
        //收到的消息
        else {
            setHeadImageAndName(holder.receiveHeadIconIV, msg, holder.receiveNameTV, holder.receiveRelationIconIV);
            holder.receivePercentTV.setText("");
            holder.receivePercentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPictures(context, chat, msg);
                }
            });
            holder.receivePercentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));

            setImage(getPictureMsgUrl(msg), holder.receivePictureIV);
        }
    }

    private String getPictureMsgUrl(MsgEntity msg) {
        //网络地址
        if (!TextUtils.isEmpty(msg.getContent()) && msg.getContent().startsWith("http")) {
            return msg.getContent();
        }
        //本地路径
        else if (!TextUtils.isEmpty(msg.getContent()) && new File(msg.getContent()).exists()) {
            return "file://" + msg.getContent();
        }
        //从扩展文件信息获取网络地址
        else {
            return "";
//            return ExtraInfo.getInstance().fromJsonExtraInfo(msg.getExtraInfo()).getFileInfo().url;
        }
    }

    /**
     * 显示图片集
     *
     * @param msg
     */
    private void showPictures(Context mContext, Chat mChat, MsgEntity msg) {
        if (msg.getStatus() != MsgEntity.MsgStatus.Success.value) {
            return;
        }
        Intent intent = new Intent(mContext,
                com.aspirecn.corpsocial.common.ui.component.imageloader.ImagePagerActivity.class);
        // 生成图片url地址列表
        ArrayList<String> urls = new ArrayList<String>();

        int currentCount = 0;
        int pictureWallPosition = 0;
        for (int i = 0; i < mChat.getMsgList().size(); i++) {
            MsgEntity msg1 = mChat.getMsgList().get(i);
            if (ContentType.PICTURE.getValue() == msg1.getContentType()) {
//                ExtraInfo extraInfo = ExtraInfo.getInstance().fromJsonExtraInfo(msg1.getExtraInfo());
//                if (!TextUtils.isEmpty(extraInfo.getFileInfo().orginalUrl)) {
//                    urls.add(extraInfo.getFileInfo().orginalUrl);
                    currentCount += 1;
                }
                if (msg1.getMsgId().equals(msg.getMsgId())) {
                    pictureWallPosition = currentCount - 1;
                }
            }
        }
//        intent.putExtra("position", pictureWallPosition);
//        intent.putStringArrayListExtra("urls", urls);
//        mContext.startActivity(intent);
//    }
}
