/**
 *
 */
package com.aspirecn.corpsocial.bundle.workgrp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSReplyRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.CreateOrModifyBBSRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSDetailRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSListEvent;
import com.aspirecn.corpsocial.bundle.workgrp.event.GetBBSListRespEvent;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSDeleteRespSubject.BBSDeleteRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.BBSReplyRespSubject.BBSReplyRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.CreateOrModifyBBSRespSubject.CreateOrModifyBBSRespListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSDetailRespSubject.GetBBSDetailRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.listener.GetBBSListRespSubject.GetBBSListRespEventListener;
import com.aspirecn.corpsocial.bundle.workgrp.repository.BBSItemDao;
import com.aspirecn.corpsocial.bundle.workgrp.ui.BBSListAdapter;
import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.bundle.workgrp.util.ReplyType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.NetTools;
import com.aspirecn.corpsocial.common.util.TimeUtil;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author duyinzhou
 */
@EFragment(R.layout.workgrp_tab_fragment)
public class BaseBBSFragment extends EventFragment implements
        GetBBSListRespEventListener, BBSReplyRespEventListener,
        BBSDeleteRespEventListener, CreateOrModifyBBSRespListener,
        OnRefreshListener2<ListView>, GetBBSDetailRespEventListener {
    private static final String TAG = "PersonalSmsFragment";
    /**
     * 页面的Listview
     */
    @ViewById(R.id.workgrp_tab_frgment_list)
    PullToRefreshListView pullListView;
    List<BBSItem> listData = new ArrayList<BBSItem>();
    /**
     * 页面正在刷新请求
     */
    boolean isRefreshing = false;
    /**
     * 无帖子的默认图片
     */
    @ViewById(R.id.workgrp_tab_fragment_noitem_ll)
    LinearLayout mLL_noItem_default;
    /**
     * 节点控制适配器
     */
    private BBSListAdapter pullAdapter;
    private BBSItemDao itemDao;
    private String groupId;
    private int currentIndex = 0;
    private long lastUpdateTime = 0;
    /**
     * 正在请求点赞无返回
     */
    private boolean isPraising = false;
    private long lastRefreshTime = 0l;
    private boolean isCreate = true;
    private boolean isFristTab = false;
    private int limit = 10;
    /**
     * 点赞的监听事件
     */
    private OnClickListener praiseListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isPraising) {
                Toast.makeText(getActivity(), "正在处理中...", Toast.LENGTH_SHORT).show();
                return;
            }
            BBSItem bbsItem = (BBSItem) v.getTag();
            BBSReplyEvent bbsReplyEvent = new BBSReplyEvent();
            bbsReplyEvent.setGroupId(bbsItem.getBbsItemEntity().getGroupId());
            bbsReplyEvent.setReplyType(ReplyType.PRAISE);
            bbsReplyEvent.setItemId(bbsItem.getBbsItemEntity().getItemId());
            UiEventHandleFacade.getInstance().handle(bbsReplyEvent);
            isPraising = true;
        }
    };

    @AfterViews
    void doAfterView() {
        Log.i(TAG, "before onCreate");
        itemDao = new BBSItemDao();
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.groupId = arguments.getString("groupid");
            this.isFristTab = arguments.getBoolean("isFirstTab", false);
        }
        pullListView.setMode(Mode.BOTH);
        pullListView.setAdapter(getAdapter());
        pullListView.setOnRefreshListener(this);
        pullListView
                .setOnPullEventListener(new OnPullEventListener<ListView>() {
                    @Override
                    public void onPullEvent(
                            PullToRefreshBase<ListView> arg0, State arg1,
                            Mode arg2) {
                        String lastUpdateLabel = "";
                        if (lastUpdateTime == 0) {
                            lastUpdateLabel = "";
                        } else {
                            lastUpdateLabel = TimeUtil
                                    .convert(lastUpdateTime);
                        }
                        arg0.getLoadingLayoutProxy().setLastUpdatedLabel(
                                lastUpdateLabel);
                    }
                });
        getListBBSItem();
    }

    private BBSListAdapter getAdapter() {
        if (pullAdapter == null) {
            pullAdapter = new BBSListAdapter(getActivity(), listData,
                    praiseListener);
        }
        return pullAdapter;
    }

    @UiThread
    public void notifyAdapterChange() {
        if (listData == null || listData.size() == 0) {
            mLL_noItem_default.setVisibility(View.VISIBLE);
        } else {
            mLL_noItem_default.setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if ((new Date().getTime() - lastRefreshTime) > 1000 * 60 * 1) {
            lastRefreshTime = new Date().getTime();
            if (listData.size() == 0) {
                doGetBBSList(0, 0);
            } else {
                doGetBBSList(0, listData.size() == 0 ? 0 : listData.get(0)
                        .getBbsItemEntity().getCreateTime());
            }
        }
        super.onResume();
    }

    /**
     * 获取BBS列表列表
     */
    void getListBBSItem() {
        listData.clear();
        listData.addAll(itemDao.findAllItems(groupId, 0, limit));
        notifyAdapterChange();
    }

    @UiThread
    @Override
    public void onHandleGetBBSListRespEvent(GetBBSListRespEvent event) {
        // TODO Auto-generated method stub
        if (event.getGroupId().equals(groupId)) {
            isRefreshing = false;
            if (ErrorCode.SUCCESS.getValue() == event.getErrCode()) {
                List<BBSItem> bbsItems = event.getBbsItems();
                if (bbsItems == null || bbsItems.size() == 0) {
                    if (currentIndex != 0) {
                        Toast.makeText(getActivity(), "数据已经全部加载！", Toast.LENGTH_SHORT)
                                .show();
                        Log.i("BaseBBSFragment", "currentIndex!=0，上拉加载完毕");
                        pullListView.onRefreshComplete();
                        currentIndex = 0;
                        return;
                    }
                }
                if (mLL_noItem_default.getVisibility() == View.VISIBLE) {
                    mLL_noItem_default.setVisibility(View.GONE);
                }
                if (currentIndex == 0) {
                    getListBBSItem();
                } else {
                    listData.addAll(bbsItems);
                }
                pullAdapter.notifyDataSetChanged();
            } else {
                if (event.getErrCode() == ErrorCode.OTHER_ERROR.getValue()
                        && event.getBbsItems() == null) {
                    Toast.makeText(getActivity(), "数据已经全部加载！",
                            Toast.LENGTH_SHORT).show();
                    Log.i("BaseBBSFragment", "errorCode为Other_error");
                }
            }
            pullListView.onRefreshComplete();
            currentIndex = 0;
        }
    }

    /**
     * 下拉刷新传lastcreateTime和lastModifyTime,上拉加载传lastCreateTime
     */
    private void doGetBBSList(int startPos, long lastCreateTime) {
        // TODO Auto-generated method stub
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        GetBBSListEvent getBBSListEvent = new GetBBSListEvent();
        getBBSListEvent.setGroupId(groupId);
        getBBSListEvent.setStartPos(startPos);
        currentIndex = startPos;
        getBBSListEvent.setLastCreateTime(lastCreateTime);
        getBBSListEvent.setLastModifyTime(startPos == 0 ? itemDao
                .getLastModifyTime() : 0);
        getBBSListEvent.setLimit(limit);
        uiEventHandleFacade.handle(getBBSListEvent);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
        lastUpdateTime = new Date().getTime();
        doGetBBSList(0, listData.size() == 0 ? 0 : listData.get(0).getBbsItemEntity()
                .getCreateTime());
    }

    /**
     * 上拉加载
     */
    @UiThread
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
        /** 如果没有联网，查询本地数据库 */
        if (NetTools.checkConnectedNetWork(getActivity()) == null) {
            final List<BBSItem> items = itemDao.findAllItems(groupId,
                    listData.size() + 1, limit);
            listData.addAll(items);
            pullListView.onRefreshComplete();
        } else {
            lastUpdateTime = new Date().getTime();
            doGetBBSList(listData.size() + 1, listData.size() == 0 ? 0
                    : listData.get(listData.size() - 1).getBbsItemEntity().getCreateTime());
        }
    }

    @UiThread
    @Override
    public void onHandleBBSReplyRespEvent(BBSReplyRespEvent bbsReplyRespEvent) {
        // TODO Auto-generated method stub
        if (listData == null || !bbsReplyRespEvent.getGroupId().equals(groupId)) {
            return;
        }
        if (bbsReplyRespEvent.getErrorCode() != ErrorCode.SUCCESS.getValue()) {
            if (bbsReplyRespEvent.getType() == ReplyType.PRAISE) {
                Toast.makeText(getActivity(), "点赞失败", Toast.LENGTH_SHORT).show();
                isPraising = false;
            }
            return;
        }
        for (BBSItem bbsItem : listData) {
            if (bbsReplyRespEvent.getBbsId().equals(bbsItem.getBbsItemEntity().getItemId())) {
                if (bbsReplyRespEvent.getType() == ReplyType.PRAISE) {
                    if (bbsItem.getBbsItemEntity().getIsPraised().equals("1")) {
                        bbsItem.getBbsItemEntity().setIsPraised("0");
                        bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes())-1)+"");
                    } else {
                        bbsItem.getBbsItemEntity().setIsPraised("1");
                        bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes())+1)+"");
                    }
                    itemDao.updatePraiseUserIds(bbsItem.getBbsItemEntity().getItemId(), bbsItem.getBbsItemEntity().getPraiseTimes()+"",bbsItem.getBbsItemEntity().getIsPraised());
                } else {
                    String replyTimes = (Integer.valueOf(bbsItem.getBbsItemEntity()
                            .getReplyTimes()) + 1) + "";
                    bbsItem.getBbsItemEntity().setReplyTimes(replyTimes);
                    itemDao.updateReplyTimes(bbsItem.getBbsItemEntity().getItemId(), replyTimes);
                }
                isPraising = false;
                pullAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onHandleBBSDeleteRespEvent(BBSDeleteRespEvent bbsDeleteRespEvent) {
        // TODO Auto-generated method stub
        if (bbsDeleteRespEvent.getGroupId().equals(groupId)
                || bbsDeleteRespEvent.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (bbsDeleteRespEvent.getBbsItemId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    if (bbsDeleteRespEvent.getDeleteType() == DeleteType.ITEM) {
                        listData.remove(bbsitem);
                    } else {
                        bbsitem.getBbsItemEntity().setReplyTimes((Integer.valueOf(bbsitem.getBbsItemEntity()
                                .getReplyTimes()) - 1) + "");
                    }
                    notifyAdapterChange();
                    break;
                }
            }
        }
    }

    @Override
    public void onHandleCreateOrModifyBBSRespEvent(
            CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent) {
        // TODO Auto-generated method stub
        if (createOrModifyBBSRespEvent.getGroupId().equals(groupId)
                && createOrModifyBBSRespEvent.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            lastUpdateTime = new Date().getTime();
            doGetBBSList(0, listData.size() == 0 ? 0 : listData.get(0).getBbsItemEntity()
                    .getCreateTime());
        }
    }

    @Override
    public void onHandleGetBBSDetailRespEvent(GetBBSDetailRespEvent event) {
        // TODO Auto-generated method stub
        if (event.getErrorCode() == ErrorCode.OTHER_ERROR.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (event.getBbsId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    listData.remove(bbsitem);
                    notifyAdapterChange();
                    break;
                }
            }
        } else if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (event.getBbsId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    bbsitem.getBbsItemEntity().setPraiseTimes(event.getPraiseInfos().size() + "");
                    bbsitem.getBbsItemEntity().setReplyTimes(event.getBbsReplyInfoEntities().size() + "");
                    notifyAdapterChange();
                    break;
                }
            }
        }
    }
}
