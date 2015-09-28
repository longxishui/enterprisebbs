package com.aspirecn.corpsocial.bundle.workgrp.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspiren.corpsocial.R;

import java.util.ArrayList;

public class BBSPraiseListAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<KeyValue> datas = null;

    public BBSPraiseListAdapter(Context mContext, ArrayList<KeyValue> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeyValue item = datas.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.workgrp_praises_list_item, null);
            viewHolder.userHead = (ImageView) convertView.findViewById(R.id.workgrp_praises_item_userheadid);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.workgrp_praises_item_usernameid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BBSUtil.setUserHeadImg(item.getKey(), viewHolder.userHead);
        viewHolder.userName.setText(item.getValue());
        return convertView;
    }

    static class ViewHolder {
        ImageView userHead;
        TextView userName;
    }

}
