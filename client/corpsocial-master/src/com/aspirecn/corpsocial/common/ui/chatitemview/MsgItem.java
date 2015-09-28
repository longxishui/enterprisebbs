package com.aspirecn.corpsocial.common.ui.chatitemview;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.ui.ChatActivity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.ui.chatitemview.viewholder.ViewHolder;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by yinghuihong on 15/8/12.
 */
public abstract class MsgItem {

    //图片默认尺寸(在子类getView()方法中根据控件在布局中的高度设置确定)
    protected int picDefaultSizePix = 280;

    //通用图片显示参数
    protected DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .showImageForEmptyUri(R.drawable.common_no_img_small)
            .showImageOnFail(R.drawable.common_no_img_small)
            .build();

    private DisplayImageOptions headOptions = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
            .showImageForEmptyUri(R.drawable.addrbook_contact)
            .showImageOnFail(R.drawable.addrbook_contact)
            .build();

    public abstract View getView(Context context, View convertView);

    public abstract void fillContent(Context context, View convertView, BaseAdapter adapter, Chat chat, int position,
                                     final OnLongClickHeadImgListener longClickListener,
                                     final OnClickSendAgainListener clickListener);

    /**
     * 设置共同信息
     *
     * @param holder
     * @param msg
     */
    protected void setCommonInfo(ViewHolder holder, final MsgEntity msg, final OnLongClickHeadImgListener mLongClickListener) {
        holder.timeTV.setText(DateUtils.getWeiXinTime(msg.getCreateTime()));
        holder.timeTV.setVisibility(msg.isShowCreateTime() ? View.VISIBLE : View.GONE);
        holder.receiveLayout.setVisibility(msg.isOwned() ? View.GONE : View.VISIBLE);
        holder.sendLayout.setVisibility(msg.isOwned() ? View.VISIBLE : View.GONE);
        holder.receiveHeadIconIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(msg);
                }
                return true;
            }
        });
        holder.sendHeadIconIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(msg);
                }
                return true;
            }
        });
    }

    /**
     * 消息状态
     *
     * @param msg
     * @param holder
     */
    protected void setMsgStatus(final int position, final MsgEntity msg, ViewHolder holder, final OnClickSendAgainListener clickListener) {
        if (msg.getStatus() == MsgEntity.MsgStatus.Sending.value) {
            holder.sendSendingPB.setVisibility(View.VISIBLE);
            holder.sendAgainBT.setVisibility(View.GONE);

        } else if (msg.getStatus() == MsgEntity.MsgStatus.Fail.value) {
            holder.sendSendingPB.setVisibility(View.GONE);
            holder.sendAgainBT.setVisibility(View.VISIBLE);
            //重发
            holder.sendAgainBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClickSendAgain(position);
                    }
                }
            });

        } else {
            holder.sendSendingPB.setVisibility(View.GONE);
            holder.sendAgainBT.setVisibility(View.GONE);
        }
    }

    /**
     * 设置自己消息头像
     *
     * @param iv
     * @param msg
     */
    protected void setHeadImageAndName(ImageView iv, final MsgEntity msg, TextView tv, ImageView myFriendIcon) {
        final Context mContext = iv.getContext();
        final User user = UserUtil.getUser(msg.getOwnedUserId());
        if (user != null) {
            ImageLoader.getInstance().displayImage(user.getUrl(), iv, headOptions);
            tv.setText(user.getName());
            if (myFriendIcon != null && !user.canCommunicate()) {
                myFriendIcon.setVisibility(View.VISIBLE);
            } else if (myFriendIcon != null) {
                myFriendIcon.setVisibility(View.GONE);
            }
        } else {
            ImageLoader.getInstance().displayImage("", iv, headOptions);
            tv.setText(msg.getOwnedUserId());
            if (myFriendIcon != null) {
                myFriendIcon.setVisibility(View.VISIBLE);
            }
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    Intent intent = new Intent();
                    if (user.getCorpList() != null && user.getCorpList().size() > 1) {
                        intent.setClass(mContext, AddrbookGroupChoose_.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("user", user);
                        intent.putExtras(mBundle);
                    } else {
                        intent.putExtra("ContactId", user.getUserid());
                        intent.putExtra("corpId", user.getCorpId());
                        intent.setClass(mContext, AddrbookPersonalParticularsActivity_.class);
                    }
                    mContext.startActivity(intent);
                }
            }
        });
        tv.setVisibility(View.VISIBLE);
    }

    public int getViewHeight(View view) {
        return view.getLayoutParams().height;
    }

    public int getPicDefaultWidth() {
        return picDefaultSizePix * 3 / 4;
    }

    public int getPicDefaultHeight() {
        return picDefaultSizePix;
    }

    public int getPicMaxSize() {
        return picDefaultSizePix;
    }

    public int getPicMinSize() {
        return picDefaultSizePix / 4;
    }

    protected void setImage(String uri, ImageView iv) {
        ImageLoader.getInstance().displayImage(uri, iv, options, new LoadingListener());
    }

    /**
     * 按比例设置图片尺寸
     *
     * @param iv
     * @param loadedImage
     */
    private void setImageSize(View iv, Bitmap loadedImage) {
        int viewWidth;
        int viewHeight;

        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();

        int picWidth = 0;
        int picHeight = 0;
        if (loadedImage != null) {
            picWidth = loadedImage.getWidth();
            picHeight = loadedImage.getHeight();
        }

        float widthScale = (float) picWidth / getPicMaxSize();
        float heightScale = (float) picHeight / getPicMaxSize();
        float scale = widthScale > heightScale ? widthScale : heightScale;
        viewWidth = (int) (picWidth / scale);
        viewHeight = (int) (picHeight / scale);

        if (viewWidth > 0 && viewHeight > 0) {
            layoutParams.width = viewWidth > getPicMinSize() ? viewWidth : getPicMinSize();
            layoutParams.height = viewHeight > getPicMinSize() ? viewHeight : getPicMinSize();
        } else {
            layoutParams.width = getPicDefaultWidth();
            layoutParams.height = getPicDefaultHeight();
        }

        iv.setLayoutParams(layoutParams);
    }

    /**
     * 头像长按回调
     */
    public interface OnLongClickHeadImgListener {
        void onLongClick(MsgEntity msg);
    }

    /**
     * 消息重发回调
     */
    public interface OnClickSendAgainListener {
        void onClickSendAgain(int position);
    }

    /**
     * 长按消息处理类
     */
    public class OnItemLongClickListener implements View.OnLongClickListener {

        private BaseAdapter mAdapter;
        private Chat mChat;

        private MsgEntity mMsg;

        public OnItemLongClickListener(BaseAdapter adapter, Chat chat, MsgEntity msg) {
            mAdapter = adapter;
            mChat = chat;
            mMsg = msg;
        }

        @Override
        public boolean onLongClick(View v) {
            final Context mContext = v.getContext();
            final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
            dialog.setContentView(R.layout.im_chat_msg_detail_dialog);
            //主题颜色
            int themeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
            // 标题
            TextView titleTv = (TextView) dialog.findViewById(R.id.title_item_tv);
            titleTv.setText(mMsg.getOwnedUserName());
            titleTv.setTextColor(themeColor);
            //分隔线
            View dividerLine = dialog.findViewById(R.id.title_divider_line_v);
            dividerLine.setBackgroundColor(themeColor);
            // 复制
            TextView copyTv = (TextView) dialog.findViewById(R.id.copy_item_tv);
            // 非文本信息隐藏复制功能
            if (mMsg.getContentType() != ContentType.TEXT.getValue()) {
                copyTv.setVisibility(View.GONE);
            }
            copyTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData cData = ClipData.newPlainText("文字", mMsg.getContent());
                    cmb.setPrimaryClip(cData);
                    dialog.dismiss();
                    Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
                }
            });
            // 删除
            TextView deleteTv = (TextView) dialog.findViewById(R.id.delete_item_tv);
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (mContext instanceof ChatActivity) {
//                        String chatId = mChat.getChatEntity().getChatId();
//                        List<String> msgIds = new ArrayList<String>();
//                        msgIds.add(mMsg.getMsgId());
//                        DeleteMsgEvent deleteMsgEvent = new DeleteMsgEvent();
//                        deleteMsgEvent.setChatId(chatId);
//                        deleteMsgEvent.setMsgIds(msgIds);
                        // 删除消息 TODO 应该放在presenter
                        mChat.getMsgList().remove(mMsg);
                        mAdapter.notifyDataSetChanged();
                        ((ChatActivity) mContext).doDeleteMessage(mMsg.getMsgId());
                    }
                    dialog.dismiss();
                }
            });
            // 转发
            TextView transpondTv = (TextView) dialog.findViewById(R.id.transpond_item_tv);
            transpondTv.setVisibility(View.GONE);
            transpondTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            // 取消
            TextView cancelTv = (TextView) dialog.findViewById(R.id.cancel_item_tv);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
    }

    private class LoadingListener implements ImageLoadingListener {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            setImageSize(view, loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
        }
    }

}
