package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.domain.KeyValue;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSDeleteRespSubject.BBSDeleteRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSReplyRespSubject.BBSReplyRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.CreateOrModifyBBSRespSubject.CreateOrModifyBBSRespListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSDetailRespSubject.GetBBSDetailRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSReplyInfoDao;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.ui.WorkgrpReplyListAdapter.TitleViewHolder;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BBSUtil;
import com.aspirecn.corpsocial.bundle.workgrp.ui.widget.BottomInputView;
import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.bundle.workgrp.util.DetailType;
import com.aspirecn.corpsocial.bundle.workgrp.util.ReplyType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.util.BitmapUtil;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.workgrp_bbs_detail_activity)
public class WorkGrpBBSDetailActivity extends EventFragmentActivity implements
        BBSReplyRespEventListener, GetBBSDetailRespEventListener,
        CreateOrModifyBBSRespListener, BBSDeleteRespEventListener, ActionBarFragment.LifeCycleListener {

    private static final int REPLY_OK = 1;
    private static final int PRAISE_OK = 2;
    /**
     * 删除成功
     */
    private static final int REPLYDETELTE_SUCCESS = 4;
    /**
     * 删除失败
     */
    private static final int DETELTE_FAIL = 5;
    /**
     * 删除成功
     */
    private static final int BBSDETELTE_SUCCESS = 6;
    private static final int EDITBBS_SUCCESS = 7;
    @ViewById(R.id.workgrp_detail_reply_listid)
    ListView replyListView;
    @ViewById(R.id.workgrp_detail_bivid)
    BottomInputView biv;

    private BBSItem bbsItem = null;
    private List<BBSReplyInfoEntity> replyInfoEntitys = null;
    private WorkgrpReplyListAdapter replyListAdapter = null;
    private ProgressDialog mProgressDialog = null;
    private String bbsId;
    private BBSReplyInfoDao replyInfoDao = null;
    // private FileInfoDao fileDao = null;
    private BBSItemDao itemDao = null;
    /**
     * listView的HeadView,用来显示帖子内容
     */
    private View headView;
    private int actId = 2;
    private Handler bbsDetailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REPLY_OK:
                    BBSReplyRespEvent bbsReplyRespEvent = (BBSReplyRespEvent) msg.obj;
                    if (ErrorCode.SUCCESS.getValue() == bbsReplyRespEvent
                            .getErrorCode()) {
                        String replyTimes = (Integer.valueOf(bbsItem.getBbsItemEntity()
                                .getReplyTimes()) + 1) + "";
                        bbsItem.getBbsItemEntity().setReplyTimes(replyTimes);
                        biv.clearBIVStatus();
                        doGetBBSDetail();
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "评论发表成功！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "评论发表失败，请检查网络并重试！",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PRAISE_OK:
                    BBSReplyRespEvent bbsPraiseRespEvent = (BBSReplyRespEvent) msg.obj;
                    if (ErrorCode.SUCCESS.getValue() == bbsPraiseRespEvent
                            .getErrorCode()) {
                        ArrayList<String> praiseUserIds = bbsItem
                                .getPraiseUseridList();
                        String userID = Config.getInstance().getUserId();
                        if (praiseUserIds
                                .contains(Config.getInstance().getUserId())) {
                            praiseUserIds.remove(userID);
                            bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity()
                                    .getPraiseTimes()) - 1) + "");
                        } else {
                            bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity()
                                    .getPraiseTimes()) + 1) + "");
                            praiseUserIds.add(userID);
                        }
                        TitleViewHolder titleViewHolder = (TitleViewHolder) headView
                                .getTag();
                            titleViewHolder.praisesInfo
                                    .setText("赞 "+bbsItem.getBbsItemEntity().getPraiseTimes());
                    } else {
                        Toast.makeText(WorkGrpBBSDetailActivity.this,
                                "点赞失败，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REPLYDETELTE_SUCCESS:
                    replyListAdapter.notifyDataSetChanged();
                    Toast.makeText(WorkGrpBBSDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
                case DETELTE_FAIL:
                    Toast.makeText(WorkGrpBBSDetailActivity.this, "删除失败，请检查网络！", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case BBSDETELTE_SUCCESS:
                    Toast.makeText(WorkGrpBBSDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case EDITBBS_SUCCESS:
                    replyListView.removeHeaderView(headView);
                    headView = replyListAdapter.initTitleItem(
                            WorkGrpBBSDetailActivity.this, bbsItem);
                    replyListView.addHeaderView(headView);
                    break;
                default:
                    break;
            }
        }
    };

   private void doClickReplyLayout() {
        biv.setVisibility(View.VISIBLE);
        biv.getInputEditText().requestFocus();
        BBSUtil.openInputMethod(this, biv.getInputEditText());
    }

   private void doClickPraiseLayout() {
        doDetailBBSPraise();
    }

    @Background
    void doDetailBBSPraise() {
        BBSReplyEvent bbsReplyEvent = new BBSReplyEvent();
        bbsReplyEvent.setReplyType(ReplyType.PRAISE);
        bbsReplyEvent.setItemId(bbsItem.getBbsItemEntity().getId());
        bbsReplyEvent.setGroupId(bbsItem.getBbsItemEntity().getGroupId());
        uiEventHandleFacade.handle(bbsReplyEvent);
    }

    @AfterViews
    protected void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.common_main_tab_colleagues)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

//		mBackBtn.setTextColor(R.string.common_main_tab_colleagues);
        mProgressDialog = new ProgressDialog(WorkGrpBBSDetailActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("正在发表评论,请稍候...");
        itemDao = new BBSItemDao();
        bbsId = getIntent().getStringExtra("bbsid");
        bbsItem = itemDao.findItemById(bbsId);
        if (bbsItem == null) {
            Toast.makeText(this, "帖子已删除", Toast.LENGTH_SHORT).show();
            EventListenerSubjectLoader instance = EventListenerSubjectLoader
                    .getInstance();
            GetBBSDetailRespEvent rsp = new GetBBSDetailRespEvent();
            rsp.setBbsId(bbsId);
            rsp.setErrorCode(ErrorCode.OTHER_ERROR.getValue());
            instance.notifyListener(rsp);
            finish();
        }
        replyInfoDao = new BBSReplyInfoDao();
        replyInfoEntitys = replyInfoDao.findAllReplyInfos(bbsItem.getBbsItemEntity().getId());
        replyListAdapter = new WorkgrpReplyListAdapter(this, replyInfoEntitys,
                bbsItem);
        headView = replyListAdapter.initTitleItem(this, bbsItem);
        replyListView.addHeaderView(headView);
        replyListView.setAdapter(replyListAdapter);

        biv.setSendButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = biv.getInputResult();
                if (TextUtils.isEmpty(reply)
                        && TextUtils.isEmpty(biv.getPicture_path())) {
                    Toast.makeText(getApplicationContext(), "评论为空，请输入评论内容后重试！",
                            Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("发送的内容为：" + reply + "图片地址为:"
                            + biv.getPicture_path());
                    mProgressDialog.show();
                    doBBSReply(reply);
                }
            }
        });
        doGetBBSDetail();
        boolean isOpen = getIntent().getBooleanExtra("open", false);
        if (isOpen) {
            biv.setVisibility(View.VISIBLE);
            biv.getInputEditText().requestFocus();
            BBSUtil.openInputMethod(this, biv.getInputEditText());
        }
        fab.setLifeCycleListener(this);
    }

    @Background
    void doBBSReply(String inputText) {
        BBSReplyEvent bbsReplyEvent = new BBSReplyEvent();
        bbsReplyEvent.setReplyType(ReplyType.REPLY);
        bbsReplyEvent.setItemId(bbsItem.getBbsItemEntity().getId());
        bbsReplyEvent.setContent(inputText);
        bbsReplyEvent.setGroupId(bbsItem.getBbsItemEntity().getGroupId());
        if (biv.getPicture_path() != null && !biv.getPicture_path().isEmpty()) {
            bbsReplyEvent.setHasPic(true);
            bbsReplyEvent.setPath(biv.getPicture_path());
        }
        uiEventHandleFacade.handle(bbsReplyEvent);
    }

    @Background
    void doGetBBSDetail() {
        GetBBSDetailEvent getBBSDetailEvent = new GetBBSDetailEvent();
        getBBSDetailEvent.setBbsId(bbsItem.getBbsItemEntity().getId());
        getBBSDetailEvent.setDetailType(DetailType.ALL);
        getBBSDetailEvent.setLimit(100);
        getBBSDetailEvent.setStartPos(0);
        uiEventHandleFacade.handle(getBBSDetailEvent);
    }

    @Override
    public void onHandleBBSReplyRespEvent(BBSReplyRespEvent bbsReplyRespEvent) {
        if (!bbsReplyRespEvent.getBbsId().equals(bbsId)) {
            return;
        }
        if (ReplyType.REPLY == bbsReplyRespEvent.getType()) {
            Message msg = bbsDetailHandler.obtainMessage();
            msg.what = REPLY_OK;
            msg.obj = bbsReplyRespEvent;
            msg.sendToTarget();
        } else if (ReplyType.PRAISE == bbsReplyRespEvent.getType()) {
            Message msg = bbsDetailHandler.obtainMessage();
            msg.what = PRAISE_OK;
            msg.obj = bbsReplyRespEvent;
            msg.sendToTarget();
        }
    }

    private SpannableString getNamesByIds(ArrayList<String> userIds) {
        ArrayList<KeyValue> pUserNames = new ArrayList<KeyValue>();
        List<User> listcontactVO = replyListAdapter
                .getcontactVO(userIds);
        if (listcontactVO != null && listcontactVO.size() > 0) {
            for (User cb : listcontactVO) {
                pUserNames.add(new KeyValue(cb.getUserid(), cb.getName()));
            }
        } else {
            for (String userid : userIds) {
                pUserNames.add(new KeyValue(userid, userid));
            }
        }
        return BBSUtil.getPraisedUserTip(pUserNames);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // private Intent getBack(){
    // Intent mIntent = new Intent();
    // mIntent.putExtra("itemid", bbsItem.getId());
    // return mIntent;
    // }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BBSUtil.BBS_REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                BitmapUtil.correction(path);
                biv.showPicture(path);
            }
        } else if (requestCode == BBSUtil.BBS_REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                String imagePath = biv.getPicture_path();
                BitmapUtil.correction(imagePath);
                biv.showPicture(imagePath);
            }
        }
    }
    @UiThread
    @Override
    public void onHandleGetBBSDetailRespEvent(GetBBSDetailRespEvent event) {
        Log.e("WorkGrpBBSDetailActivity","触发获取详情监听");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        bbsItem.getBbsItemEntity().setLastModifiedTime(System.currentTimeMillis());
        if (event.getErrorCode() != ErrorCode.SUCCESS.getValue()) {
            if (event.getErrorCode() == ErrorCode.OTHER_ERROR
                    .getValue()) {
                Toast.makeText(WorkGrpBBSDetailActivity.this, "帖子已删除",
                        Toast.LENGTH_SHORT).show();
                WorkGrpBBSDetailActivity.this.finish();
            }
            return;
        }
        List<BBSReplyInfoEntity> mReplyInfos = event.getBbsReplyInfoEntities();
        List<String> mPraiseInfos = event.getPraiseInfos();
        TitleViewHolder titleViewHolder = (TitleViewHolder) headView
                .getTag();
        if (mPraiseInfos != null && mPraiseInfos.size() > 0) {
            bbsItem.getBbsItemEntity().setPraiseTimes(mPraiseInfos.size() + "");
            ArrayList<String> praiseUserIds = new ArrayList<String>();
            for (String praiseInfo : mPraiseInfos) {
                praiseUserIds.add(praiseInfo);
            }
            bbsItem.setPraiseUseridList(praiseUserIds);
            titleViewHolder.praisesInfo
                    .setText("赞 "+mPraiseInfos.size());
        } else {
            bbsItem.getBbsItemEntity().setPraiseTimes(0 + "");
            titleViewHolder.praisesInfo.setText("赞 0");
        }
        if (mReplyInfos == null) {
            return;
        }
        int size = mReplyInfos.size();
        if (size > 0) {
            replyInfoEntitys.clear();
            Log.e("WorkGrpBBSDetail","评论数："+size);
            replyInfoEntitys.addAll(mReplyInfos);
            replyListAdapter.notifyDataSetChanged();
            bbsItem.getBbsItemEntity().setReplyTimes(replyInfoEntitys.size() + "");
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onHandleBBSDeleteRespEvent(BBSDeleteRespEvent bbsDeleteRespEvent) {
        // TODO Auto-generated method stub
        if (bbsDeleteRespEvent.getBbsItemId().equals(bbsId)) {

            if (bbsDeleteRespEvent.getErrorCode() == ErrorCode.SUCCESS
                    .getValue()) {
                if (bbsDeleteRespEvent.getDeleteType() == DeleteType.ITEM) {
                    bbsDetailHandler.sendEmptyMessage(BBSDETELTE_SUCCESS);
                } else {
                    for (BBSReplyInfoEntity bbsreply : replyInfoEntitys) {
                        if (bbsDeleteRespEvent.getReplyId().equals(
                                bbsreply.getId())) {
                            replyInfoEntitys.remove(bbsreply);
                            bbsItem.getBbsItemEntity().setReplyTimes(replyInfoEntitys.size() + "");
                            break;
                        }
                    }
                    bbsDetailHandler.sendEmptyMessage(REPLYDETELTE_SUCCESS);
                }
            } else {
                bbsDetailHandler.sendEmptyMessage(DETELTE_FAIL);
            }
        }
    }

    @Override
    public void onHandleCreateOrModifyBBSRespEvent(
            CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent) {
        // TODO Auto-generated method stub
        if (createOrModifyBBSRespEvent.getItemId().equals(bbsId)
                && createOrModifyBBSRespEvent.getErrorCode() == ErrorCode.SUCCESS
                .getValue()) {
            bbsItem.setBbsItemEntity(createOrModifyBBSRespEvent.getBbsItemEntity());
            bbsDetailHandler.sendEmptyMessage(EDITBBS_SUCCESS);
        }
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {

        if (bbsItem.getBbsItemEntity().getCreatorId().equals(
                Config.getInstance()
                        .getUserId())) {
            fab.build().setFirstButtonText("编辑").apply();
            fab.build().setFirstButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(
                            WorkGrpBBSDetailActivity.this,
                            com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpNewBBSActivity_.class);
                    mIntent.putExtra("itemid", bbsItem.getBbsItemEntity().getId());
                    startActivity(mIntent);
                }
            }).apply();
        }

    }
}
