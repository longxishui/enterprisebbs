package com.aspirecn.corpsocial.bundle.addrbook.ui;

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

import java.util.List;

public class AddrbookSearchAdapter extends BaseAdapter {

    private List<User> mBeans;
    private Context mContext;


    public AddrbookSearchAdapter(Context context, List<User> searchItems) {
        this.mBeans = searchItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup vg) {
        // 初始化控件
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.im_search_chat_msg_list_item, vg, false);
            //头像
            holder.headImgView = (TextView) convertView.findViewById(R.id.im_search_chat_msg_head_portrait_tv);
            holder.headImgIv = (ImageView) convertView.findViewById(R.id.im_search_chat_msg_head_portrait_iv);
            // 名称
            holder.userName = (TextView) convertView.findViewById(R.id.im_search_chat_msg_usename_tv);
            // 部门
            holder.myDept = (TextView) convertView.findViewById(R.id.im_search_chat_msg_msg_tv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

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
        User item = mBeans.get(position);
        holder.userName.setText(item.getName());
        //holder.myDept.setText(item.getDeptName());
        holder.myDept.setText(item.getSignature());

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
