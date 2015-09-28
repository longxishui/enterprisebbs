package com.aspirecn.corpsocial.bundle.addrbook.ui.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class AddrbookSelectSearchAdapter extends BaseAdapter {
    private List<User> searchItems;
    private LayoutInflater mInflater;

    public AddrbookSelectSearchAdapter(Context context) {
        setData(null);
        this.mInflater = LayoutInflater.from(context);
    }

    public AddrbookSelectSearchAdapter(Context context, List<User> searchItems) {
        setData(searchItems);
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 配置数据
     */
    public void setData(List<User> searchItems) {
        if (searchItems == null) {
            this.searchItems = new ArrayList<User>();
        } else {
            this.searchItems = searchItems;
        }
    }

    @Override
    public int getCount() {
        return searchItems.size();
    }

    @Override
    public User getItem(int position) {
        return searchItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // 初始化控件
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.im_search_chat_msg_list_item, null);
            //头像
            holder.headImgView = (TextView) convertView
                    .findViewById(R.id.im_search_chat_msg_head_portrait_tv);
            holder.headImgIv = (ImageView) convertView
                    .findViewById(R.id.im_search_chat_msg_head_portrait_iv);
            // 名称
            holder.userName = (TextView) convertView
                    .findViewById(R.id.im_search_chat_msg_usename_tv);
            // 部门
            holder.myDept = (TextView) convertView
                    .findViewById(R.id.im_search_chat_msg_msg_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 绑定数据
        bindingData(holder, position);

        return convertView;
    }

    /**
     * 绑定数据
     *
     * @param holder
     */
    private void bindingData(ViewHolder holder, int position) {
        final User item = getItem(position);
        holder.userName.setText(item.getName());
        holder.myDept.setText(item.getDeptName());

        holder.headImgView.setVisibility(View.GONE);
        holder.headImgIv.setVisibility(View.VISIBLE);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                .build();
        ImageLoader.getInstance().displayImage(item.getUrl(), holder.headImgIv, options);
    }

    class ViewHolder {
        public TextView headImgView;
        public ImageView headImgIv;
        public TextView userName;
        public TextView myDept;
    }

}
