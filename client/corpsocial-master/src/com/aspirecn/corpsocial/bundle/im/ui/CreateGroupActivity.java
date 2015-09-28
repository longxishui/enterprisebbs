package com.aspirecn.corpsocial.bundle.im.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddressBookConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.ui.company.CompanyActivty_;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupEvent;
import com.aspirecn.corpsocial.bundle.im.event.CreateUpdateGroupRespEvent;
import com.aspirecn.corpsocial.bundle.im.listener.CreateUpdateGroupRespSubject.CreateUpdateGroupRespEventListener;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupEntity;
import com.aspirecn.corpsocial.bundle.im.utils.GroupType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;
import com.shamanland.fonticon.FontIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.im_create_group_activity)
public class CreateGroupActivity extends EventFragmentActivity implements
        CreateUpdateGroupRespEventListener, ActionBarFragment.LifeCycleListener {

    /**
     * 群组名称
     */
    @ViewById(R.id.im_create_group_name)
    EditText mGroupNameText;
    /**
     * 群备注
     */
    @ViewById(R.id.im_create_group_comment)
    EditText mGroupCommentText;
    /**
     * 群成员
     */
    @ViewById(R.id.im_create_group_member)
    EditText mGroupMemberText;
    //位置按钮
    @ViewById(R.id.im_create_group_add_member_btn)
    FontIconView mCreateGroupAddMemberFIV;
    private ProgressDialog mDialog;
    private List<String> mMemberNameIds;
    private Bundle bundle = new Bundle();
    private List<String> companyIds;
    /**
     * 群名称
     */
    private String mGroupName;

    @AfterViews
    void doAfterViews() {

        AppConfig appConfig = AppConfig.getInstance();
        mCreateGroupAddMemberFIV.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.im_create_group_title_txt)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
        companyIds = new ArrayList<String>();
    }


    /**
     * 群名称输入框文本内容变化监听方法
     *
     * @param text
     */
    @TextChange(R.id.im_create_group_name)
    void onTextChanged(CharSequence text) {
        if (text.length() > 50) {
            Toast.makeText(this, "群名称已达到限制长度", Toast.LENGTH_LONG).show();
            mGroupNameText.setText(mGroupName);
            mGroupNameText.setSelection(mGroupName.length());
        } else {
            mGroupName = text.toString().trim();
        }
    }

    /**
     * 点击确定按钮方法
     */
//    @Click(R.id.im_create_group_create_bt)
    void onClickCreateBt() {
        // 群名称
        if (TextUtils.isEmpty(mGroupName)) {
            Toast.makeText(this, "请输入群名称", Toast.LENGTH_SHORT).show();
            return;
        }

        // 群备注
        String groupComment = mGroupCommentText.getText().toString().trim();
        // if (TextUtils.isEmpty(groupComment)) {
        // Toast.makeText(this, "请输入群备注", Toast.LENGTH_SHORT).show();
        // return;
        // }

        // 群成员
        if (mMemberNameIds == null || mMemberNameIds.size() < 1) {
            Toast.makeText(this, "请添加成员", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMemberNameIds.size() > 200) {
            Toast.makeText(this, "群成员不能超过200人", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> adminList = new ArrayList<String>();
        String userId = Config.getInstance().getUserId();
        if (!mMemberNameIds.contains(userId)) {
            mMemberNameIds.add(userId);
        }
        adminList.add(userId);

        // 封装参数并执行请求
        CreateUpdateGroupEvent groupEvent = new CreateUpdateGroupEvent();
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setCreator(userId);
        groupEntity.setGroupType(GroupType.GROUP.getValue());
        groupEntity.setLimitType("0");
        groupEntity.setName(mGroupName);
        groupEntity.setDescription(groupComment);
        groupEntity.setAdminList(adminList);
        groupEntity.setMemberList(mMemberNameIds);

        groupEvent.setGroupEntity(groupEntity);

        mDialog = ProgressDialog.show(this, "", "正在创建群...");
        doCreateDialogGroup(groupEvent);
    }

    /**
     * 点击添加群成员方法
     */
    @Click(R.id.im_create_group_add_member_btn)
    void onClickAddMemberBtn() {
//		AddrbookSelectEntrance.CreatNewGroup(CreateGroupActivity.this, true,
//				bundle);
        Intent intent = new Intent(this, CompanyActivty_.class);
        AddrbookSelectConfig selectConfig = new AddrbookSelectConfig();
        selectConfig.setSelectType(AddrbookSelectConfig.AddrbookSelectType.SELECT);
        selectConfig.setSelectForMe(true);
        selectConfig.setContactIds(companyIds);
        bundle.putSerializable(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY, selectConfig);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {

            companyIds = data.getStringArrayListExtra(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY);
            List<User> members = (List<User>) OsgiServiceLoader.getInstance().getService(FindContactsByIdsService.class).invoke(companyIds);

            mMemberNameIds = new ArrayList<String>();
            StringBuilder builder = new StringBuilder();
            for (User member : members) {
                mMemberNameIds.add(member.getUserid());
                builder.append(member.getName() + "/");
            }
            CharSequence subSequence = builder.toString().subSequence(0,
                    builder.toString().length() - 1);
            showMembersName(subSequence);
        }
    }

    @UiThread
    void showMembersName(CharSequence memberNames) {
        mGroupMemberText.setText(memberNames);
    }

    // 创建群
    @Background
    void doCreateDialogGroup(CreateUpdateGroupEvent createUpdateGroupEvent) {
        uiEventHandleFacade.handle(createUpdateGroupEvent);
    }

    @Override
    public void onHandleCreateUpdateGroupRespEvent(
            CreateUpdateGroupRespEvent event) {
        mDialog.dismiss();
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            nextPage(event);
        } else if (event.getErrorCode() == ErrorCode.NO_NETWORK.getValue()) {
            showErrorMessage("当前网络不可用，请检查网络后再试！");
        } else if (event.getErrorCode() == ErrorCode.TIMEOUT.getValue()) {
            showErrorMessage("当前网络不给力，请稍后再试！");
        } else if (event.getErrorCode() == ErrorCode.OTHER_ERROR.getValue()) {
            showErrorMessage("网络出异常，请稍后再试！");
        } else if (event.getErrorCode() == ErrorCode.OVER_GROUP_LIMIT
                .getValue()) {
            showErrorMessage("对不起，建群数已达到上限，无法创建！");
        } else if (event.getErrorCode() == ErrorCode.OVER_GROUP_NUMBER_LIMIT
                .getValue()) {
            showErrorMessage("对不起，添加群成员数超过上限，无法建群！");
        } else if (event.getErrorCode() == ErrorCode.ILLEGAL.getValue()) {
            showErrorMessage("群“" + mGroupName + "”已有人创建，请修改群名！");
        } else {
            showErrorMessage("创建失败，请检查网络或稍后再试！");
        }
    }

    /**
     * 跳转下一页
     */
    private void nextPage(CreateUpdateGroupRespEvent event) {
        ChatEntity chatEntity = event.getChatEntity();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putString("chatId", chatEntity.getChatId());
        bundle.putString("chatName", chatEntity.getChatName());
        bundle.putInt("chatType", chatEntity.getChatType());
        intent.putExtras(bundle);
        intent.setClass(this, ChatActivity_.class);
        startActivity(intent);
        finish();
    }

    /**
     * 网络请求失败提示
     *
     * @param errorMessage
     */
    @UiThread
    void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonText(R.string.im_create_group_btn_txt).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreateBt();
            }
        }).apply();
    }
}
