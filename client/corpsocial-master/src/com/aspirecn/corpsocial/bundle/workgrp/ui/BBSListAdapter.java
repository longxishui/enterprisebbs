/**
 *
 */
package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.ui.fragment.ShareDialog;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSDialog;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSUtil;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.SysInfo;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspirecn.corpsocial.common.util.ImageDownloadUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;
//import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;

/**
 * @author duyinzhou
 */
public class BBSListAdapter extends BaseAdapter {
    private Context context;
    private List<BBSItem> listBBS;
    //private ContactDao contactDao;
    private UserDao userDao;
    private OnClickListener praiListener;
    private DisplayImageOptions options;

    public BBSListAdapter(Context context, List<BBSItem> listBBS, OnClickListener praiseClickListener) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listBBS = listBBS;
        userDao = new UserDao();
        this.praiListener = praiseClickListener;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listBBS.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listBBS.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder mHolder = null;
        final BBSItem bbsItem = listBBS.get(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.workgrp_content_list_item, null);
            mHolder = new ViewHolder();
//            mHolder.itemTitle = (TextView) convertView.findViewById(R.id.workgrp_item_titleid);
            mHolder.itemTime = (TextView) convertView.findViewById(R.id.workgrp_item_timeid);
            mHolder.itemCreator = (TextView) convertView.findViewById(R.id.workgrp_item_creatorid);
            mHolder.itemPraiseBtnViewImage = (ImageView) convertView.findViewById(R.id.workgrp_item_praise_btnimageid);
            mHolder.itemReplyImage = (ImageView) convertView.findViewById(R.id.workgrp_item_reply_btnimageid);
            mHolder.itemPraiseBtnViewText = (TextView) convertView.findViewById(R.id.workgrp_item_praise_btntextid);
            mHolder.itemReplyBtnText = (TextView) convertView.findViewById(R.id.workgrp_item_reply_btntextid);
            mHolder.itemContent = (TextView) convertView.findViewById(R.id.workgrp_item_contentid);
            mHolder.itemPicture = (ImageView) convertView.findViewById(R.id.workgrp_item_content_pictureid);
            mHolder.itemUserImage = (ImageView) convertView.findViewById(R.id.workgrp_user_imageid);
            mHolder.itemshareBtn = (ImageView) convertView.findViewById(R.id.workgrp_item_share_btnimageid);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setId(position);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(context,
                        com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpBBSDetailActivity_.class);

                mIntent.putExtra("bbsid", bbsItem.getBbsItemEntity().getId());
                context.startActivity(mIntent);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String srcContent = bbsItem.getBbsItemEntity().getContent();
                BBSDialog bbsDialog = new BBSDialog(context, R.style.MyDialog, srcContent);
                bbsDialog.setCancelable(true);
                bbsDialog.show();
                bbsDialog.setTitle("复制帖子");
                return true;
            }
        });
        mHolder.itemshareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareDialog(context,"wxd9e637fe30c9280e",bbsItem.getBbsItemEntity().getContent(),bbsItem.getBbsItemEntity().getTitle(),"",bbsItem.getBbsItemEntity().getId()).show();
            }
        });
//        mHolder.itemTitle.setText(bbsItem.getBbsItemEntity().getTitle());
        mHolder.itemContent.setText(FaceConversionUtil.getInstace().getExpressionString(context, bbsItem.getBbsItemEntity().getContent()));
        mHolder.itemTime.setText(DateUtils.getWeiXinTime(bbsItem.getBbsItemEntity().getCreateTime()));
//		mHolder.itemTime.setText(BBSUtil.getDistanceTime(bbsItem.getCreateTime()));
        mHolder.itemCreator.setText(bbsItem.getBbsItemEntity().getCreatorName());

        FileInfoEntity fileInfoEntity = bbsItem.getFileInfoEntity();
        if (fileInfoEntity != null) {
//            mHolder.itemPicture.setImageResource(R.drawable.component_noimg_small);
            String imageUrl = fileInfoEntity.getUrl();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                mHolder.itemPicture.setVisibility(View.VISIBLE);
                if (imageUrl.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    mHolder.itemPicture.setImageDrawable(BBSUtil.getLocalDrawablePicture(imageUrl));
//					try {
//						mHolder.itemPicture.setImageDrawable(SyncImageLoader.loadImageFromUrl(imageUrl));
//					} catch (IOException e) {
//						e.printStackTrace();
//						mHolder.itemPicture.setImageResource(R.drawable.im_chat_from_load_fail);
//					}
                } else {
                    ImageDownloadUtil.INSTANCE.showImage(imageUrl, "bbs", mHolder.itemPicture);
//					ImageLoader.getInstance().displayImage(imageUrl, mHolder.itemPicture,options);
//					syncImageLoader.loadImage(position, imageUrl, imageLoadListener);
                }
            } else {
                mHolder.itemPicture.setVisibility(View.GONE);
            }
        } else {
            mHolder.itemPicture.setVisibility(View.GONE);
        }
//		}
        String size = bbsItem.getBbsItemEntity().getReplyTimes();
        if (Integer.valueOf(size) > 0) {
            mHolder.itemReplyBtnText.setText("评论 "+size);
        } else {
            mHolder.itemReplyBtnText.setText("评论 0");
        }

        ArrayList<String> praiseUserIds = bbsItem.getPraiseUseridList();
        if (Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes()) > 0) {
            mHolder.itemPraiseBtnViewText.setText("赞 "+bbsItem.getBbsItemEntity().getPraiseTimes());
            if (praiseUserIds!=null&&praiseUserIds.contains(Config.getInstance().getUserId())) {
                mHolder.itemPraiseBtnViewImage.setBackgroundResource(R.drawable.workgrp_praised_icon2);
            } else {
                mHolder.itemPraiseBtnViewImage.setBackgroundResource(R.drawable.workgrp_nopraise_icon2);
            }
        } else {
            mHolder.itemPraiseBtnViewText.setText("赞 0");
            mHolder.itemPraiseBtnViewImage.setBackgroundResource(R.drawable.workgrp_nopraise_icon2);
        }
        mHolder.itemPraiseBtnViewImage.setTag(bbsItem);
        mHolder.itemPraiseBtnViewImage.setOnClickListener(praiListener);

        mHolder.itemUserImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                User user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(bbsItem.getBbsItemEntity().getCreatorId());
                if (user != null) {
                    if (user.getCorpList().size() > 1) {
                        intent.setClass(context, AddrbookGroupChoose_.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("user", user);
                        intent.putExtras(mBundle);
                    } else {
                        intent.putExtra("ContactId", user.getUserid());
                        intent.putExtra("corpId", user.getCorpId());
                        intent.setClass(context, AddrbookPersonalParticularsActivity_.class);
                    }
                    context.startActivity(intent);
                }
            }
        });

        // 设置用户头像
        BBSUtil.setUserHeadImg(bbsItem.getBbsItemEntity().getCreatorId(), mHolder.itemUserImage);

        mHolder.itemReplyImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context,
                        com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpBBSDetailActivity_.class);
                mIntent.putExtra("bbsid", bbsItem.getBbsItemEntity().getId());
                mIntent.putExtra("open", true);
                context.startActivity(mIntent);
            }
        });
        return convertView;

    }

    private List<User> getcontactVO(ArrayList<String> praiseUserIds) {
        // TODO Auto-generated method stub
        List<User> contactVO = new ArrayList<User>();
        int size = praiseUserIds.size();
        if (size >= 2) {
            List<String> praiseQueryId = new ArrayList<String>();
            praiseQueryId.add(praiseUserIds.get(size - 1));
            praiseQueryId.add(praiseUserIds.get(size - 2));
            try {
                contactVO.addAll(userDao.findFilterByContactIds(Config.getInstance().getUserId(), praiseQueryId));
            } catch (Exception e) {
                LogUtil.e("", e);
            }
        } else {
            try {
                contactVO.addAll(userDao.findFilterByContactIds(Config.getInstance().getUserId(), praiseUserIds));
            } catch (Exception e) {
                LogUtil.e("", e);
            }
        }
        return contactVO;
    }

    private class ViewHolder {
//        TextView itemTitle;
        TextView itemContent;
        TextView itemTime;
        TextView itemCreator;
        TextView itemReplyBtnText;
        ImageView itemPraiseBtnViewImage;
        ImageView itemReplyImage;
        TextView itemPraiseBtnViewText;
        ImageView itemshareBtn;
        ImageView itemPicture;
        ImageView itemUserImage;
    }

}
