package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyDismissGroupEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyKickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.NotifyRefreshMyGroupEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyDismissGroupSubject.NotifyDismissGroupEventListener;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyKickOutGroupMemberSubject.NotifyKickOutGroupMemberEventListener;
import com.aspirecn.corpsocial.bundle.addrbook.listener.NotifyRefreshMyGroupSubject.NotifyRefreshMyGroupEventListener;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.facade.Group;
import com.aspirecn.corpsocial.bundle.im.facade.LoadMyGroupService;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupRespSubject.DismissGroupRespEventListener;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 我的群组
 *
 * @author chenziqiang
 */
@EFragment(R.layout.addrbook_contacts_activity)
public class AddrbookGroupFragment extends EventFragment implements
//		UIRefreshContactsRespEventListener,
        NotifyKickOutGroupMemberEventListener, NotifyDismissGroupEventListener,
        DismissGroupRespEventListener, CreateUpdateGroupRespEventListener,
        NotifyRefreshMyGroupEventListener {
    /**
     * 控件初始化
     */
    @ViewById(R.id.addrbook_contacts_list)
    PullToRefreshListView ContactsList;
    @ViewById(R.id.addrbook_contact_nodata_rl)
    RelativeLayout nodataRl;
    @ViewById(R.id.addrbook_contact_nodata_text)
    TextView nodataText;
    @ViewById(R.id.addrbook_contact_nodata_img)
    ImageView nodataImg;
    @ViewById(R.id.add_conatacts_rl)
    RelativeLayout conatacts_rl;
    private AddrbookGroupAdapter groupadapter;
    private boolean groupContactsAddData = true;// 是否进行重新加载，为true则加载数据，否则为false

    @AfterViews
    void start() {
        conatacts_rl.setVisibility(View.GONE);
        FetchFrequentlyContacts();
        ContactsList.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            // 下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(1000);
                        FetchFrequentlyContacts();
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        groupadapter.notifyDataSetChanged();
                        ContactsList.onRefreshComplete();

                    }

                    @Override
                    protected void onCancelled() {
                        ContactsList.onRefreshComplete();
                    }

                }.execute();
            }

            // 上拉刷新
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        SystemClock.sleep(1000);
                        if (isCancelled()) {
                            return null;
                        }
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        groupadapter.notifyDataSetChanged();
                        ContactsList.onRefreshComplete();
                    }

                    protected void onCancelled() {
                        ContactsList.onRefreshComplete();
                    }
                }.execute();
            }
        });

        ContactsList
                .setOnPullEventListener(new OnPullEventListener<ListView>() {

                    @Override
                    public void onPullEvent(PullToRefreshBase<ListView> arg0,
                                            State arg1, Mode arg2) {
                    }

                });
    }

    /**
     * 获取我的群组方法方法
     */
    @Background
    void FetchFrequentlyContacts() {
        LoadMyGroupService service = (LoadMyGroupService) OsgiServiceLoader
                .getInstance().getService(LoadMyGroupService.class);

        List<Group> groupList = service.invoke(null);
        ContactsList.setOnItemClickListener(new itemClickListener(groupList));
        initFetchContacts(groupList);
    }

    /***
     * 显示常用联系人
     *
     * @param groupentityList
     */
    @UiThread
    void initFetchContacts(List<Group> groupentityList) {
        groupadapter = new AddrbookGroupAdapter(getActivity(), groupentityList);
        groupadapter.notifyDataSetChanged();
        if (groupentityList != null && groupentityList.size() > 0) {
            setGroupContactsAddData(false);
            ContactsList.setAdapter(groupadapter);
            if (nodataRl.isShown()) {
                nodataRl.setVisibility(View.GONE);
            }
            if (!ContactsList.isShown()) {
                ContactsList.setVisibility(View.VISIBLE);
            }
        } else {
            if (!nodataRl.isShown()) {
                nodataText.setText(R.string.im_main_tab_no_group_msg_tips);
                nodataImg.setBackgroundResource(R.drawable.no_chat_msg_tips);
                nodataRl.setVisibility(View.VISIBLE);
            }
            if (ContactsList.isShown()) {
                ContactsList.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @return groupContactsAddData
     */
    public boolean isGroupContactsAddData() {
        return groupContactsAddData;
    }

    /**
     * @param groupContactsAddData 要设置的 groupContactsAddData
     */
    public void setGroupContactsAddData(boolean groupContactsAddData) {
        this.groupContactsAddData = groupContactsAddData;
    }

    public void onClickItem(Group group) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("chatId", group.getGroupId());
        bundle.putString("chatName", group.getName());
        bundle.putInt("chatType", 1);
        intent.putExtras(bundle);
        intent.setClass(getActivity(),
                com.aspirecn.corpsocial.bundle.im.ui.ChatActivity_.class);
        AddrbookGroupFragment.this.startActivity(intent);
    }

    // 解散群监听
    @Override
    public void onHandleDismissGroupEvent(NotifyDismissGroupEvent event) {
        FetchFrequentlyContacts();

    }

    // 页卡切换监听
//	@Override
//	public void onHandleUIRefreshContactsRespEvent(
//			UIRefreshAddrbookViewPagerRespEvent event) {
//		if (event.getPageIndex() == 1 && isGroupContactsAddData() == true) {
//			start();
//		}
//	}

    // 被踢监听
    @Override
    public void onHandleKickOutGroupMemberEvent(
            NotifyKickOutGroupMemberEvent event) {
        FetchFrequentlyContacts();
    }

    // 主动解散监听
    @Override
    public void onHandleDismissGroupRespEvent(DismissGroupRespEvent event) {
        FetchFrequentlyContacts();
    }

    // 主动解散群监听
    @Override
    public void onHandleCreateUpdateGroupRespEvent(
            CreateUpdateGroupRespEvent event) {
        FetchFrequentlyContacts();
    }

    //刷新我的群组监听
    @Override
    public void onHandleRefreshMyGroup(NotifyRefreshMyGroupEvent event) {
        FetchFrequentlyContacts();
    }

    /**
     * item点击事件处理
     */
    public class itemClickListener implements OnItemClickListener {
        private List<Group> groupentityList;

        public itemClickListener(List<Group> groupentityList) {
            this.groupentityList = groupentityList;
        }

        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {
            onClickItem(groupentityList.get(position - 1));
        }
    }
}
