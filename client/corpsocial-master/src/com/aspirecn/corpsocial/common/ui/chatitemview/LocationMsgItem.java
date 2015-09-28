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
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.LocationMsgViewHolder;
import com.aspiren.corpsocial.R;

import java.io.File;

import cn.trinea.android.common.util.JSONUtils;

public class LocationMsgItem extends MsgItem {

    private static MsgItem sInstance;

    public static MsgItem getInstance() {
        if (sInstance == null) {
            sInstance = new LocationMsgItem();
        }
        return sInstance;
    }

    public View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof LocationMsgViewHolder)) {
            LocationMsgViewHolder holder = new LocationMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_location_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            holder.receiveLayout = convertView
                    .findViewById(R.id.common_chat_item_location_receive_layout_rl);
            holder.receiveHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_location_receive_head_iv);
            holder.receiveNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_location_receive_name_tv);
            holder.receiveRelationIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_receive_relation_icon_iv);
            holder.receivePictureIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_location_receive_picture_iv);
            holder.receivePercentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_location_receive_percent_tv);

            holder.sendLayout = convertView
                    .findViewById(R.id.common_chat_item_location_send_layout_rl);
            holder.sendHeadIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_location_send_head_iv);
            holder.sendNameTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_location_send_name_tv);
            holder.sendPictureIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_location_send_picture_iv);
            holder.sendPercentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_location_send_percent_tv);
            holder.sendAgainBT = (Button) convertView
                    .findViewById(R.id.common_chat_item_location_send_again_bt);
            holder.sendSendingPB = (ProgressBar) convertView
                    .findViewById(R.id.common_chat_item_location_sending_pb);

            convertView.setTag(holder);

            picDefaultSizePix = getViewHeight(holder.receivePictureIV);
        }
        return convertView;
    }

    @Override
    public void fillContent(final Context context, View convertView, BaseAdapter adapter, Chat chat, int position, OnLongClickHeadImgListener longClickListener, OnClickSendAgainListener clickListener) {
        LocationMsgViewHolder holder = (LocationMsgViewHolder) convertView.getTag();
        final MsgEntity msg = (MsgEntity) adapter.getItem(position);

        setCommonInfo(holder, msg, longClickListener);


        //自己发的位置信息
        if (msg.isOwned()) {
            setHeadImageAndName(holder.sendHeadIconIV, msg, holder.sendNameTV, null);
            holder.receiveRelationIconIV.setVisibility(View.GONE);
            double p = 100 * JSONUtils.getDouble(msg.getExtraInfo(), "percent", 0);
            if (msg.getStatus() == MsgEntity.MsgStatus.Sending.value) {
                holder.sendPercentTV.setText(String.valueOf(String.format("%.1f", p) + "%"));
                holder.receivePercentTV.setSelected(true);
            } else {
                holder.sendPercentTV.setText("");
                holder.receivePercentTV.setSelected(false);
            }
            holder.sendPercentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLocationMap(context, msg);
                }
            });
            holder.sendPercentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));

//            setImage(getLocationMsgUrl(msg), holder.sendPictureIV);

            setMsgStatus(position, msg, holder, clickListener);
        }
        //收到的位置信息
        else {
            setHeadImageAndName(holder.receiveHeadIconIV, msg, holder.receiveNameTV, holder.receiveRelationIconIV);
            holder.receivePercentTV.setText("");
            holder.receivePercentTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLocationMap(context, msg);
                }
            });
            holder.receivePercentTV.setOnLongClickListener(new OnItemLongClickListener(adapter, chat, msg));

//            setImage(getLocationMsgUrl(msg), holder.receivePictureIV);
        }
    }

    /**
     * 打开位置地图
     *
     * @param msg
     */
    private void openLocationMap(Context mContext, MsgEntity msg) {
//        Intent mIntent = new Intent(mContext, LocationActivity.class);
//        mIntent.putExtra("position", msg.getContent());
//        mContext.startActivity(mIntent);
    }


}
