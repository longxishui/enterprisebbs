/**
 *
 */
package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author duyinzhou
 */
public class WorkgrpNewGridAdapter extends BaseAdapter {
    private Context context;
    //private ContactDao contactDao;
    private List<String> listFileNames;
    private DisplayImageOptions options;

    public WorkgrpNewGridAdapter(Context context, List<String> listFileNames) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listFileNames = listFileNames;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listFileNames.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listFileNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
           convertView =  LayoutInflater.from(context).inflate(
                    R.layout.workgrp_picture_grid_item, null);
            viewHolder.mIV_content = (ImageView) convertView.findViewById(R.id.grid_item_image);
            viewHolder.mIV_Select = (ImageView) convertView.findViewById(R.id.grid_item_select);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(listFileNames.get(position), viewHolder.mIV_content);
        viewHolder.mIV_Select.setVisibility(View.GONE);
        return convertView;
    }
    class ViewHolder{
        ImageView mIV_Select;
        ImageView mIV_content;
    }
}
