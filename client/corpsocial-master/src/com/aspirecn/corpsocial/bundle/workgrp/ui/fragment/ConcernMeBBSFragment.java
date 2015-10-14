package com.aspirecn.corpsocial.bundle.workgrp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspirecn.corpsocial.common.util.NetTools;
import com.aspirecn.corpsocial.common.util.TimeUtil;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author duyinzhou
 */
public class ConcernMeBBSFragment extends Fragment implements GetBBSListRespEventListener, BBSReplyRespEventListener, BBSDeleteRespEventListener, CreateOrModifyBBSRespListener, OnRefreshListener2<ListView>, GetBBSDetailRespEventListener {
    private static final String TAG = "ConcernMeBBSFragment";
    private static final int ONE_LIST_COUNT = 10;
    private static final int GET_BBSLIST_RESP = 2;
    private static final int DATACHANGE = 10;
    private static final int PRAISE_FAIL = 3;
    private static final int LOAD_COMPLETE = 4;
    public UiEventHandleFacade uiEventHandleFacade;
    ArrayList<BBSItem> listData;
    /**
     * 页面正在刷新请求
     */
    boolean isRefreshing = false;
    private View view;
    /**
     * 节点控制适配器
     */
    private BBSListAdapter pullAdapter;
    /**
     * 页面的Listview
     */
    private PullToRefreshListView pullListView;
    private BBSItemDao itemDao;
    private int currentIndex = 0;
    private long lastUpdateTime = 0;
    /**
     * 我参与和我发起的类型
     */
    private int type;
    /**
     * 正在请求点赞无返回
     */
    private boolean isPraising = false;

    /**
     * 无帖子的默认图片
     */
    private LinearLayout mLL_noItem_default;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_BBSLIST_RESP:
                    GetBBSListRespEvent event = (GetBBSListRespEvent) msg.obj;
                    if (ErrorCode.SUCCESS.getValue() == event.getErrCode()) {
                        List<BBSItem> bbsItems = event.getBbsItems();
                        if (bbsItems == null || bbsItems.size() == 0) {
                            if (currentIndex != 0) {
                                Toast.makeText(getActivity(), "数据已经全部加载！", Toast.LENGTH_SHORT).show();
                                pullListView.onRefreshComplete();
                                currentIndex = 0;
                                return;
                            }
                        }
                        if (mLL_noItem_default.isShown()) {
                            mLL_noItem_default.setVisibility(View.GONE);
                        }
                        if (currentIndex == 0) {
                            listData.clear();
                            listData.addAll(getListBBSItem(type));
                        } else {
                            listData.clear();
                            listData.addAll(getListBBSItem(type));
//						listData.addAll(bbsItems);
                        }
                        pullAdapter.notifyDataSetChanged();
                    } else {
                        if (event.getErrCode() == ErrorCode.OTHER_ERROR.getValue() && event.getBbsItems() == null) {
                            Toast.makeText(getActivity(), "数据已经全部加载！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    pullListView.onRefreshComplete();
                    currentIndex = 0;
                    break;
                case DATACHANGE:
                    pullAdapter.notifyDataSetChanged();
                    isPraising = false;
//				pAdapter.doPraiseCallBack((Integer) msg.obj);
                    break;
                case PRAISE_FAIL:
                    Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    isPraising = false;
                    break;
                case LOAD_COMPLETE:
                    pullListView.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }
    };
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "before onCreate");
        super.onCreate(savedInstanceState);
        uiEventHandleFacade = UiEventHandleFacade.getInstance();
        itemDao = new BBSItemDao();
        type = getArguments().getInt("type");
        listData = getListBBSItem(type);
        Log.i(TAG, "after onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.workgrp_concernme_fragment, null);
            mLL_noItem_default = (LinearLayout) view.findViewById(R.id.workgrp_tab_fragment_noitem_ll);
            if (listData == null || listData.size() == 0) {
                mLL_noItem_default.setVisibility(View.VISIBLE);
            } else {
                mLL_noItem_default.setVisibility(View.GONE);
            }
            pullListView = (PullToRefreshListView) view.findViewById(R.id.workgrp_tab_frgment_list);
            pullAdapter = new BBSListAdapter(getActivity(), listData, praiseListener);
            pullListView.setMode(Mode.PULL_UP_TO_REFRESH);
            pullListView.setAdapter(pullAdapter);
            pullListView.setOnRefreshListener(this);
            pullListView.setOnPullEventListener(new OnPullEventListener<ListView>() {

                @Override
                public void onPullEvent(PullToRefreshBase<ListView> arg0, State arg1, Mode arg2) {
                    String lastUpdateLabel = "";
                    if (lastUpdateTime == 0) {
                        lastUpdateLabel = "";
                    } else {
                        lastUpdateLabel = TimeUtil.convert(lastUpdateTime);
                    }
                    arg0.getLoadingLayoutProxy().setLastUpdatedLabel(lastUpdateLabel);
                }
            });
            EventListenerSubjectLoader instance = EventListenerSubjectLoader
                    .getInstance();
            instance.registerListener(this);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }

        return view;
    }

    /**
     * 获取BBS列表列表
     */
    private ArrayList<BBSItem> getListBBSItem(int type) {
        switch (type) {
            case 0:
                return (ArrayList<BBSItem>) itemDao.findConcernMeItemsCreate(Config.getInstance().getCorpId());
            case 1:
                return (ArrayList<BBSItem>) itemDao.findConcernMeItemsPartin(Config.getInstance().getCorpId());
            default:
                return (ArrayList<BBSItem>) itemDao.findConcernMeItemsCreate(Config.getInstance().getCorpId());
        }
    }

    @Override
    public void onHandleGetBBSListRespEvent(GetBBSListRespEvent event) {
        // TODO Auto-generated method stub
        LogUtil.i("得到的groupId为" + event.getGroupId());
        isRefreshing = false;
        Message msg = new Message();
        msg.obj = event;
        msg.what = GET_BBSLIST_RESP;
        handler.sendMessage(msg);
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
        getBBSListEvent.setGroupId("-1");
        getBBSListEvent.setStartPos(startPos);
        currentIndex = startPos;
        getBBSListEvent.setLastCreateTime(lastCreateTime);
        getBBSListEvent.setLastModifyTime(startPos == 0 ? itemDao.getLastModifyTime() : 0);
        getBBSListEvent.setLimit(ONE_LIST_COUNT);
        getBBSListEvent.setConcernMe(true);
        uiEventHandleFacade.handle(getBBSListEvent);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        EventListenerSubjectLoader instance = EventListenerSubjectLoader
                .getInstance();
        instance.unregisterListener(this);
        super.onDestroy();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
        lastUpdateTime = new Date().getTime();
        doGetBBSList(0, listData.size() == 0 ? 0 : listData.get(0).getBbsItemEntity().getCreateTime());
    }

    /**
     * 上拉加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
        /** 如果没有联网，查询本地数据库 */
        if (NetTools.checkConnectedNetWork(getActivity()) == null) {
//			final List<BBSItem> items = itemDao.findAllConcernMeItems(listData.size()+1);
//			listData.addAll(items);
            handler.sendEmptyMessage(LOAD_COMPLETE);
        } else {
            lastUpdateTime = new Date().getTime();
            doGetBBSList(listData.size() + 1, listData.size() == 0 ? 0 : listData.get(listData.size() - 1).getBbsItemEntity().getCreateTime());
        }
    }

    @Override
    public void onHandleBBSReplyRespEvent(BBSReplyRespEvent bbsReplyRespEvent) {
        // TODO Auto-generated method stub
        if (listData == null) {
            return;
        }
        for (BBSItem bbsItem : listData) {
            if (bbsReplyRespEvent.getBbsId().equals(bbsItem.getBbsItemEntity().getItemId())) {
                if (bbsReplyRespEvent.getType() == ReplyType.PRAISE) {
                    if (bbsItem.getBbsItemEntity().getIsPraised().equals("1")) {
                        bbsItem.getBbsItemEntity().setIsPraised("0");
                        bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes()) - 1) + "");
                    } else {
                        bbsItem.getBbsItemEntity().setIsPraised("1");
                        bbsItem.getBbsItemEntity().setPraiseTimes((Integer.valueOf(bbsItem.getBbsItemEntity().getPraiseTimes()) + 1) + "");
                    }
                    itemDao.updatePraiseUserIds(bbsItem.getBbsItemEntity().getItemId(), bbsItem.getBbsItemEntity().getPraiseTimes()+"",bbsItem.getBbsItemEntity().getIsPraised());
                } else {
                    String replyTimes = (Integer.valueOf(bbsItem.getBbsItemEntity().getReplyTimes()) + 1) + "";
                    bbsItem.getBbsItemEntity().setReplyTimes(replyTimes);
                    itemDao.updateReplyTimes(bbsItem.getBbsItemEntity().getItemId(), replyTimes);
                }
                handler.sendEmptyMessage(DATACHANGE);
                break;
            }
        }
    }

    @Override
    public void onHandleBBSDeleteRespEvent(BBSDeleteRespEvent bbsDeleteRespEvent) {
        // TODO Auto-generated method stub
        if (bbsDeleteRespEvent.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (bbsDeleteRespEvent.getBbsItemId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    if (bbsDeleteRespEvent.getDeleteType() == DeleteType.ITEM) {
                        listData.remove(bbsitem);
                    } else {
                        listData.clear();
                        listData.addAll(getListBBSItem(type));
                    }
                    handler.sendEmptyMessage(DATACHANGE);
                    break;
                }
            }
        }
    }

    @Override
    public void onHandleCreateOrModifyBBSRespEvent(
            CreateOrModifyBBSRespEvent createOrModifyBBSRespEvent) {
        // TODO Auto-generated method stub
        if (createOrModifyBBSRespEvent.getErrorCode()
                == ErrorCode.SUCCESS.getValue()) {
            lastUpdateTime = new Date().getTime();
            doGetBBSList(0, listData.size() == 0 ? 0 : listData.get(0).getBbsItemEntity().getCreateTime());
        }
    }

    @Override
    public void onHandleGetBBSDetailRespEvent(GetBBSDetailRespEvent event) {
        // TODO Auto-generated method stub
        if (event.getErrorCode() == ErrorCode.OTHER_ERROR.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (event.getBbsId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    listData.remove(bbsitem);
                    handler.sendEmptyMessage(DATACHANGE);
                    break;
                }
            }
        } else if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            for (BBSItem bbsitem : listData) {
                if (event.getBbsId().equals(bbsitem.getBbsItemEntity().getItemId())) {
                    bbsitem.getBbsItemEntity().setPraiseTimes(event.getPraiseInfos().size() + "");
                    bbsitem.getBbsItemEntity().setReplyTimes(event.getBbsReplyInfoEntities().size() + "");
                    handler.sendEmptyMessage(DATACHANGE);
                    break;
                }
            }
        }
    }
}
