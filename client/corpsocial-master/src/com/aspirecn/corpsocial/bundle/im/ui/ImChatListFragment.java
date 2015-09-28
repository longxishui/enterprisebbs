/**
 * @(#) IM_MainTabActivity.java Created on 2013-11-5
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.ui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.QuitDialog;
import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.bundle.im.event.CancelMoveChatToTopEvent;
import com.aspirecn.corpsocial.bundle.im.event.DeleteChatEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoadLocalChatsEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoginRespNotifyEvent;
import com.aspirecn.corpsocial.bundle.im.event.LoginStartEvent;
import com.aspirecn.corpsocial.bundle.im.event.MoveChatToTopEvent;
import com.aspirecn.corpsocial.bundle.im.event.NetStatueChangeEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupSubject.DismissGroupEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberSubject.KickOutGroupMemberEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.LoginRespNotifySubject.LoginRespNotifyEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.LoginStartSubject.LoginStartEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.NetStatueChangeSubject.NetStatueChangeEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.RefreshImMainTabSubject.RefreshImMainTabEventListener;
import com.aspirecn.corpsocial.bundle.im.notify.MsgNotificationControllers;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.bundle.im.ui.ImMainTabItem.ItemType;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.ConfigPersonal;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.TimeUtil;
import com.aspiren.corpsocial.R;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeDelegate;
import com.gotye.api.GotyeMedia;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeUser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对话消息主页面
 *
 * @author lihaiqiang
 */
@EFragment(R.layout.im_chat_list_fragment)
public class ImChatListFragment extends EventFragment implements
        RefreshImMainTabEventListener, NetStatueChangeEventListener,
        KickOutGroupMemberEventListener, DismissGroupEventListener,
        LoginStartEventListener, LoginRespNotifyEventListener {

    private final int initPageSize = 20;
    private final int nextPageSize = 10;
    /**
     * 登录状态提示
     */
    @ViewById(R.id.im_main_tab_login_status_tv)
    TextView mLoginStatusTV;
    /**
     * 网络状态提示条
     */
    @ViewById(R.id.im_main_tab_net_status_tv)
    TextView mNetStatusBt;
    /**
     * 无消息空白区提示
     */
    @ViewById(R.id.im_main_tab_no_msg_tips_tv)
    TextView mNoMsgTipsTV;
    /**
     * 列表
     */
    @ViewById(R.id.im_main_tab_item)
    PullToRefreshListView mListView;
    private List<ImMainTabItem> mImMainTabItems = new ArrayList<ImMainTabItem>();
    private List<Chat> mChats = new ArrayList<Chat>();
//    private List<PubAccountChat> mPubAccountChats = new ArrayList<PubAccountChat>();
    private ImChatListAdapter mAdapter;
/** 亲加的实例 */
private GotyeAPI api = GotyeAPI.getInstance();
    @AfterViews
    void doAfterViews() {
        MsgNotificationControllers.getInstance().clearAllNotify();
        GotyeAPI.getInstance().addListener(mDelegate);
        initView();
    }

    private void initView() {
        int state=api.isOnline();
        if(state!=1){
            showOrHideNetworkStatus(true,"未连接");
        }else{
            showOrHideNetworkStatus(false, "");
        }
        updateList();
        mListView.setMode(Mode.DISABLED);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            // 下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
                int count = mImMainTabItems.size() > initPageSize ?
                        mImMainTabItems.size() : initPageSize;
                doLoadListItemDatas(LoadType.REFRESH, 0, 0, count);
                ConfigPersonal.getInstance().setLastUpdateImMainTab(System.currentTimeMillis());
            }

            // 上拉刷新
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
//                doLoadListItemDatas(LoadType.LOAD_MORE, mChats.size(),
//                        mPubAccountChats.size(), nextPageSize);
            }
        }
            );
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(true){
                    return;
                }
                ListAdapter adapter = mListView.getRefreshableView().getAdapter();

                ImMainTabItem imMainTabItem = (ImMainTabItem) adapter.getItem(position);
                ItemType itemType = imMainTabItem.getItemType();

                switch (itemType) {
                    case P2PChat: {
                        openImChatView((ChatEntity) imMainTabItem.getItemObj());
                        break;
                    }
                    case CustomGroupChat: {
                        openImChatView((ChatEntity) imMainTabItem.getItemObj());
                        break;
                    }
                    case CorpGroupChat: {
                        openImChatView((ChatEntity) imMainTabItem.getItemObj());
                        break;
                    }
                    case Teleconference: {
//                        startActivity(new Intent(getActivity(),
//                                com.aspirecn.corpsocial.bundle.appcenter.ui.TeleConferenceActivity_.class));
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        mListView.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showChatSettingDialog(view, position - 1);
                return true;
            }
        });
        mListView.setOnPullEventListener(new OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> arg0, State arg1, Mode arg2) {
                long lastUpdateTime = ConfigPersonal.getInstance().getLastUpdateImMainTab();
                String lastUpdateLabel = TimeUtil.convert(lastUpdateTime);
                arg0.getLoadingLayoutProxy().setLastUpdatedLabel(lastUpdateLabel);
            }
        });
    }
    @UiThread
    public void updateList(){
        mImMainTabItems.clear();
        List<GotyeChatTarget> sessions = api.getSessionList();
        List<ImMainTabItem> list = new ArrayList<ImMainTabItem>();
        if(sessions==null||sessions.size()==0){
            return;
        }
        for(GotyeChatTarget session :sessions){
            ImMainTabItem i = new ImMainTabItem();
            i.setItemId(String.valueOf(session.getId()));
            i.setItemType(session.getType() == GotyeChatTargetType.GotyeChatTargetTypeGroup ? ItemType.CorpGroupChat : ItemType.P2PChat);
            GotyeMessage lastMsg = api.getLastMessage(session);
            i.setTime(new Date(lastMsg.getDate()*1000));
            String content = "";
            if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeText) {
                content = lastMsg.getText();
            } else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeImage) {
                content = "图片消息";
            } else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
                content = "语音消息";
            } else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeUserData) {
                content = "自定义消息";
            }
            i.setIntro(content);
            GotyeUser user = api.getUserDetail(session, false);
            i.setTitle(user.getNickname().equals("")?user.getName():user.getNickname());
            i.setUnReadCount( api.getUnreadMessageCount(session));
            list.add(i);
        }
        mImMainTabItems.addAll(list);
        notifyDataChange();
    }
    @Override
    public void onResume() {
        super.onResume();

        int count = mImMainTabItems.size() > initPageSize ?
                mImMainTabItems.size() : initPageSize;
        doLoadListItemDatas(LoadType.REFRESH, 0, 0, count);
    }

    /**
     * 创建群
     */
    @Click(R.id.im_main_tab_add_ib)
    void onClickCreateGroupBtn() {
        startActivity(new Intent(getActivity(), CreateGroupActivity_.class));
    }

    /**
     * 搜索按钮
     */
    @Click(R.id.im_main_tab_search_ib)
    void onClickSearchBtn() {
        Intent intent = new Intent(getActivity(), SearchChatActivity_.class);
        startActivity(intent);
    }

    /**
     * 加载列表数据
     *
     * @param type
     * @param index1 im会话index
     * @param index2 公众号会话index
     * @param count
     */
    @SuppressWarnings("unchecked")
    @Background
    void doLoadListItemDatas(LoadType type, int index1, int index2, int count) {
        //im会话
        LoadLocalChatsEvent loadLocalChatsEvent = new LoadLocalChatsEvent();
        loadLocalChatsEvent.setIndex(index1);
        loadLocalChatsEvent.setCount(count);
        List<Chat> imChats = (List<Chat>) uiEventHandleFacade.handle(loadLocalChatsEvent);
        if (imChats == null) {
            imChats = new ArrayList<Chat>();
        }

        List<ImMainTabItem> mainTabItems = integrationData(count, imChats);

        //刷新界面列表
        refreshUiDataList(mainTabItems, type);
    }

    /**
     * 整合排序数据
     *
     * @param imChats
     * @return
     */
    private List<ImMainTabItem> integrationData(int count,
                                                List<Chat> imChats) {

        List<ImMainTabItem> mainTabItems = new ArrayList<ImMainTabItem>();

        for (int i = 0; i < count; i++) {

            //通过时间判断对应集合是否有数据，大于-1表示有数据
            long imChatMoveToTopTime = -1;
            long pubAccountChatMoveToTopTime = -1;
            long imChatTime = -1;
            long pubAccountChatTime = -1;

            if (imChats.size() > 0) {
                imChatMoveToTopTime = imChats.get(0).doGetMoveToTopTime();
                imChatTime = imChats.get(0).doGetChatTime().getTime();
            }
            //
            if (imChatMoveToTopTime > pubAccountChatMoveToTopTime) {
                mainTabItems.add(getItemForImChat(imChats.remove(0)));
            }
            //
            else if (pubAccountChatMoveToTopTime > imChatMoveToTopTime) {
            }
            //时间相等情况
            else if (imChatTime == pubAccountChatTime && imChatTime > -1) {
                mainTabItems.add(getItemForImChat(imChats.remove(0)));
            }
            //
            else if (imChatTime > pubAccountChatTime) {
                mainTabItems.add(getItemForImChat(imChats.remove(0)));
            }
        }

        return mainTabItems;
    }

    /**
     * 获取im会话类型item
     *
     * @param chat
     * @return
     */
    private ImMainTabItem getItemForImChat(Chat chat) {
        ImMainTabItem imMainTabItem = new ImMainTabItem();

        int doGetChatType = chat.doGetChatType();
        if (ChatType.GROUP.value == doGetChatType) {
            int doGetGroupType = chat.doGetGroupType();
            if (GroupType.DEPRT.getValue() == doGetGroupType
                    || GroupType.DEPT.getValue() == doGetGroupType) {
                imMainTabItem.setItemType(ItemType.CorpGroupChat);
            } else if (GroupType.GROUP.getValue() == doGetGroupType) {
                imMainTabItem.setItemType(ItemType.CustomGroupChat);
            } else {
                imMainTabItem.setItemType(ItemType.Other);
            }

        } else if (ChatType.P2P.value == doGetChatType) {
            imMainTabItem.setItemType(ItemType.P2PChat);
        }

        imMainTabItem.setIntro(chat.doGetChatIntro());
        imMainTabItem.setTitle(chat.doGeChatName());
        imMainTabItem.setUnReadCount(chat.doGetUnReadCount());
        imMainTabItem.setItemObj(chat.getChatEntity());
        imMainTabItem.setItemId(chat.doGetChatId());
        imMainTabItem.setUnReadCount(chat.doGetUnReadCount());
        imMainTabItem.setTime(chat.doGetChatTime());
        imMainTabItem.setMoveToTop(chat.doGetIsMoveToTop());

        mChats.add(chat);

        return imMainTabItem;
    }

    @UiThread
    void refreshUiDataList(List<ImMainTabItem> imMainTabItems, LoadType type) {
//        if (type == LoadType.REFRESH) {
//            mImMainTabItems.clear();
//        }
//        mImMainTabItems.addAll(imMainTabItems);

        notifyDataChange();
        mListView.onRefreshComplete();
    }

    private void openImChatView(ChatEntity chatEntity) {
        Intent intent = new Intent(getActivity(), ChatActivity_.class);
        Bundle bundle = new Bundle();
        //bundle.putString("corpId", chatEntity.getCorpId());
        bundle.putString("chatId", chatEntity.getChatId());
        bundle.putString("chatName", chatEntity.getChatName());
        bundle.putInt("chatType", chatEntity.getChatType());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * 长按消息item显示窗口
     *
     * @param view
     * @param position
     */
    private void showChatSettingDialog(View view, final int position) {
        final ImMainTabItem imMainTabItem = getAdapter().getImMainTabItems().get(position);

        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.setContentView(R.layout.im_main_tab_item_info_dialog);
        //主题色
        int themeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        // 设置dialog标题
        TextView titleTV = (TextView) dialog.findViewById(R.id.im_main_tab_title_tv);
        titleTV.setText(imMainTabItem.getTitle());
        titleTV.setTextColor(themeColor);
        //分隔线
        View dividerLine = dialog.findViewById(R.id.im_main_tab_divider_line_v);
        dividerLine.setBackgroundColor(themeColor);
        // 消息置顶或取消消息置顶
        TextView moveToTopTv = (TextView) dialog.findViewById(R.id.im_main_tab_move_to_top_tv);
        final boolean isSetMoveToTop = imMainTabItem.isSetMoveToTop();
        if (isSetMoveToTop) {
            moveToTopTv.setText("取消置顶");
        } else {
            moveToTopTv.setText("置顶聊天");
        }
        moveToTopTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isSetMoveToTop) {// 取消置顶
                    cancelMoveChatMessageToTop(imMainTabItem);
                } else {// 置顶
                    moveChatMessageToTop(imMainTabItem);
                }
            }
        });
        // 删除会话
        dialog.findViewById(R.id.im_main_tab_delete_chat_tv)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        deleteChatMessage(position);
                    }
                });
        dialog.show();
    }

    /**
     * 获取适配器
     *
     * @return
     */
    private ImChatListAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ImChatListAdapter(getActivity());
            mAdapter.setImMainTabItems(mImMainTabItems);
            mListView.setAdapter(getAdapter());
        }
        return mAdapter;
    }

    /**
     * 置顶聊天
     */
    @Background
    void moveChatMessageToTop(ImMainTabItem imMainTabItem) {
            MoveChatToTopEvent event = new MoveChatToTopEvent();
            event.setChatId(imMainTabItem.getItemId());
            uiEventHandleFacade.handle(event);
    }

    /**
     * 取消置顶
     *
     * @param imMainTabItem
     */
    @Background
    void cancelMoveChatMessageToTop(ImMainTabItem imMainTabItem) {
            CancelMoveChatToTopEvent event = new CancelMoveChatToTopEvent();
            event.setChatId(imMainTabItem.getItemId());
            uiEventHandleFacade.handle(event);
    }

    /**
     * 删除聊天
     */
    @Background
    void deleteChatMessage(int position) {
        List<ImMainTabItem> imMainTabItems = getAdapter().getImMainTabItems();
        ImMainTabItem imMainTabItem = imMainTabItems.get(position);

            DeleteChatEvent event = new DeleteChatEvent();
            event.setChatId(imMainTabItem.getItemId());
            uiEventHandleFacade.handle(event);

        imMainTabItems.remove(position);
        notifyDataChange();
    }

    /**
     * 刷新消息主页面
     */
    @Override
    public void onHandleRefreshImMainTabEvent(RefreshImMainTabEvent event) {
        int count = mImMainTabItems.size() > initPageSize ? mImMainTabItems.size() : initPageSize;
        doLoadListItemDatas(LoadType.REFRESH, 0, 0, count);
    }

    /**
     * 处理网络状态变更通知
     */
    @Override
    public void onHandleNetStatueChangeEvent(NetStatueChangeEvent event) {
        showOrHideNetworkStatus(!event.isNetworkAvaileble(), event.getPromptMsg());
    }

    @UiThread
    void showOrHideNetworkStatus(boolean show, String prompt) {
        if (show) {
            mNetStatusBt.setVisibility(View.VISIBLE);
            mNetStatusBt.setText(prompt);
        } else {
            mNetStatusBt.setVisibility(View.GONE);
        }
    }

    /**
     * 群解散监听方法
     */
    @Override
    public void onHandleDismissGroupEvent(DismissGroupEvent event) {
        String groupName = event.getGroupName();
        if (!TextUtils.isEmpty(groupName)) {
            showToast("群（" + groupName + "）已被解散。");
        }

//        refreshLocalChat(PULLREFRESH);
        int count = mImMainTabItems.size() > initPageSize ? mImMainTabItems.size() : initPageSize;
        doLoadListItemDatas(LoadType.REFRESH, 0, 0, count);
    }

    /**
     * 被踢监听方法
     */
    @Override
    public void onHandleKickOutGroupMemberEvent(KickOutGroupMemberEvent event) {
        String groupName = event.getGroupName();
        if (!TextUtils.isEmpty(groupName)) {
            showToast("你已被踢出群（" + event.getGroupName() + "）。");
        }

//        refreshLocalChat(PULLREFRESH);
        int count = mImMainTabItems.size() > initPageSize ? mImMainTabItems.size() : initPageSize;
        doLoadListItemDatas(LoadType.REFRESH, 0, 0, count);
    }

    /**
     * 刷新数据
     */
    @UiThread
    void notifyDataChange() {
        if (mImMainTabItems.size() > 0) {
            mNoMsgTipsTV.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mNoMsgTipsTV.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    /**
     * Toast提示
     */
    @UiThread
    void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHandleLoginStartEvent(LoginStartEvent event) {
        showOrHideLoginStatus(View.VISIBLE);
    }

    /**
     * 显示或隐藏登陆状态提示
     *
     * @param visibility
     */
    @UiThread
    void showOrHideLoginStatus(int visibility) {
        mLoginStatusTV.setVisibility(visibility);
    }

    @Override
    public void onHandleLoginRespNotifyEvent(LoginRespNotifyEvent event) {
        showOrHideLoginStatus(View.GONE);

        int respCode = event.getRespCode();
        if (respCode != ErrorCode.SUCCESS.getValue()) {
            showOrHideNetworkStatus(true, event.getMessage());
            if (event.getMessage() != null && event.getMessage().contains("不存在")) {
                new QuitDialog(getActivity()).show();
            }
        }
    }

    enum LoadType {
        INIT,//初始化
        REFRESH,//刷新
        LOAD_MORE//加载更多
    }
    private GotyeDelegate mDelegate = new GotyeDelegate(){

        @Override
        public void onDownloadMedia(int code, GotyeMedia media) {
            // TODO Auto-generated method stub
            if(getActivity().isTaskRoot()){
                getAdapter().notifyDataSetChanged();
            }
        }

        @Override
        public void onLogout(int code) {
            showOrHideNetworkStatus(true,"未连接");
        }
        @Override
        public void onLogin(int code, GotyeUser currentLoginUser) {
            showOrHideNetworkStatus(false,"未连接");
        }
        @Override
        public void onReconnecting(int code, GotyeUser currentLoginUser) {
            showOrHideNetworkStatus(false,"未连接");
        }

        @Override
        public void onReceiveMessage(GotyeMessage message) {
            super.onReceiveMessage(message);
            updateList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        GotyeAPI.getInstance().removeListener(mDelegate);
    }
}
