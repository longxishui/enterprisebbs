package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

/**
 * Created by Administrator on 2015/6/24.
 */

public class AddrbookOftenAdapter extends BaseAdapter implements SectionIndexer {
    private List<User> list = null;
    private Context mContext;

    public AddrbookOftenAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /*显示头像*/
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
            .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
            .showImageOnFail(R.drawable.im_main_tab_head_portrait_3)
            .build();

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final User mContent = list.get(position);
        if (mContent.getInitialKey().isEmpty()) {
            mContent.setInitialKey("#");
        }

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.addrbook_often_item, null);
            viewHolder.ivHead = (ImageView) view.findViewById(R.id.addrbook_often_item_head_iv);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.addrbook_often_item_title_tv);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.addrbook_often_item_catalog_tv);
            viewHolder.tvCopyPosition = (TextView) view.findViewById(R.id.addrbook_often_item_copyposition_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(list.get(position).getName());
        //viewHolder.tvCopyPosition.setText(list.get(position).getCorpName());
        viewHolder.tvCopyPosition.setText(list.get(position).getSignature());

        ImageLoader.getInstance().displayImage(list.get(position).getSmallUrl(), viewHolder.ivHead, options);

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getInitialKey().toUpperCase().substring(0, 1));
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        return view;

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getInitialKey().toUpperCase().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getInitialKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    final static class ViewHolder {
        ImageView ivHead;
        TextView tvLetter;
        TextView tvTitle;
        TextView tvCopyPosition;
    }
}
