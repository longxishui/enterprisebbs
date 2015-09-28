package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.MainTabActivity_;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.ChangeGroupChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.event.ClearChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.DismissGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.InviteGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.event.KickOutGroupMemberRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.LocalGroupChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.event.QuitGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.QuitGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.RefreshImMainTabEvent;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupRespSubject.DismissGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.DismissGroupSubject.DismissGroupEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.InviteGroupMemberRespSubject.InviteGroupMemberRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.InviteGroupMemberSubject.InviteGroupMemberEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberRespSubject;
import com.aspirecn.corpsocial.bundle.im.listener.KickOutGroupMemberSubject.KickOutGroupMemberEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.QuitGroupRespSubject.QuitGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.listener.RefreshImMainTabSubject.RefreshImMainTabEventListener;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupChatSetting;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomAlertDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * 群聊聊天设置
 *
 * @author lihaiqiang
 */
@EActivity(R.layout.im_group_chat_setting_activity)
public class GroupChatSettingActivity extends EventFragmentActivity implements
        CreateUpdateGroupRespEventListener,
        DismissGroupRespEventListener,
        QuitGroupRespEventListener,
        DismissGroupEventListener,
        KickOutGroupMemberEventListener,
        KickOutGroupMemberRespSubject.KickOutGroupMemberRespEventListener,
        RefreshImMainTabEventListener,
        InviteGroupMemberRespEventListener,
        InviteGroupMemberEventListener {

    @ViewById(R.id.im_group_chat_setting_name_content_tv)
    TextView mGroupNameTV;
    @ViewById(R.id.im_group_chat_setting_admin_layout)
    View mGroupAdminLayout;
    @ViewById(R.id.im_group_chat_setting_member_content_tv)
    TextView mGroupMemberCountTV;
    @ViewById(R.id.im_group_chat_setting_new_msg_switch_cb)
    CheckBox mNewMsgNotifySwitchCB;
    @ViewById(R.id.im_group_chat_setting_dismiss_group_layout)
    View mDismissGroupLayout;
    @ViewById(R.id.im_group_chat_setting_quit_group_layout)
    View mQuitGroupLayout;
    private String mChatId;
    private String mChatName;
    private GroupChatSetting mGroupChatSetting;
    private int mThemeColor;
    private int CLEAR = 0;
    private int QUIT = 1;
    private int DISMISS = 2;

    @AfterViews
    void doAfterViews() {
        mChatId = getIntent().getStringExtra("chatId");
        mChatName = getIntent().getStringExtra("mChatName");
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        initActionBar();
        loadGroupChatSetting();
    }

    private void initActionBar() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.gronp_chat_setting_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
    }

    @Background
    void loadGroupChatSetting() {
        LocalGroupChatSettingEvent event = new LocalGroupChatSettingEvent();
        event.setGroupId(mChatId);
        mGroupChatSetting = (GroupChatSetting) UiEventHandleFacade.getInstance().handle(event);

        refreshUI();
    }

    @UiThread
    void refreshUI() {
        mGroupNameTV.setText(mGroupChatSetting.doGetGroupName());
        if (mGroupChatSetting.doGetGroupType() != GroupType.GROUP.getValue()) {
            mGroupAdminLayout.setVisibility(View.GONE);
        } else {
            mGroupAdminLayout.setVisibility(View.VISIBLE);
        }
        mGroupMemberCountTV.setText("(" + mGroupChatSetting.doGetMemberCount() + ")");
        mNewMsgNotifySwitchCB.setChecked(mGroupChatSetting.isNewMsgNotify());
        if (mGroupChatSetting.doGetGroupType() != GroupType.GROUP.getValue()) {
            mDismissGroupLayout.setVisibility(View.GONE);
            mQuitGroupLayout.setVisibility(View.GONE);
        } else if (mGroupChatSetting.isAdmin()) {
            mDismissGroupLayout.setVisibility(View.VISIBLE);
            mQuitGroupLayout.setVisibility(View.GONE);
        } else {
            mDismissGroupLayout.setVisibility(View.GONE);
            mQuitGroupLayout.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.im_group_chat_setting_name_layout)
    void updateGroupName() {
        //只有管理员可修改群名称
        if (mGroupChatSetting.isAdmin()) {
            final CustomAlertDialog dialog = new CustomAlertDialog(this);
            dialog.getAlertMsgTV().setVisibility(View.GONE);
            dialog.getInputMsgET().setVisibility(View.VISIBLE);
            dialog.getInputMsgET().setHint("请输入新的群名称");
            dialog.setBtn1Text("取消");
            dialog.setBtn1ClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setBtn2Text("确定");
            dialog.getBtn2().setTextColor(mThemeColor);
            dialog.setBtn2ClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = dialog.getInputMsgET().getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        showToastMessage("群名称不能为空!");
                        dialog.dismiss();

                    } else if (name.length() > 50) {
                        showToastMessage("群名称字数超出限制!");
                        dialog.dismiss();

                    } else {
                        CreateUpdateGroupEvent busEvent = new CreateUpdateGroupEvent();
                        GroupEntity groupEntity = mGroupChatSetting.getGroupEntity();
                        // 保存起来，服务器反馈成功之后再改
                        mChatName = name;
                        groupEntity.setName(name);
                        busEvent.setGroupEntity(groupEntity);
                        doUpdateGroupName(busEvent);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    @Click(R.id.im_group_chat_setting_picture_layout)
    void
    showGroupPictures() {
        Intent intent = new Intent(this,
                GroupPictureWallActivity_.class);
        Bundle bundle = new Bundle();
        String groupId = mGroupChatSetting.doGetGroupId();
        bundle.putString("chatId", groupId);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Click(R.id.im_group_chat_setting_admin_layout)
    void showGroupAdmins() {
        Intent intent = new Intent(this, GroupAdminActivity_.class);
        intent.putExtra("creatorId", mGroupChatSetting.getGroupEntity().getCreator());
        startActivity(intent);
    }

    @Click(R.id.im_group_chat_setting_member_layout)
    void showGroupMembers() {
        Intent intent = new Intent(this, GroupMemberActivity_.class);
        intent.putExtra("groupId", mGroupChatSetting.doGetGroupId());
        intent.putExtra("groupName", mGroupChatSetting.doGetGroupName());
        intent.putExtra("groupType", mGroupChatSetting.doGetGroupType());
        startActivity(intent);
    }

    @CheckedChange(R.id.im_group_chat_setting_new_msg_switch_cb)
    void newMsgNotifyChange(CompoundButton cb, boolean isChecked) {
        if (mGroupChatSetting != null) {
            mGroupChatSetting.setNewMsgNotify(isChecked);
            doChangeChatSetting(mGroupChatSetting);
        }
    }

    @Click(R.id.im_group_chat_setting_find_chat_msg_layout)
    void findChatMsg() {
        Intent intent = new Intent(this, SearchChatMsgActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatName", mGroupNameTV.getText().toString().trim());
        bundle.putString("chatId", mChatId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Click(R.id.im_group_chat_setting_clear_chat_msg_layout)
    void clearChatMsg() {
        showAlertDialog(CLEAR, "您确定要清空聊天记录吗？");
    }

    @Click(R.id.im_group_chat_setting_dismiss_group_layout)
    void dismissGroup() {
        showAlertDialog(DISMISS, "您确定要解散本群吗？");
    }

    @Click(R.id.im_group_chat_setting_quit_group_layout)
    void quitGroup() {
        showAlertDialog(QUIT, "您确定要退出本群吗？");
    }

    @Background
    void doUpdateGroupName(CreateUpdateGroupEvent busEvent) {
        UiEventHandleFacade.getInstance().handle(busEvent);
    }

    @Background
    void doClearChatMsg() {
        ClearChatMsgEvent clearChatMsgEvent = new ClearChatMsgEvent();
        clearChatMsgEvent.setChatId(mGroupChatSetting.doGetGroupId());
        UiEventHandleFacade.getInstance().handle(clearChatMsgEvent);
        showToastMessage("清空聊天记录成功");
        finish();
    }

    @Background
    void doQuitGroup() {
        QuitGroupEvent quitGroupEvent = new QuitGroupEvent();
        quitGroupEvent.setGroupId(mGroupChatSetting.doGetGroupId());
        UiEventHandleFacade.getInstance().handle(quitGroupEvent);
    }

    @Background
    void doDismissGroup() {
        DismissGroupEvent dismissGroupEvent = new DismissGroupEvent();
        dismissGroupEvent.setGroupId(mGroupChatSetting.doGetGroupId());
        dismissGroupEvent.setGroupName(mGroupChatSetting.doGetGroupName());
        dismissGroupEvent.setOperatorId(Config.getInstance().getUserId());
        dismissGroupEvent.setOperatorName(Config.getInstance().getNickName());
        dismissGroupEvent.setReason("散伙......");
        UiEventHandleFacade.getInstance().handle(dismissGroupEvent);
    }

    @Background
    void doChangeChatSetting(GroupChatSetting groupChatSetting) {
        ChangeGroupChatSettingEvent event = new ChangeGroupChatSettingEvent();
        event.setGroupChatSetting(groupChatSetting);
        UiEventHandleFacade.getInstance().handle(event);
    }

    private void showAlertDialog(final int actionType, String msg) {
        final CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setAlertMsg(msg);
        dialog.getAlertMsgTV().setGravity(Gravity.CENTER);
        dialog.setBtn1Text("取消");
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setBtn2Text("确定");
        dialog.getBtn2().setTextColor(mThemeColor);
        dialog.setBtn2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (actionType == CLEAR) {
                    doClearChatMsg();
                } else if (actionType == QUIT) {
                    doQuitGroup();
                } else if (actionType == DISMISS) {
                    doDismissGroup();
                }
            }
        });
        dialog.show();
    }

    /**
     * 群创建或信息更新回调
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleCreateUpdateGroupRespEvent(CreateUpdateGroupRespEvent event) {
        int errorCode = event.getErrorCode();
        if (errorCode == ErrorCode.SUCCESS.getValue()) {
            mGroupNameTV.setText(event.getGroupName());
            showToastMessage("群名修改成功！");
        } else if (errorCode == ErrorCode.ILLEGAL.getValue()) {
            showToastMessage("群名“" + mChatName + "”已存在，请更换群名再试。");
        } else {
            String errorInfo = event.getErrorInfo();
            if (!TextUtils.isEmpty(errorInfo)) {
                showToastMessage(errorInfo);
            }
        }
    }

    /**
     * 被动解散群回调
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleDismissGroupEvent(DismissGroupEvent event) {
        if (mChatId.equals(event.getGroupId())) {
            Intent intent = new Intent(this, MainTabActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 主动解散群回到
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleDismissGroupRespEvent(DismissGroupRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            showToastMessage("解散群成功!");
            Intent intent = new Intent(this, MainTabActivity_.class);
            startActivity(intent);
        } else if (event.getErrorCode() == ErrorCode.NO_NETWORK.getValue()) {
            showToastMessage("解散群失败，网络连接不可用");
        } else {
            showToastMessage("解散群失败!");
        }
    }

    /**
     * 退出群回调
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleQuitGroupRespEvent(QuitGroupRespEvent event) {
        String errorInfo = event.getErrorInfo();
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            showToastMessage(!TextUtils.isEmpty(errorInfo) ? errorInfo : "退出群成功");
            Intent intent = new Intent(this, MainTabActivity_.class);
            startActivity(intent);
        } else {
            showToastMessage(!TextUtils.isEmpty(errorInfo) ? errorInfo : "退出群失败");
        }
    }

    /**
     * 被踢出群回调
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleKickOutGroupMemberEvent(KickOutGroupMemberEvent event) {
        if (mChatId.equals(event.getGroupId())) {
            Intent intent = new Intent(this,
                    MainTabActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 踢人响应
     *
     * @param event
     */
    @Override
    public void onHandleKickOutGroupMemberRespEvent(KickOutGroupMemberRespEvent event) {
        loadGroupChatSetting();
    }

    /**
     * 自己邀请群成员响应
     *
     * @param event
     */
    @Override
    public void onHandleInviteGroupMemberRespEvent(InviteGroupMemberRespEvent event) {
        loadGroupChatSetting();
    }

    /**
     * 他人邀请群成员响应
     *
     * @param event
     */
    @Override
    public void onHandleInviteGroupMemberEvent(InviteGroupMemberEvent event) {
        loadGroupChatSetting();
    }

    /**
     * 刷新界面
     *
     * @param event
     */
    @Override
    public void onHandleRefreshImMainTabEvent(RefreshImMainTabEvent event) {
        loadGroupChatSetting();
    }

    @UiThread
    void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
