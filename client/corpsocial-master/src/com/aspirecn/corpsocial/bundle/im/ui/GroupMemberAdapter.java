package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberAdapter extends BaseAdapter {

    DisplayImageOptions mOptions;
    DisplayImageOptions mAddButtonOptions;
    private Context mContext;
    private int mGroupType;
    private List<String> mMemberIds;
    private List<String> mDeleteIds;
    private boolean mEditState = false;
    private OnDeletedItemsChangeListener changeListener;

    public GroupMemberAdapter(Context context, int groupType, List<String> memberIds) {
        mContext = context;
        mGroupType = groupType;
        mMemberIds = memberIds;
        //头像配置
        mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.im_chat_setting_head_selector)
                .showImageOnFail(R.drawable.im_chat_setting_head_selector)
                .build();
        //加号配置
        mAddButtonOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.workgrp_admin_setting_item_addbtn)
                .build();
    }

    public boolean isEditState() {
        return mEditState;
    }

    public void setEditState(boolean editState) {
        this.mEditState = editState;
    }

    /**
     * 获取要删除的Item集合
     *
     * @return
     */
    public List<String> getForDeleteMemberIds() {
        if (mDeleteIds == null) {
            mDeleteIds = new ArrayList<String>();
        }
        return mDeleteIds;
    }

    /**
     * 清空待删除成员Id
     */
    public void clearForDeleteMemberIds() {
        if (mDeleteIds != null) {
            mDeleteIds.clear();
        }
    }

    @Override
    public int getCount() {
        int defaultCount = 0;
        //自建群允许加人（显示加人按钮）
        if (mGroupType == GroupType.GROUP.getValue()) {
            defaultCount = 1;
        }
        if (mMemberIds != null) {
            return mMemberIds.size() + defaultCount;
        }
        return defaultCount;
    }

    @Override
    public String getItem(int position) {
        return mMemberIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = createView(convertView);
        bindingData(position, (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void bindingData(final int position, ViewHolder holder) {
        //成员信息
        if (position < mMemberIds.size()) {
            User user = UserUtil.getUser(getItem(position));
            if (user != null) {
                ImageLoader.getInstance().displayImage(user.getUrl(), holder.headImg, mOptions);
                holder.name.setText(user.getName());
                holder.friendIcon.setVisibility(!user.canCommunicate() ? View.VISIBLE : View.GONE);
            } else {
                ImageLoader.getInstance().displayImage(null, holder.headImg, mOptions);
                holder.name.setText(getItem(position));
                holder.friendIcon.setVisibility(View.GONE);
            }
            //编辑状态
            holder.deleteIcon.setVisibility(mEditState ? View.VISIBLE : View.GONE);
            holder.deleteIcon.setChecked(getForDeleteMemberIds().contains(getItem(position)));
            holder.deleteIcon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (checked) {
                        if (!getForDeleteMemberIds().contains(getItem(position))) {
                            getForDeleteMemberIds().add(getItem(position));
                        }
                    } else {
                        if (getForDeleteMemberIds().contains(getItem(position))) {
                            getForDeleteMemberIds().remove(getItem(position));
                        }
                    }
                    changeDeleteItems(getForDeleteMemberIds().size());
                }
            });
        }
        //添加成员按钮
        else {
            //显示方式与头像同样，否则会显示错乱（ImageLoader机制问题导致）
            ImageLoader.getInstance().displayImage(null, holder.headImg, mAddButtonOptions);
            holder.deleteIcon.setVisibility(View.GONE);
            holder.name.setText("添加成员");
            holder.friendIcon.setVisibility(View.GONE);
        }
    }

    private View createView(View view) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.im_group_admin_setting_griditem, null);
            ViewHolder holder = new ViewHolder();
            holder.headImg = (ImageView) view.findViewById(R.id.im_member_head_img_iv);
            holder.deleteIcon = (CheckBox) view.findViewById(R.id.im_member_delete_icon_cb);
            holder.name = (TextView) view.findViewById(R.id.im_member_name_tv);
            holder.friendIcon = (ImageView) view.findViewById(R.id.im_member_friend_icon_iv);
            view.setTag(holder);
        }
        return view;
    }

    public void setOnDeleteItemsChangeListener(OnDeletedItemsChangeListener listener) {
        this.changeListener = listener;
    }

    private void changeDeleteItems(int counts) {
        if (changeListener != null) {
            changeListener.onForDeleteItemsChange(counts);
        }
    }

    public interface OnDeletedItemsChangeListener {
        void onForDeleteItemsChange(int counts);
    }

    class ViewHolder {
        ImageView headImg;
        CheckBox deleteIcon;
        TextView name;
        ImageView friendIcon;
    }

}
