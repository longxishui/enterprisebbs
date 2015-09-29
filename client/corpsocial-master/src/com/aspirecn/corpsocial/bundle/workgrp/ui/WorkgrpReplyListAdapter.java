package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.repository.UserDao;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.FileInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSDialog;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSUtil;
import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.component.imageloader.ImagePagerActivity;
import com.aspirecn.corpsocial.common.util.DateUtils;
import com.aspirecn.corpsocial.common.util.FaceConversionUtil;
import com.aspirecn.corpsocial.common.util.ImageDownloadUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class WorkgrpReplyListAdapter extends BaseAdapter {

    private Context context;
    private List<BBSReplyInfoEntity> replyInfoEntitys;
    private View titleView;
    private BBSItem bbsItem;
    //private ContactDao contactDao;
    private UserDao userDao;
    private OnClickListener praiseOnClickListener;
    private OnClickListener pictureClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
//			Intent intent = new Intent(
//					context,
//					com.aspirecn.corpsocial.bundle.workgrp.ui.BBsShowPictureActivity_.class);
//			intent.putExtra("fileinfo", (FileInfo) arg0.getTag());
//			context.startActivity(intent);
            Intent intent = new Intent(context, ImagePagerActivity.class);
            ArrayList<String> urls = new ArrayList<String>();
            urls.add(((FileInfoEntity) arg0.getTag()).getOrginalUrl());
            intent.putStringArrayListExtra("urls", urls);
            context.startActivity(intent);
        }
    };
    private OnClickListener headClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();

//            UserServiceParam serviceParam = new UserServiceParam();
//            serviceParam.setServie("FindContactDetailService");
//            Map<String, String> param = new HashMap<String, String>();
//            param.put("userid", (String) arg0.getTag());
//            serviceParam.setParams(param);
//
//            UserServiceResult<User> result = (UserServiceResult<User>) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class).invoke(serviceParam);
//            User user = result.getData();
            User user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(arg0.getTag());
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
    };
    private OnClickListener titleOperationClickListener = new OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            // TODO Auto-generated method stub
            int[] location = new int[2];
            arg0.getLocationInWindow(location);
            final PopupWindow popuWin;
            View view = View.inflate(context,
                    R.layout.workgrp_bbs_edit_dialog, null);
            popuWin = new PopupWindow(context);
            popuWin.setContentView(view);
            final BBSItem bbsitem = (BBSItem) arg0.getTag();
//			view.findViewById(R.id.workgrp_bbs_dialog_edit)
//					.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							popuWin.dismiss();
//							Intent mIntent = new Intent(
//									context,
//									com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpNewBBSActivity_.class);
//							mIntent.putExtra("itemid", bbsitem.getBbsItemEntity().getId());
//							context.startActivity(mIntent);
//
//						}
//	});
            view.findViewById(R.id.workgrp_bbs_dialog_delete)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            popuWin.dismiss();
                            UiEventHandleFacade.getInstance().handle(new
                                    BBSDeleteEvent(DeleteType.ITEM,
                                    bbsitem.getBbsItemEntity().getId(), "", bbsitem.getBbsItemEntity().getGroupId()));
                        }
                    });
            popuWin.setBackgroundDrawable(new ColorDrawable(0));
            view.measure(0, 0);
            popuWin.setWidth(view.getMeasuredWidth());
            popuWin.setHeight(view.getMeasuredHeight());
            popuWin.setOutsideTouchable(true);
            // menuWindow.showAsDropDown(layout); //设置弹出效果
            // menuWindow.showAsDropDown(null, 0, layout.getHeight());
            popuWin.showAtLocation(arg0, Gravity.TOP | Gravity.LEFT,
                    location[0] - view.getMeasuredWidth() - 10, location[1]);
        }
    };

    private OnClickListener editClickListener = new OnClickListener() {

        @Override
        public void onClick(final View arg0) {
            // TODO Auto-generated method stub
            int[] location = new int[2];
            arg0.getLocationInWindow(location);
            final PopupWindow popuWin;
            View view = View.inflate(context,
                    R.layout.workgrp_replyinfo_dialog, null);
            popuWin = new PopupWindow(context);
//			popuWin = new PopupWindow(view,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            popuWin.setContentView(view);
            view.findViewById(R.id.workgrp_replyinfo_dialog_delete)
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            popuWin.dismiss();
                            BBSReplyInfoEntity bbsreplyInfo = (BBSReplyInfoEntity) arg0
                                    .getTag();
                            UiEventHandleFacade.getInstance().handle(
                                    new BBSDeleteEvent(DeleteType.REPLY,
                                            bbsreplyInfo.getItemId(),
                                            bbsreplyInfo.getId(), bbsItem.getBbsItemEntity()
                                            .getGroupId()));
                        }
                    });

            popuWin.setBackgroundDrawable(new ColorDrawable(0));
            view.measure(0, 0);
            popuWin.setWidth(view.getMeasuredWidth());
            popuWin.setHeight(view.getMeasuredHeight());
            popuWin.setOutsideTouchable(true);
            popuWin.showAtLocation(arg0, Gravity.TOP | Gravity.LEFT,
                    location[0] - view.getMeasuredWidth() - 10, location[1]);
        }
    };

    public WorkgrpReplyListAdapter(Context context,
                                   List<BBSReplyInfoEntity> replyInfoEntitys, BBSItem bbsItem,OnClickListener praiseOnClickListener) {
        super();
        this.context = context;
        this.replyInfoEntitys = replyInfoEntitys;
        this.bbsItem = bbsItem;
        this.praiseOnClickListener = praiseOnClickListener;
        //contactDao = new ContactDao();
        userDao = new UserDao();
    }

    @Override
    public int getCount() {
        return replyInfoEntitys.size();
    }

    @Override
    public Object getItem(int position) {
        return replyInfoEntitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        final BBSReplyInfoEntity bbsReplyInfoEntity = replyInfoEntitys.get(position);
        if (null == convertView || convertView.equals(titleView)) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.workgrp_reply_list_item, null);
            viewHolder.replyUser = (TextView) convertView
                    .findViewById(R.id.workgrp_reply_usernameid);
            viewHolder.replyContent = (TextView) convertView
                    .findViewById(R.id.workgrp_reply_contentid);
            viewHolder.replyTime = (TextView) convertView
                    .findViewById(R.id.workgrp_reply_timeid);
            viewHolder.replyStorey = (TextView) convertView
                    .findViewById(R.id.workgrp_reply_storeyid);
            viewHolder.replyPicture = (ImageView) convertView
                    .findViewById(R.id.workgrp_reply_pictureid);
            viewHolder.replyUserHead = (ImageView) convertView
                    .findViewById(R.id.workgrp_reply_headid);
            viewHolder.editbtnView = (ImageView) convertView
                    .findViewById(R.id.workgrp_reply_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // convertView.setId(position);
        viewHolder.replyUser.setText(bbsReplyInfoEntity.getReplyerName());
        viewHolder.replyContent.setText(FaceConversionUtil.getInstace()
                .getExpressionString(context, bbsReplyInfoEntity.getContent()));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                boolean unShow = false;
                if (bbsReplyInfoEntity.getReplyerId().equals(
                        Config.getInstance()
                                .getUserId()) || bbsItem.getBbsItemEntity().getCreatorId().equals(Config.getInstance()
                        .getUserId())) {
                    unShow = true;
                }

                String srcContent = bbsReplyInfoEntity.getContent();
                BBSDialog bbsDialog = new BBSDialog(context, R.style.MyDialog, bbsReplyInfoEntity, bbsItem, unShow, srcContent);
                bbsDialog.setCancelable(true);
                bbsDialog.show();
                return true;
            }
        });

        viewHolder.replyTime.setText(DateUtils.getWeiXinTime(bbsReplyInfoEntity
                .getTime()));
        viewHolder.replyStorey.setText("第" + (position + 1) + "楼");
        viewHolder.replyUserHead.setTag(bbsReplyInfoEntity.getReplyerId());
        viewHolder.replyUserHead.setOnClickListener(headClickListener);
//        if (replyInfo.getReplyerId().equals(
//                com.aspirecn.corpsocial.common.config.Config.getInstance()
//                        .getUserId())) {
//            viewHolder.editbtnView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.editbtnView.setVisibility(View.GONE);
//        }
//        viewHolder.editbtnView.setTag(replyInfo);
//        viewHolder.editbtnView.setOnClickListener(editClickListener);
        // 设置用户头像
        BBSUtil.setUserHeadImg(bbsReplyInfoEntity.getReplyerId(),
                viewHolder.replyUserHead);
        final FileInfoEntity fileInfoEntity = bbsReplyInfoEntity.getConvertTOFileInfo();
        if (null != fileInfoEntity) {
            String imageUrl = fileInfoEntity.getUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                viewHolder.replyPicture.setVisibility(View.VISIBLE);
                if (imageUrl.startsWith(Environment
                        .getExternalStorageDirectory().getAbsolutePath())) {
                    viewHolder.replyPicture.setImageDrawable(BBSUtil
                            .getLocalDrawablePicture(imageUrl));
                    // viewHolder.replyPicture.setBackgroundDrawable(Drawable.createFromPath(imageUrl));
                } else {
//					ImageDownloadUtil.INSTANCE.showImage(imageUrl, "bbs",
//							viewHolder.replyPicture);
                    ImageLoader.getInstance().displayImage(imageUrl, viewHolder.replyPicture);
                }
            } else {
                viewHolder.replyPicture.setVisibility(View.GONE);
            }
            viewHolder.replyPicture.setTag(fileInfoEntity);
            viewHolder.replyPicture.setOnClickListener(pictureClickListener);
        } else {
            viewHolder.replyPicture.setVisibility(View.GONE);
        }
        return convertView;
    }

    public View initTitleItem(final Context context, final BBSItem bbsItem) {
        TitleViewHolder titleViewHolder = null;
        if (titleView == null) {
            titleViewHolder = new TitleViewHolder();
            titleView = View.inflate(context,
                    R.layout.workgrp_reply_title_item, null);
            titleViewHolder.userHead = (ImageView) titleView
                    .findViewById(R.id.workgrp_detail_user_headid);
            titleViewHolder.userNameTv = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_user_nameid);
            titleViewHolder.creatorTimeTv = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_publish_timeid);
            titleViewHolder.contentTv = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_contentid);
            titleViewHolder.titleTv = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_titleid);
            titleViewHolder.contentPicture = (ImageView) titleView
                    .findViewById(R.id.workgrp_detail_content_pictureid);
            titleViewHolder.praisesInfo = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_praise_tv);
            titleViewHolder.operationbtnView = (ImageView) titleView
                    .findViewById(R.id.workgrp_reply_title_edit);
            titleViewHolder.replyInfo = (TextView) titleView
                    .findViewById(R.id.workgrp_detail_reply_tv);
            titleViewHolder.praiseIV = (ImageView) titleView
                    .findViewById(R.id.workgrp_detail_praise_iv);
            titleView.setTag(titleViewHolder);
        } else {
            titleViewHolder = (TitleViewHolder) titleView.getTag();
        }
        titleViewHolder.userNameTv.setText(bbsItem.getBbsItemEntity().getCreatorName());
        titleViewHolder.creatorTimeTv.setText(DateUtils.getWeiXinTime(bbsItem.getBbsItemEntity()
                .getCreateTime()));
        titleViewHolder.contentTv.setText(FaceConversionUtil.getInstace()
                .getExpressionString(context, bbsItem.getBbsItemEntity().getContent()));
        if (titleViewHolder.contentTv.getText().toString().equals("")) {
            titleViewHolder.contentTv.setVisibility(View.GONE);
        }
        titleViewHolder.titleTv.setText(bbsItem.getBbsItemEntity().getTitle());
        if (titleViewHolder.titleTv.getText().toString().equals("")) {
            titleViewHolder.titleTv.setVisibility(View.GONE);
        }
        if (bbsItem.getBbsItemEntity().getCreatorId().equals(
                Config.getInstance()
                        .getUserId())) {
            titleViewHolder.operationbtnView.setVisibility(View.VISIBLE);
            titleViewHolder.operationbtnView.setTag(bbsItem);
            titleViewHolder.operationbtnView
                    .setOnClickListener(titleOperationClickListener);
        } else {
            titleViewHolder.operationbtnView.setVisibility(View.GONE);
        }
        // TODO 列表界面显示固定宽高的缩略图片，详情界面显示原图，现在只能显示缩略图
        if (bbsItem.getFileInfoEntity() != null) {
            String imageUrl = bbsItem.getFileInfoEntity().getUrl();
            if (TextUtils.isEmpty(imageUrl)) {
                titleViewHolder.contentPicture.setVisibility(View.GONE);
                LogUtil.i("帖子的图片内容为空");
            } else {
                titleViewHolder.contentPicture.setVisibility(View.VISIBLE);
                if (imageUrl.startsWith(Environment
                        .getExternalStorageDirectory().getAbsolutePath())) {
                    titleViewHolder.contentPicture.setImageDrawable(BBSUtil
                            .getLocalDrawablePicture(imageUrl));
                } else {
					ImageDownloadUtil.INSTANCE.showImage(bbsItem.getFileInfoEntity().getUrl(), "bbs", titleViewHolder.contentPicture);
//                    ImageLoader.getInstance().displayImage(bbsItem.getFileInfoEntity().getUrl(), titleViewHolder.contentPicture);
                }
                titleViewHolder.contentPicture.setTag(bbsItem.getFileInfoEntity());
                titleViewHolder.contentPicture
                        .setOnClickListener(pictureClickListener);
            }
        } else {
            titleViewHolder.contentPicture.setVisibility(View.GONE);
            LogUtil.i("帖子的图片内容为空");
        }
        // 设置用户头像
        BBSUtil.setUserHeadImg(bbsItem.getBbsItemEntity().getCreatorId(), titleViewHolder.userHead);
        titleViewHolder.userHead.setTag(bbsItem.getBbsItemEntity().getCreatorId());
        titleViewHolder.userHead.setOnClickListener(headClickListener);
        if (Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes()) > 0) {
            ArrayList<String> praiseUserIds = bbsItem.getPraiseUseridList();
            if (praiseUserIds!=null&&praiseUserIds.contains(Config.getInstance().getUserId())) {
                titleViewHolder.praiseIV.setBackgroundResource(R.drawable.workgrp_praised_icon2);
            } else {
                titleViewHolder.praiseIV.setBackgroundResource(R.drawable.workgrp_nopraise_icon2);
            }
        } else {
            titleViewHolder.praiseIV.setBackgroundResource(R.drawable.workgrp_nopraise_icon2);
        }
        titleViewHolder.praisesInfo.setText("赞 "+bbsItem.getBbsItemEntity().getPraiseTimes());
        titleViewHolder.praiseIV.setOnClickListener(praiseOnClickListener);
        titleViewHolder.praisesInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                BBSUtil.goBBSPraiseListActivity(context, bbsItem);
            }
        });
        return titleView;
    }

    /**
     * 查询id集合对应的联系人名字
     */
    public List<User> getcontactVO(ArrayList<String> praiseUserIds) {
        // TODO Auto-generated method stub
        List<User> contactVO = new ArrayList<User>();
        int size = praiseUserIds.size();
        if (size >= 2) {
            List<String> praiseQueryId = new ArrayList<String>();
            praiseQueryId.add(praiseUserIds.get(size - 1));
            praiseQueryId.add(praiseUserIds.get(size - 2));
            //contactVO = contactDao.findByContactIds(Config.getInstance().getUserId(), praiseQueryId);
            try {
                contactVO.addAll(userDao.findFilterByContactIds(Config.getInstance().getUserId(), praiseQueryId));
            } catch (Exception e) {
                LogUtil.e("", e);
            }
        } else {
            //contactVO = contactDao.findByContactIds(Config.getInstance().getUserId(), praiseUserIds);
            try {
                contactVO.addAll(userDao.findFilterByContactIds(Config.getInstance().getUserId(), praiseUserIds));
            } catch (Exception e) {
                LogUtil.e("WorkgrpReplyListAdapter", e);
            }
        }
        return contactVO;
    }

    static class ViewHolder {
        TextView replyUser;
        TextView replyContent;
        TextView replyTime;
        TextView replyStorey;
        ImageView replyPicture;
        ImageView replyUserHead;
        ImageView editbtnView;
    }

    public class TitleViewHolder {
        ImageView praiseIV;
        TextView praisesInfo;
        TextView replyInfo;
        TextView userNameTv;
        ImageView userHead;
        TextView creatorTimeTv;
        TextView contentTv;
        TextView titleTv;
        ImageView contentPicture;
        ImageView operationbtnView;
    }
}
