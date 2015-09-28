package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.RichMsgViewHolder;
import com.aspiren.corpsocial.R;

public class RichMsgItemView {

    public static View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof RichMsgViewHolder)) {
            RichMsgViewHolder holder = new RichMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_rich_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            // single
            holder.singleLayout = convertView.findViewById(R.id.common_chat_item_single_layout);

            holder.singleTitleLyout = convertView
                    .findViewById(R.id.common_chat_item_title_layout_rl);
            holder.singleTitleIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_title_icon_iv);
            holder.singleTitleTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_title_tv);
            holder.singleSubTitleTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_sub_title_tv);
            holder.singlePicLayout = convertView
                    .findViewById(R.id.common_chat_item_pic_layout_rl);
            holder.singlePicIconIV = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_pic_icon_iv);
            holder.singlePicDescrTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_pic_descr_tv);
            holder.singleContentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_tv);

            holder.singleActionLayout0 = convertView
                    .findViewById(R.id.common_chat_item_action_layout0_rl);
            holder.singleActionDescrTV0 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_action_descr0_tv);
            holder.singleActionBtnBT0 = (Button) convertView
                    .findViewById(R.id.common_chat_item_action_btn0_bt);

            holder.singleActionLayout1 = convertView
                    .findViewById(R.id.common_chat_item_action_layout1_rl);
            holder.singleActionDescrTV1 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_action_descr1_tv);
            holder.singleActionBtnBT1 = (Button) convertView
                    .findViewById(R.id.common_chat_item_action_btn1_bt);

            holder.singleActionLayout2 = convertView
                    .findViewById(R.id.common_chat_item_action_layout2_rl);
            holder.singleActionDescrTV2 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_action_descr2_tv);
            holder.singleActionBtnBT2 = (Button) convertView
                    .findViewById(R.id.common_chat_item_action_btn2_bt);

            holder.singleStateTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_state_tv);
            holder.singleReadAllTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_read_all_tv);

            // mult
            holder.multLayout = convertView.findViewById(R.id.common_chat_item_mult_layout);

            holder.multContentLayout0 = convertView
                    .findViewById(R.id.common_chat_item_content_layout0_rl);
            holder.multContentDescrTV00 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr00_tv);
            holder.multContentIconIV0 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon0_iv);
            holder.multContentDescrTV0 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr0_tv);

            holder.multContentLayout1 = convertView
                    .findViewById(R.id.common_chat_item_content_layout1_rl);
            holder.multContentIconIV1 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon1_iv);
            holder.multContentDescrTV1 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr1_tv);

            holder.multContentLayout2 = convertView
                    .findViewById(R.id.common_chat_item_content_layout2_rl);
            holder.multContentIconIV2 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon2_iv);
            holder.multContentDescrTV2 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr2_tv);

            holder.multContentLayout3 = convertView
                    .findViewById(R.id.common_chat_item_content_layout3_rl);
            holder.multContentIconIV3 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon3_iv);
            holder.multContentDescrTV3 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr3_tv);

            holder.multContentLayout4 = convertView
                    .findViewById(R.id.common_chat_item_content_layout4_rl);
            holder.multContentIconIV4 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon4_iv);
            holder.multContentDescrTV4 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr4_tv);

            holder.multContentLayout5 = convertView
                    .findViewById(R.id.common_chat_item_content_layout5_rl);
            holder.multContentIconIV5 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon5_iv);
            holder.multContentDescrTV5 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr5_tv);

            holder.multContentLayout6 = convertView
                    .findViewById(R.id.common_chat_item_content_layout6_rl);
            holder.multContentIconIV6 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon6_iv);
            holder.multContentDescrTV6 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr6_tv);

            holder.multContentLayout7 = convertView
                    .findViewById(R.id.common_chat_item_content_layout7_rl);
            holder.multContentIconIV7 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon7_iv);
            holder.multContentDescrTV7 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr7_tv);

            holder.multContentLayout8 = convertView
                    .findViewById(R.id.common_chat_item_content_layout8_rl);
            holder.multContentIconIV8 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon8_iv);
            holder.multContentDescrTV8 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr8_tv);

            holder.multContentLayout9 = convertView
                    .findViewById(R.id.common_chat_item_content_layout9_rl);
            holder.multContentIconIV9 = (ImageView) convertView
                    .findViewById(R.id.common_chat_item_content_icon9_iv);
            holder.multContentDescrTV9 = (TextView) convertView
                    .findViewById(R.id.common_chat_item_content_descr9_tv);

            convertView.setTag(holder);
        }
        return convertView;
    }

}
