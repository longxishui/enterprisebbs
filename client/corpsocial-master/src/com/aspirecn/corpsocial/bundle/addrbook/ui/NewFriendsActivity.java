package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.FriendInvite;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteDeleteEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.InviteReadEvent;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindInviteParam;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindInviteService;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptAddFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.InviteFriendSubject;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomAlertDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 新的朋友
 * Created by lihaiqiang on 2015/6/18.
 */
@EActivity(R.layout.addrbook_new_friends_activity)
public class NewFriendsActivity extends EventFragmentActivity implements
        AcceptAddFriendRespSubject.AcceptAddFriendRespEventListener,
        InviteFriendSubject.InviteFriendEventListener {

    @ViewById(R.id.addrbook_new_friends_layout)
    View mNewFriendLayout;
    @ViewById(R.id.addrbook_new_friends_lv)
    ListView mListView;
    private NewFriendsAdapter mAdapter;
    private List<FriendInvite> mFriendInvites = new ArrayList<FriendInvite>();
    private int mThemeColor;

    @AfterViews
    void doAfterViews() {
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        ActionBarFragment fab = ActionBarFragment_.builder().
                title("新的朋友").
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        initView();

        loadNewsFriends();
    }

    private void initView() {
        mAdapter = new NewFriendsAdapter(this, mFriendInvites);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("info", "onItemClick");
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialogOnItemLongClick(mFriendInvites.get(i));
                return true;
            }
        });
    }

    @Background
    @SuppressWarnings("unchecked")
    void loadNewsFriends() {
        FindInviteParam param = new FindInviteParam();
        param.setStart(0);
        param.setCount(0);
        List<FriendInvite> invites = (List<FriendInvite>) OsgiServiceLoader.getInstance().getService(FindInviteService.class).invoke(param);
        if (invites.size() > 0) {
            updateData(invites);
        }
    }

    @UiThread
    void updateData(List<FriendInvite> friendInvites) {
        mFriendInvites.clear();
        mFriendInvites.addAll(friendInvites);
        refreshUIList();
    }

    @UiThread
    void refreshUIList() {
        mAdapter.notifyDataSetChanged();
        mNewFriendLayout.setVisibility(mFriendInvites.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Click(R.id.addrbook_input_phone_number_layout)
    void onClickInputPhoneNumberLayout() {
        Intent intent = new Intent(this, FindContactActivity_.class);
        startActivity(intent);
    }

    /**
     * 处理接受添加好友申请响应
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleAcceptAddFriendRespEvent(AcceptAddFriendRespEvent event) {
        CustomDialog.closeProgress(this);
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            showToast("已添加好友");
            //重新加载列表数据
            loadNewsFriends();
        } else {
            showToast("添加好友失败");
        }
    }

    /**
     * 处理接受到添加好友申请事件
     *
     * @param event
     */
    @Override
    public void onHandleInviteFriendEvent(InviteFriendEvent event) {
        //重新加载列表数据
        loadNewsFriends();
    }

    /**
     * 长按显示
     *
     * @param friendInvite
     */
    private void showDialogOnItemLongClick(final FriendInvite friendInvite) {
        final CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setAlertMsg("删除【" + friendInvite.getUsername() + "】的好友邀请消息？");
        dialog.setBtn1Text("取消");
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setBtn2Text("删除");
        dialog.getBtn2().setTextColor(mThemeColor);
        dialog.setBtn2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                deleteInviteRecord(friendInvite);
                mFriendInvites.remove(friendInvite);
                refreshUIList();
            }
        });
        dialog.show();
    }

    /**
     * 删除好友邀请记录
     *
     * @param friendInvite
     */
    @Background
    void deleteInviteRecord(FriendInvite friendInvite) {
        List<Integer> unReadSeqNoes = new ArrayList<Integer>();
        unReadSeqNoes.add(friendInvite.getSeqNo());
        InviteDeleteEvent event = new InviteDeleteEvent();
        event.setSeqNoes(unReadSeqNoes);
        UiEventHandleFacade.getInstance().handle(event);
    }

    @UiThread
    void showToast(CharSequence charSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空未读
        clearUnRead();
    }

    @Background
    void clearUnRead() {
        List<Integer> unReadSeqNoes = new ArrayList<Integer>();
        for (FriendInvite friendInvite : mFriendInvites) {
            if (friendInvite.getIsNew() == 1) {
                unReadSeqNoes.add(friendInvite.getSeqNo());
            }
        }
        if (unReadSeqNoes.size() > 0) {
            InviteReadEvent event = new InviteReadEvent();
            event.setSeqs(unReadSeqNoes);
            UiEventHandleFacade.getInstance().handle(event);
        }
        //从新统计未读
        UiEventHandleFacade.getInstance().handle(new CalculateAddressBookUnReadMsgCountEvent());
    }

}
