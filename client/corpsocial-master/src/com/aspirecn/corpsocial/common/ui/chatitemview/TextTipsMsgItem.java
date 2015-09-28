package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendEvent;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.TextTipsMsgViewHolder;
import com.aspirecn.corpsocial.common.ui.widget.CustomAlertDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspiren.corpsocial.R;

import org.xml.sax.XMLReader;

public class TextTipsMsgItem extends MsgItem {

    private static MsgItem sInstance;

    public static MsgItem getInstance() {
        if (sInstance == null) {
            sInstance = new TextTipsMsgItem();
        }
        return sInstance;
    }

    public View getView(Context context, View convertView) {
        if (convertView == null || !(convertView.getTag() instanceof TextTipsMsgViewHolder)) {
            TextTipsMsgViewHolder holder = new TextTipsMsgViewHolder();
            convertView = View.inflate(context, R.layout.common_chat_item_text_tips_msg, null);

            holder.timeTV = (TextView) convertView.findViewById(R.id.common_chat_item_time_tv);

            holder.contentTV = (TextView) convertView
                    .findViewById(R.id.common_chat_item_text_tips_content_tv);

            convertView.setTag(holder);
        }
        return convertView;
    }

    @Override
    public void fillContent(Context context, View convertView, BaseAdapter adapter, Chat chat, int position, OnLongClickHeadImgListener longClickListener, OnClickSendAgainListener clickListener) {
        TextTipsMsgViewHolder holder = (TextTipsMsgViewHolder) convertView.getTag();
        MsgEntity msg = (MsgEntity) adapter.getItem(position);

        holder.timeTV.setText(DateUtils.getWeiXinTime(msg.getCreateTime()));
        holder.contentTV.setText(Html.fromHtml(msg.getContent(), null, new AddFriendTagHandler(context, chat)));
        holder.contentTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.contentTV.setClickable(true);
    }

    /**
     * 显示添加好友确认框
     */
    private void showAddFriendDialog(final Context mContext, final Chat mChat) {
        final CustomAlertDialog dialog = new CustomAlertDialog(mContext);
        final String name = TextUtils.isEmpty(mChat.doGeChatName()) ? "他（她）" : mChat.doGeChatName();
        dialog.setAlertMsg("确定向" + name + "发起添加好友邀请？");
        dialog.setBtn1Text("取消");
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setBtn2Text("添加");
        dialog.getBtn2().setTextColor(ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor));
        dialog.setBtn2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                User user = UserUtil.getUser(mChat.doGetChatId());
                if (user != null) {
                    CustomDialog.showProgeress(mContext);
                    AddFriendEvent event = new AddFriendEvent();
                    event.setUser(user);
                    UiEventHandleFacade.getInstance().handle(event);
                } else {
                    Toast.makeText(mContext, "获取" + name + "的信息失败，请退出当前页面重新进入再试。",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    /**
     * 标签文本处理类
     */
    private class AddFriendTagHandler implements Html.TagHandler {

        private int startIndex = 0;
        private int stopIndex = 0;

        private Context mContext;
        private Chat mChat;

        AddFriendTagHandler(Context context, Chat chat) {
            mContext = context;
            mChat = chat;
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equalsIgnoreCase("add_friend")) {
                if (opening) {
                    startAddFriend(tag, output, xmlReader);
                } else {
                    endAddFriend(tag, output, xmlReader);
                }
            }
        }

        public void startAddFriend(String tag, Editable output, XMLReader xmlReader) {
            startIndex = output.length();
        }

        public void endAddFriend(String tag, Editable output, XMLReader xmlReader) {
            stopIndex = output.length();
            output.setSpan(new AddFriendSpan(), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private class AddFriendSpan extends ClickableSpan implements View.OnClickListener {

            @Override
            public void updateDrawState(TextPaint ds) {
                //文字颜色
                ds.setColor(ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor));
                //无下划线
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View view) {
                showAddFriendDialog(mContext, mChat);
            }
        }
    }
}
