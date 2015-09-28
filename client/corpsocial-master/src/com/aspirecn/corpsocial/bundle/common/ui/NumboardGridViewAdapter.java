package com.aspirecn.corpsocial.bundle.common.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aspiren.corpsocial.R;

public class NumboardGridViewAdapter extends BaseAdapter {
    //private Context context;
    private LayoutInflater inflater;

    private Integer[] imageIds = {R.drawable.numkeyboard_1,
            R.drawable.numkeyboard_2, R.drawable.numkeyboard_3,
            R.drawable.numkeyboard_4, R.drawable.numkeyboard_5,
            R.drawable.numkeyboard_6, R.drawable.numkeyboard_7,
            R.drawable.numkeyboard_8, R.drawable.numkeyboard_9,
            R.drawable.numkeyboard_cancel, R.drawable.numkeyboard_0,
            R.drawable.numkeyboard_ok};

    public NumboardGridViewAdapter(Context c) {
        super();
        //context = c;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {

        return imageIds.length;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(
                    R.layout.common_login_keyboard_griditem, null);
            holder.img = (ImageView) convertView
                    .findViewById(R.id.login_numkeyboard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setImageResource(imageIds[position]);
        return convertView;
    }

    final class ViewHolder {
        public ImageView img;
    }


}
