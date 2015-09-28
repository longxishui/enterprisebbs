package com.aspirecn.corpsocial.bundle.im.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddressBookConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.ui.company.CompanyActivty_;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.ChangeP2PChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.event.ClearChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.event.LocalP2PChatSettingEvent;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.P2PChatSetting;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.ui.widget.CustomAlertDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 点对点聊天设置
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.im_p2p_chat_setting_activity)
public class P2PChatSettingActivity extends EventFragmentActivity implements
        CreateUpdateGroupRespEventListener {

    // 个人设置头像
    @ViewById(R.id.im_p2p_chat_head_img_iv)
    ImageView mHeadImgIV;
    // 个人设置名字
    @ViewById(R.id.im_p2p_chat_name_tv)
    TextView mNameTV;
    // 新消息通知开关
    @ViewById(R.id.im_p2p_chat_new_msg_notify_switch_cb)
    CheckBox mNewMsgNotifySwitchCB;
    private ProgressDialog mDialog;
    private Dialog dialog;
    private String mChatId;
    private String mChatName;
    private P2PChatSetting mP2PChatSetting;
    private int mThemeColor;

    @AfterViews
    void doAfterViews() {
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        // 标题修正
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.P2Pchat_setting)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        // 加载数据
        mChatId = getIntent().getStringExtra("chatId");
        mChatName = getIntent().getStringExtra("chatName");

        doLoadP2PChatSetting(mChatId);
    }

    private void doLoadP2PChatSetting(String userId) {
        LocalP2PChatSettingEvent event = new LocalP2PChatSettingEvent();
        event.setUserId(userId);
        mP2PChatSetting = (P2PChatSetting) uiEventHandleFacade.handle(event);
        refreshUI(mP2PChatSetting);
    }

    @UiThread
    void refreshUI(P2PChatSetting p2PChatSetting) {
        User user = p2PChatSetting.getUser();
        if (user != null) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                    .build();
            ImageLoader.getInstance().displayImage(user.getUrl(), mHeadImgIV, options);
        }
        mNameTV.setText(mChatName);
        mNewMsgNotifySwitchCB.setChecked(p2PChatSetting.isNewMsgNotify());
    }

    // 添加更多人至创建群，跳转到联系人界面
    @Click(R.id.p2p_chat_more)
    void addUserToCreateGroup() {
        Toast.makeText(this, "添加群成员", Toast.LENGTH_SHORT).show();
        List<String> p2p = new ArrayList<String>();
        p2p.add(mChatId);
        Intent intent = new Intent(this, CompanyActivty_.class);
        Bundle mBundle = new Bundle();
        AddrbookSelectConfig selectConfig = new AddrbookSelectConfig();
        selectConfig.setSelectType(AddrbookSelectConfig.AddrbookSelectType.SELECT);
        selectConfig.setSelectForMe(true);
        mBundle.putSerializable(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY, selectConfig);
        intent.putExtras(mBundle);
        startActivityForResult(intent, 100);
    }

    // 查找聊天记录
    @Click(R.id.im_p2p_chat_find_chat_msg_tv)
    void findChatMsg() {
        Intent intent = new Intent(this, SearchChatMsgActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId", mChatId);
        bundle.putString("chatName", mChatName);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    // 清空聊天记录
    @Click(R.id.im_p2p_chat_clear_chat_msg_tv)
    void clearChatMsg() {
        final CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setAlertMsg("您确定要清空聊天记录吗?");
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
                doClearChatMsg();
            }
        });
        dialog.show();
    }

    @Background
    void doClearChatMsg() {
        ClearChatMsgEvent clearChatMsgEvent = new ClearChatMsgEvent();
        clearChatMsgEvent.setChatId(mChatId);
        uiEventHandleFacade.handle(clearChatMsgEvent);
        showToast("已清空聊天记录");
    }

    // 获取个人设置
    @CheckedChange(R.id.im_p2p_chat_new_msg_notify_switch_cb)
    void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        mP2PChatSetting.setNewMsgNotify(arg1);
        ChangeChatSetting(mP2PChatSetting);
    }

    @Background
    void ChangeChatSetting(P2PChatSetting p2pChatSetting) {
        ChangeP2PChatSettingEvent event = new ChangeP2PChatSettingEvent();
        event.setP2pChatSetting(p2pChatSetting);
        uiEventHandleFacade.handle(event);
    }

    // 回调添加群成员方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == 100) {
            ArrayList<String> companyIds = data.getStringArrayListExtra(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY);
            List<User> members = (List<User>) OsgiServiceLoader.getInstance().getService(FindContactsByIdsService.class).invoke(companyIds);

            StringBuffer groupNameSB = new StringBuffer();
            groupNameSB.append(Config.getInstance().getNickName());
            groupNameSB.append("/").append(mChatName);

            List<String> memberList = new ArrayList<String>();
            for (User member : members) {
                memberList.add(member.getUserid());
                if (groupNameSB.toString().length() < 50) {
                    groupNameSB.append("/").append(member.getName());
                }
            }

            String creatorId = Config.getInstance().getUserId();
            if (!memberList.contains(creatorId)) {
                memberList.add(creatorId);
            }
            if (!memberList.contains(mChatId)) {
                memberList.add(mChatId);
            }

            List<String> adminList = new ArrayList<String>();
            adminList.add(creatorId);

            CreateUpdateGroupEvent groupEvent = new CreateUpdateGroupEvent();
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setCreator(creatorId);
            groupEntity.setGroupType(GroupType.GROUP.getValue());
            groupEntity.setLimitType("0");
            groupEntity.setName(groupNameSB.toString());
            groupEntity.setDescription(groupNameSB.toString());
            groupEntity.setAdminList(adminList);
            groupEntity.setMemberList(memberList);

            groupEvent.setGroupEntity(groupEntity);

            mDialog = ProgressDialog.show(this, "", "正在创建群...");
            doCreateDialogGroup(groupEvent);
        }
    }

    // 创建群
    @Background
    void doCreateDialogGroup(CreateUpdateGroupEvent createUpdateGroupEvent) {
        uiEventHandleFacade.handle(createUpdateGroupEvent);
    }

    @Override
    public void onHandleCreateUpdateGroupRespEvent(CreateUpdateGroupRespEvent event) {
        mDialog.dismiss();
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            // 跳到下一页
            nextPage(event);
        } else if (event.getErrorCode() == ErrorCode.NO_NETWORK.getValue()) {
            showToast("无网络！");
        } else if (event.getErrorCode() == ErrorCode.TIMEOUT.getValue()) {
            showToast("网络不给力！");
        } else if (event.getErrorCode() == ErrorCode.OTHER_ERROR.getValue()) {
            showToast("网络出异常！");
        } else {
            showToast("请求失败");
        }
    }

    /**
     * 跳转下一页
     */
    private void nextPage(CreateUpdateGroupRespEvent event) {
        ChatEntity chatEntity = event.getChatEntity();
        Intent intent = new Intent(this, ChatActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId", chatEntity.getChatId());
        bundle.putString("chatName", chatEntity.getChatName());
        bundle.putInt("chatType", chatEntity.getChatType());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /**
     * 网络请求失败提示
     *
     * @param errorMessage
     */
    @UiThread
    void showToast(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
