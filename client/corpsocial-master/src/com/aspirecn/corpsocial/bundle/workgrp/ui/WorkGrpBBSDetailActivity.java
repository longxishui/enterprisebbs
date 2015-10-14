package com.aspirecn.corpsocial.bundle.workgrp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.aspirecn.corpsocial.common.util.StringUtils;
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
    @ViewById(R.id.workgrp_detail_reply_et)
    EditText mET_reply;
    @ViewById(R.id.workgrp_detail_reply_send_iv)
    ImageView mIV_reply_btn;

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
                        TitleViewHolder titleViewHolder = (TitleViewHolder) headView
                                .getTag();
                        if (bbsItem.getBbsItemEntity().getIsPraised().equals("1")) {
                            bbsItem.getBbsItemEntity().setIsPraised("0");
                            bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity()
                                    .getPraiseTimes()) - 1) + "");
                            titleViewHolder.praiseIV.setBackgroundResource(R.drawable.workgrp_nopraise_icon2);
                        } else {
                            bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity()
                                    .getPraiseTimes()) + 1) + "");
                            bbsItem.getBbsItemEntity().setIsPraised("1");
                            titleViewHolder.praiseIV.setBackgroundResource(R.drawable.workgrp_praised_icon2);
                        }
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

    @Background
    void doDetailBBSPraise() {
        BBSReplyEvent bbsReplyEvent = new BBSReplyEvent();
        bbsReplyEvent.setReplyType(ReplyType.PRAISE);
        bbsReplyEvent.setItemId(bbsItem.getBbsItemEntity().getItemId());
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
        replyInfoEntitys = replyInfoDao.findAllReplyInfos(bbsItem.getBbsItemEntity().getItemId());
        replyListAdapter = new WorkgrpReplyListAdapter(this, replyInfoEntitys,
               bbsItem,new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDetailBBSPraise();
            }
        });
        headView = replyListAdapter.initTitleItem(this, bbsItem);
        replyListView.addHeaderView(headView);
        replyListView.setAdapter(replyListAdapter);
        mIV_reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = mET_reply.getText().toString();
                mProgressDialog.show();
                mET_reply.setText("");
                doBBSReply(reply);
            }
        });
        mIV_reply_btn.setEnabled(false);
        mET_reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            if(StringUtils.isEmpty(mET_reply.getText().toString())){
                mIV_reply_btn.setEnabled(false);
            }else{
                mIV_reply_btn.setEnabled(true);
            }
            }
        });
        doGetBBSDetail();
        boolean isOpen = getIntent().getBooleanExtra("open", false);
        if (isOpen) {
            mET_reply.requestFocus();
        }
        fab.setLifeCycleListener(this);
    }

    @Background
    void doBBSReply(String inputText) {
        BBSReplyEvent bbsReplyEvent = new BBSReplyEvent();
        bbsReplyEvent.setReplyType(ReplyType.REPLY);
        bbsReplyEvent.setItemId(bbsItem.getBbsItemEntity().getItemId());
        bbsReplyEvent.setContent(inputText);
        bbsReplyEvent.setGroupId(bbsItem.getBbsItemEntity().getGroupId());
        bbsReplyEvent.setHasPic(false);
        uiEventHandleFacade.handle(bbsReplyEvent);
    }

    @Background
    void doGetBBSDetail() {
        GetBBSDetailEvent getBBSDetailEvent = new GetBBSDetailEvent();
        getBBSDetailEvent.setBbsId(bbsItem.getBbsItemEntity().getItemId());
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
    @UiThread
    @Override
    public void onHandleGetBBSDetailRespEvent(GetBBSDetailRespEvent event) {
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
            titleViewHolder.praisesInfo
                    .setText("赞 "+mPraiseInfos.size());
        } else {
            bbsItem.getBbsItemEntity().setPraiseTimes(0 + "");
            titleViewHolder.praisesInfo.setText("赞 0");
        }
        if (mReplyInfos == null) {
            titleViewHolder.replyInfo.setText("评论 0");
            return;
        }
        int size = mReplyInfos.size();
        if (size > 0) {
            titleViewHolder.replyInfo.setText("评论 "+size);
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
                    mIntent.putExtra("itemid", bbsItem.getBbsItemEntity().getItemId());
                    startActivity(mIntent);
                }
            }).apply();
        }

    }
}
