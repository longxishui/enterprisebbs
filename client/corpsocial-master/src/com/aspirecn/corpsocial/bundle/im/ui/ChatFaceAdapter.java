package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aspirecn.corpsocial.bundle.im.domain.ChatEmoji;
import com.aspirecn.corpsocial.common.util.DensityUtil;
import com.aspiren.corpsocial.R;

import java.util.List;

public class ChatFaceAdapter extends BaseAdapter {

    private List<ChatEmoji> data;

    private LayoutInflater inflater;

    private int size = 0;

//    private float scaleWidth=1;
//    
//    private float scaleHeight=1;

    private Context context;

    public ChatFaceAdapter(Context context, List<ChatEmoji> list) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.size = list.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        //System.out.println("size:"+this.size);
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatEmoji emoji = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.im_chat_item_face, null);
            viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.item_iv_face);
            /*LayoutParams lp = viewHolder.iv_face.getLayoutParams();
            lp.width = 75;
            lp.height = 70;
            viewHolder.iv_face.setLayoutParams(lp);*/

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*if(emoji.getId() == R.drawable.face_del_icon) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageResource(emoji.getId());
        } else if(TextUtils.isEmpty(emoji.getCharacter())) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageDrawable(null);
        } else {
            viewHolder.iv_face.setTag(emoji);
            viewHolder.iv_face.setImageResource(emoji.getId());
        }*/
        //if(emoji.getFaceName() == "face_del.png") 
        if (position == 20) {
            LayoutParams lp = viewHolder.iv_face.getLayoutParams();
            int dp = DensityUtil.dip2px(context, 35);
            //System.out.println(dp);
            lp.width = dp;
            lp.height = dp;
            viewHolder.iv_face.setLayoutParams(lp);
            //convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.del_face_btn));
            //convertView.setBackgroundDrawable(null);
            //viewHolder.iv_face.setImageBitmap(BitmapFactory.decodeFile(emoji.getPath()));
            viewHolder.iv_face.setImageResource(R.drawable.im_chat_face_del_btn);
            //viewHolder.iv_face.setImageResource(R.drawable.face_del_ico_dafeult);
            emoji.setId(R.drawable.im_chat_face_del_btn);
            //emoji.setId(R.drawable.face_del_ico_dafeult);
        } else if (TextUtils.isEmpty(emoji.getCharacter())) {
            convertView.setBackgroundDrawable(null);

            LayoutParams lp = viewHolder.iv_face.getLayoutParams();
            int dp = DensityUtil.dip2px(context, 35);
            //System.out.println(dp);
            lp.width = dp;
            lp.height = dp;
            viewHolder.iv_face.setLayoutParams(lp);
            viewHolder.iv_face.setImageDrawable(null);
        } else {
            //System.out.println(emoji.getFaceName());
            LayoutParams lp = viewHolder.iv_face.getLayoutParams();
            int dp = DensityUtil.dip2px(context, 35);
            lp.width = dp;
            lp.height = dp;
            viewHolder.iv_face.setLayoutParams(lp);


            viewHolder.iv_face.setTag(emoji);
            Bitmap face = BitmapFactory.decodeFile(emoji.getPath());
            /*int bmpbmpWidth=face.getWidth();
            int bmpbmpHeight=face.getHeight();
        	System.out.println("bmpbmpWidth: "+bmpbmpWidth);
        	System.out.println("bmpbmpHeight: "+bmpbmpHeight);*/
            viewHolder.iv_face.setImageBitmap(face);
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView iv_face;
    }
}
