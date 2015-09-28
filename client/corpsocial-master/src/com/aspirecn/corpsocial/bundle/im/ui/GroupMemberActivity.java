package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddressBookConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.company.CompanyActivty_;
import com.aspirecn.corpsocial.bundle.common.ui.MainTabActivity_;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.AcceptGroupInviteEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.LocalGroupChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.listener.AcceptGroupInviteSubject.AcceptGroupInviteEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupRespSubject.DismissGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.InviteGroupMemberRespSubject.InviteGroupMemberRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberRespSubject.KickOutGroupMemberRespEventListener;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
import com.aspirecn.corpsocial.bundle.im.ui.GroupMemberAdapter.OnDeletedItemsChangeListener;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 群管理员设置
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.im_group_admin_setting_activity)
public class GroupMemberActivity extends EventFragmentActivity implements
        OnItemLongClickListener,
        OnItemClickListener,
        OnDeletedItemsChangeListener,
        KickOutGroupMemberRespEventListener,
        DismissGroupRespEventListener,
        InviteGroupMemberRespEventListener,
        AcceptGroupInviteEventListener,
        ActionBarFragment.LifeCycleListener {

    // 绑定成员展示
    @ViewById(R.id.im_member_gv)
    GridView mGridView;
    private String operatorId;
    private String operatorName;
    private ActionBarFragment fab;
    private int INIT = 0;
    private int MORE = 1;
    private String mGroupId;
    private String mGroupName;
    private int mGroupType;
    private boolean mIsAdmin = false;
    private GroupMemberAdapter mAdapter;
    private List<String> mMemberIds = new ArrayList<String>();
    private List<String> mAddMemberIds = new ArrayList<String>();

    @AfterInject
    void doAfterInject() {
        mGroupId = getIntent().getStringExtra("groupId");
        mGroupName = getIntent().getStringExtra("groupName");
        mGroupType = getIntent().getIntExtra("groupType", GroupType.DEPRT.getValue());

        SharedPreferences sharedPre = getSharedPreferences(
                Constant.SHAREDPREFE_NAME_OAWEIXIN,
                Context.MODE_PRIVATE);
        operatorId = sharedPre.getString("user_id", "null");
        operatorName = sharedPre.getString("user_name", "null");
    }

    @AfterViews
    void doAfterViews() {
        initView();
        doLoadChatSetting();
    }

    private void initView() {
        mAdapter = new GroupMemberAdapter(this, mGroupType, mMemberIds);
        mAdapter.setOnDeleteItemsChangeListener(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

        initActionbar();
    }

    private void initActionbar() {
        fab = ActionBarFragment_.builder().
                title("群成员(" + mMemberIds.size() + ")").
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commitAllowingStateLoss();
        fab.setLifeCycleListener(this);
    }

    @Background
    void doLoadChatSetting() {
        LocalGroupChatSettingEvent event = new LocalGroupChatSettingEvent();
        event.setGroupId(mGroupId);
        GroupChatSetting groupChatSetting = (GroupChatSetting) UiEventHandleFacade
                .getInstance().handle(event);
        mIsAdmin = groupChatSetting.isAdmin();
        refreshUI(INIT, groupChatSetting.doGetMemberIds());
    }

    @UiThread
    void refreshUI(int actionType, List<String> memberIds) {
        if (actionType == INIT) {
            mMemberIds.clear();
            mMemberIds.addAll(memberIds);

            mAddMemberIds.addAll(memberIds);
        } else if (actionType == MORE) {
            mMemberIds.addAll(memberIds);
        }
        mAdapter.notifyDataSetChanged();
        initActionbar();
    }

    @Background
    void dismissGroup() {
        DismissGroupEvent dismissGroupEvent = new DismissGroupEvent();
        dismissGroupEvent.setGroupId(mGroupId);
        dismissGroupEvent.setGroupName(mGroupName);
        dismissGroupEvent.setOperatorId(operatorId);
        dismissGroupEvent.setOperatorName(operatorName);
        dismissGroupEvent.setReason("散伙......");
        UiEventHandleFacade.getInstance().handle(dismissGroupEvent);
    }

    // 回调添加群成员方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 清空上次添加的成员

        mAddMemberIds.clear();
        if (requestCode == 100 && resultCode == 100) {
            ArrayList<String> companyIds = data.getStringArrayListExtra(
                    AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY);

            @SuppressWarnings("unchecked")
            List<User> members = (List<User>) OsgiServiceLoader.getInstance()
                    .getService(FindContactsByIdsService.class).invoke(companyIds);
            for (User member : members) {
                String memberId = member.getUserid();
                // 忽略选自己 和 忽略已存在的人
                if (Config.getInstance().getUserId().equals(memberId)) {
                    continue;
                }
                mAddMemberIds.add(memberId);
            }
            if (mAddMemberIds.size() > 0) {
                InviteGroupMemberEvent inviteGroupMemberEvent = new InviteGroupMemberEvent();
                inviteGroupMemberEvent.setUserIdList(mAddMemberIds);
                inviteGroupMemberEvent.setUserDeptName("testDeptName");
                inviteGroupMemberEvent.setUserName("testName");
                inviteGroupMemberEvent.setGroupId(mGroupId);
                inviteGroupMemberEvent.setGroupName(mGroupName);
                doInviteGroupMember(inviteGroupMemberEvent);
            }
        }
    }

    /**
     * 邀请成员加入群
     *
     * @param inviteGroupMemberEvent
     */
    @Background
    void doInviteGroupMember(InviteGroupMemberEvent inviteGroupMemberEvent) {
        UiEventHandleFacade.getInstance().handle(inviteGroupMemberEvent);
    }

    /**
     * 邀请群成员加入群回调方法
     */
    @Override
    public void onHandleInviteGroupMemberRespEvent(InviteGroupMemberRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            doLoadChatSetting();
        } else if (event.getErrorCode() == ErrorCode.NO_NETWORK.getValue()) {
            showToast("网络连接不可用！");
        } else {
            showToast(event.getErrorInfo());
        }
    }

    // 踢出群成员
    @Background
    void doKickOutGroupMember(KickOutGroupMemberEvent kickOutGroupMemberEvent) {
        UiEventHandleFacade.getInstance().handle(kickOutGroupMemberEvent);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // 必须先运行这一步
        if (mAdapter.isEditState()) {
            CheckBox box = ((CheckBox) arg1.findViewById(R.id.im_member_delete_icon_cb));
            box.setChecked(!box.isChecked());
        }
        // 添加群成员
        else if (mMemberIds.size() == position) {
            addGroupMember();
        }
        // 查看成员详情
        else {
            showGroupMemberDetailInfo(mMemberIds.get(position));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if (mGroupType == GroupType.GROUP.getValue() && mIsAdmin) {
            mAdapter.setEditState(!mAdapter.isEditState());
            mAdapter.clearForDeleteMemberIds();
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onForDeleteItemsChange(int counts) {
        if (counts > 0) {
            fab.build().setSecondButtonText("删除(" + counts + ")").apply();
            fab.build().setSecondButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickDeleteBtn();
                }
            }).apply();
        }
        //
        else {
            fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
            fab.build().setSecondButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSearchBtn();
                }
            }).apply();
        }
    }

    private void onClickDeleteBtn() {
        KickOutGroupMemberEvent kickOutGroupMemberEvent = new KickOutGroupMemberEvent();
        List<String> KickOutIds = new ArrayList<String>(mAdapter.getForDeleteMemberIds());
        kickOutGroupMemberEvent.setKickoutUserList(KickOutIds);
        kickOutGroupMemberEvent.setGroupId(mGroupId);
        kickOutGroupMemberEvent.setGroupName(mGroupName);
        kickOutGroupMemberEvent.setOperatorId(operatorId);
        kickOutGroupMemberEvent.setOperatorName(operatorName);

        mAdapter.getForDeleteMemberIds().clear();
        mAdapter.setEditState(false);
        // 解散群
        if (mMemberIds.size() == KickOutIds.size()) {
            dismissGroup();
        }
        //踢人
        else {
            if (KickOutIds.contains(operatorId)) {
                showToast("非全选,不可以删除自己!");
            } else {
                doKickOutGroupMember(kickOutGroupMemberEvent);
                mMemberIds.removeAll(mAdapter.getForDeleteMemberIds());
            }
        }
        mAdapter.notifyDataSetChanged();

        fab.build().setTitle("群成员(" + mMemberIds.size() + ")").apply();
        fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
        fab.build().setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearchBtn();
            }
        }).apply();
    }

    /**
     * 添加群成员方法
     */
    private void addGroupMember() {
        Intent intent = new Intent(this, CompanyActivty_.class);
        Bundle mBundle = new Bundle();
        AddrbookSelectConfig selectConfig = new AddrbookSelectConfig();
        selectConfig.setSelectType(AddrbookSelectConfig.AddrbookSelectType.SELECT);
        selectConfig.setSelectForMe(true);
        selectConfig.setExistingContactIds(mAddMemberIds);
        mBundle.putSerializable(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY, selectConfig);
        intent.putExtras(mBundle);
        startActivityForResult(intent, 100);
    }

    /**
     * 查看群成员详情
     */
    private void showGroupMemberDetailInfo(String contactId) {
        Intent intent = new Intent();

//        UserServiceParam serviceParam = new UserServiceParam();
//        serviceParam.setServie("FindContactDetailService");
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("userid", contactId);
//        serviceParam.setParams(param);
//
//        @SuppressWarnings("unchecked")
//        UserServiceResult<User> result = (UserServiceResult<User>) OsgiServiceLoader.getInstance()
//                .getService(UserService.class).invoke(serviceParam);
//        User user = result.getData();
        User user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(contactId);

        if (user == null) {
            showToast("获取用户信息失败");
            return;
        }

        if (user.getCorpList().size() > 1) {
            intent.setClass(this, AddrbookGroupChoose_.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("user", user);
            intent.putExtras(mBundle);
        } else {
            intent.putExtra("ContactId", user.getUserid());
            intent.putExtra("corpId", user.getCorpId());
            intent.setClass(this, AddrbookPersonalParticularsActivity_.class);
        }

        startActivity(intent);
    }

    /**
     * 踢人反馈回调方法
     */
    @Override
    public void onHandleKickOutGroupMemberRespEvent(KickOutGroupMemberRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            doLoadChatSetting();
            showToast("踢人成功!");
        } else {
            showToast("踢人失败!");
        }
    }

    /**
     * 解散群反馈回调方法
     */
    @Override
    public void onHandleDismissGroupRespEvent(DismissGroupRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            showToast("解散群成功!");
            Intent intent = new Intent(this, MainTabActivity_.class);
            startActivity(intent);
        } else if (event.getErrorCode() == ErrorCode.NO_NETWORK.getValue()) {
            showToast("网络连接不可用");
        } else {
            showToast("解散群失败!");
        }
    }

    @Override
    public void onHandleAcceptGroupInviteEvent(AcceptGroupInviteEvent event) {
        if (event.getAccept()) {
            doLoadChatSetting();
        }
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
        fab.build().setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearchBtn();
            }
        }).apply();
    }

    /**
     * 搜索按钮方法
     */
    void onClickSearchBtn() {
        Intent intent = new Intent(this, SearchGroupMemberActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("groupId", mGroupId);
        bundle.putInt("groupType", mGroupType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}