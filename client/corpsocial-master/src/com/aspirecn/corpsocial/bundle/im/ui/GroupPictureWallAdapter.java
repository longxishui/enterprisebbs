package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aspirecn.corpsocial.bundle.im.domain.GroupPictureWall;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.ui.component.imageloader.ImagePagerActivity;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class GroupPictureWallAdapter extends BaseAdapter {

    private Context context;

    private GroupPictureWall groupPictureWall;

    private LayoutInflater mInflater;

    public GroupPictureWallAdapter(Context context, GroupPictureWall groupPictureWall) {
        super();
        this.context = context;
        this.groupPictureWall = groupPictureWall;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return groupPictureWall.getPictureMsgList().size();
    }

    @Override
    public Object getItem(int position) {
        return groupPictureWall.getPictureMsgList().get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.im_group_picture_item, null);
            holder.msgPictureIv = (ImageView) view.findViewById(R.id.im_group_picture_iv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final MsgEntity item = groupPictureWall.getPictureMsgList().get(position);

        ImageLoader.getInstance().displayImage(item.getOriginalUrl(), holder.msgPictureIv);

        holder.msgPictureIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 生成图片url地址列表
                ArrayList<String> urls = new ArrayList<String>();
                List<MsgEntity> pictureList = groupPictureWall
                        .getPictureMsgList();
                for (MsgEntity picture : pictureList) {
                    urls.add(picture.getOriginalUrl());
                }
                Intent intent = new Intent(context, ImagePagerActivity.class);
//				ImageDownloadUtil.INSTANCE.preparePath("picturewall");
//				// 创建图片信息。
//				// 其中imageSubPath指图片的子文件夹，对应Constant.PICTURE_PATH文件夹的哪一个子文件夹。例如新闻的子文件夹为news2
//				ImageGalleryInfo info = new ImageGalleryInfo("picturewall",
//						urls, position);

                intent.putExtra("position", position);
                intent.putStringArrayListExtra("urls", urls);
                context.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {
        public ImageView msgPictureIv;

    }


}
