package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendEvent;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * 新朋友适配器
 * Created by lihaiqiang on 2015/6/19.
 */
public class NewFriendsAdapter extends BaseAdapter {

    private Context mContext;

    private List<FriendInvite> mFriendInvites;

    private DisplayImageOptions mOptions;

    public NewFriendsAdapter(Context context, List<FriendInvite> friendInvites) {
        mContext = context;
        mFriendInvites = friendInvites;

        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.addrbook_contact)
                .showImageOnFail(R.drawable.addrbook_contact)
                .build();
    }

    @Override
    public int getCount() {
        if (mFriendInvites != null) {
            return mFriendInvites.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public FriendInvite getItem(int i) {
        return mFriendInvites.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.addrbook_new_friends_list_item, null);
            holder.headIV = (ImageView) view.findViewById(R.id.addrbook_new_friends_item_head_iv);
            holder.nameTV = (TextView) view.findViewById(R.id.addrbook_new_friends_item_name_tv);
            holder.corpTV = (TextView) view.findViewById(R.id.addrbook_new_friends_item_corp_tv);
            holder.statusTV = (TextView) view.findViewById(R.id.addrbook_new_friends_item_status_tv);
            holder.addBT = (Button) view.findViewById(R.id.addrbook_new_friends_item_add_bt);

            view.setTag(holder);
        }

        bindingData(i, view);

        return view;
    }

    private void bindingData(int i, View view) {
        final FriendInvite friendInvite = getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        //是否新消息
        if (friendInvite.getIsNew() == 1) {
            view.setBackgroundResource(R.drawable.listview_item_bg_selector2);
        } else {
            view.setBackgroundResource(R.drawable.listview_item_bg_selector);
        }
        //头像
        ImageLoader.getInstance().displayImage(friendInvite.getSmallUrl(), holder.headIV, mOptions);
        //名称
        holder.nameTV.setText(friendInvite.getUsername());
        //所属企业名称
        if (!TextUtils.isEmpty(friendInvite.getSignature()))
            holder.corpTV.setText(friendInvite.getSignature());
        else
            holder.corpTV.setText("让我们开始聊吧");
        //状态
        //未接受添加好友请求
        if (friendInvite.getStatus() == 0) {
            holder.statusTV.setVisibility(View.GONE);
            holder.addBT.setVisibility(View.VISIBLE);
        }
        //已经接受添加好友请求
        else {
            holder.statusTV.setVisibility(View.VISIBLE);
            holder.addBT.setVisibility(View.GONE);
        }
        //确认添加好友
        holder.addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptAddFriend(friendInvite);
            }
        });
    }

    /**
     * 接受添加好受请求
     *
     * @param friendInvite
     */
    private void acceptAddFriend(FriendInvite friendInvite) {
        CustomDialog.showProgeress(mContext);
        AcceptAddFriendEvent event = new AcceptAddFriendEvent();
        event.setInvite(friendInvite);
        UiEventHandleFacade.getInstance().handle(event);
    }

    private class ViewHolder {
        ImageView headIV;
        TextView nameTV;
        TextView corpTV;
        TextView statusTV;
        Button addBT;
    }

}
