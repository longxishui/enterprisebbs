package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.domain.UserDept;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFrequentlyContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactDetailEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFrequentlyContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.SetFrequentlyContactLastOpEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.QueryResult;
import com.aspirecn.corpsocial.bundle.addrbook.facade.Contact;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFrequentlyContactsRespSubject.AddFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFrequentlyContactsRespSubject.RemoveFrequentlyContactRespEventListener;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FrequentlyContactType;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspirecn.corpsocial.common.util.ContactInformationUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shamanland.fonticon.FontIconView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;


/**
 * 常用联系人个人详情页
 *
 * @author chenziqiang
 */
@EActivity(R.layout.addrbook_personal_particulars)
public class AddrbookPersonalParticularsActivity extends EventFragmentActivity
        implements AddFrequentlyContactRespEventListener,
        RemoveFrequentlyContactRespEventListener,
        RemoveFriendRespSubject.RemoveFriendRespEventListener {
    /* 控件初始化 */
    // 名字
    @ViewById(R.id.addrbook_contact_name)
    TextView name;
    // 职位
    @ViewById(R.id.addrbook_contact_position_view)
    RelativeLayout positionView;
    @ViewById(R.id.addrbook_contact_position)
    TextView position;
    // 电话
    @ViewById(R.id.addrbook_personal_phone)
    TextView phoneNumber;
    @ViewById(R.id.addrbook_personal_phone_view)
    RelativeLayout phoneNumberView;
    // 部门
    @ViewById(R.id.addrbook_personal_department)
    TextView deptName;
    @ViewById(R.id.addrbook_personal_department_view)
    RelativeLayout deptNameView;
    // 工作群
    @ViewById(R.id.addrbook_personal_group)
    TextView groupName;
    @ViewById(R.id.addrbook_personal_group_view)
    RelativeLayout groupView;
    // Email
    @ViewById(R.id.addrbook_personal_email)
    TextView emailAddress;
    // Email LinearLayout
    @ViewById(R.id.addrbook_personal_email_ll)
    RelativeLayout emailView;
    // 所属子部门
    @ViewById(R.id.addrbook_deptname_ll)
    LinearLayout deptView;
    // 加为常用
    @ViewById(R.id.addrbook_personal_add_remove)
    TextView frequenContactView;
    @ViewById(R.id.addrbook_personal_signature)
    TextView signature;
    @ViewById(R.id.addrbook_contact_information)
    LinearLayout information;
    // 头像
    @ViewById(R.id.addrbook_personal_headimg)
    ImageView headimg;
    // 打电话按钮
    @ViewById(R.id.personal_phone)
    RelativeLayout phoneView;
    // 发短信按钮
    @ViewById(R.id.personal_sms)
    RelativeLayout smsView;
    //短号
    @ViewById(R.id.addrbook_personal_short_view)
    RelativeLayout shortView;
    @ViewById(R.id.addrbook_personal_shortphone)
    TextView shortphone;
    //打电话图标
    @ViewById(R.id.personal_phone_imgview)
    FontIconView mPhoneView;

    //发短信图标
    @ViewById(R.id.personal_sms_imgview)
    FontIconView mSmsView;
    //发消息图标
    @ViewById(R.id.personal_message_imgview)
    FontIconView mMessageView;
    //发邮件图标
    @ViewById(R.id.personal_sendeamil_img)
    FontIconView mSendMailView;
    //发任务图标
    @ViewById(R.id.addrbook_task_img)
    FontIconView mTaskView;
    //移除常用图标
    @ViewById(R.id.addrbook_frequencontact_img_del)
    FontIconView mRemoveViewDel;
    //增加常用图标
    @ViewById(R.id.addrbook_frequencontact_img_add)
    FontIconView mRemoveViewAdd;
    @ViewById(R.id.personal_common)
    RelativeLayout frequen;
    private User user;
    private String phoneNo;
    private String id;
    private AddrbookPersonalParticularsActivity context;
    private Toast mToast;
    private IsProduct mIsProduct;
    /**
     * 控件修正
     */
    @AfterViews
    void initview() {
        mIsProduct = AssetsUtils.getInstance().read(AspirecnCorpSocial.getContext(), "addressbook_personal.json", IsProduct.class);
        AppConfig appConfig = AppConfig.getInstance();
        mPhoneView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mSmsView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mMessageView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mSendMailView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mTaskView.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mRemoveViewDel.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));
        mRemoveViewAdd.setTextColor(ColorUtil.convert(appConfig.topViewDef.backgroundColor));

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.addrbook_personal)).
                transparent(true).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        context = this;
        Intent getid = getIntent();
        String ID = getid.getStringExtra("ContactId");
        String corpId = getid.getStringExtra("corpId");

        if (!StringUtils.isBlank(ID)) {
            GetContactDetailEvent getevent = new GetContactDetailEvent();
            this.doLoadChat(getevent, ID, corpId);
        }
        if (mIsProduct.isPro)
            frequen.setVisibility(View.GONE);
    }

    // 获取数据
    void doLoadChat(GetContactDetailEvent getevent, String ID, String corpId) {
        getevent.setContactId(ID);
        getevent.setCorpId(corpId);
        FindUserEvent findUserEvent = new FindUserEvent(getevent);
        QueryResult result = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
        user = (User) result.getResult();

        ShowData(ID, user);

    }

    /**
     * 界面数据设置
     */
    void ShowData(String userId, User user) {
        if (user == null) {
            name.setText(userId);
            return;
        }
        //是否陌生人
        boolean isStranger = !user.canCommunicate();
        //陌生人显示
        String hideInfo = "********";

        // 姓名设置
        if (!StringUtils.isBlank(user.getName())) {
            name.setText(user.getName());
        }
        // 部门设置
        if (!StringUtils.isBlank(user.getDuty())) {
            positionView.setVisibility(View.VISIBLE);
            position.setText(isStranger ? hideInfo : user.getDuty());
        }
        // 电话设置
        if (!StringUtils.isBlank(user.getCellphone())) {
            phoneNumber.setText(isStranger ? hideInfo : user.getCellphone());
        } else {
            phoneNumberView.setVisibility(View.GONE);
            phoneView.setEnabled(false);
            smsView.setEnabled(false);
        }
        //短号设置
        if (!StringUtils.isBlank(user.getInnerPhone())) {
            shortView.setVisibility(View.VISIBLE);
            shortphone.setText(isStranger ? hideInfo : user.getInnerPhone());
        }
        if (!StringUtils.isBlank(user.getDeptName())) {
            deptName.setText(isStranger ? hideInfo : user.getDeptName());
        } else {
            deptNameView.setVisibility(View.GONE);
        }
        // 头像设置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.addrbook_contact_big)
                .build();
        ImageLoader.getInstance().displayImage(user.getUrl(), headimg, options);
        // 常用联系人设置
        if (mIsProduct.isPro) {
            if (user.getIsFriend() == 1)
                addFrequenContactGUI();
            else
                removeFrequenContactGUI();
        }else {
            if (user.getIsFreq() == 1)
                addFrequenContactGUI();
            else
                removeFrequenContactGUI();
        }


        // 兼职部门
        List<UserDept> depts = user.getDepts();
        for (UserDept dept : depts) {
            if (!StringUtils.isBlank(dept.getDuty())) {
                LinearLayout depart = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.addrbook_deptname_item, null);
                TextView departName = (TextView) depart.findViewById(R.id.addrbook_depart_name);
                departName.setText(isStranger ? hideInfo : dept.getName() + "(" + dept.getDuty() + ")");
                deptView.addView(depart);
            }
        }
        // E-mail设置
        if (!StringUtils.isBlank(user.getEmail())) {
            emailAddress.setText(isStranger ? hideInfo : user.getEmail());
        } else {
            emailView.setVisibility(View.GONE);
        }
        // 个性签名
        if (!StringUtils.isBlank(user.getSignature())) {
            signature.setText(isStranger ? hideInfo : user.getSignature());
        } else {
            signature.setText(isStranger ? hideInfo : getString(R.string.addrbook_signature_null));
        }
        // 手机号
        phoneNo = user.getCellphone().toString();
        id = user.getUserid();
        // 是否是自己或陌生人
        if (Config.getInstance().getUserId().equals(id) || isStranger) {
            information.setVisibility(View.GONE);
        } else {
            information.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打电话
     */@Click(R.id.personal_phone)
    void phone() {
        if (!StringUtils.isBlank(id)) {
            if (user.getIsFriend() == 1) {
                recordCommunicationState(1, id);
            }
            if (!StringUtils.isBlank(user.getInnerPhone()))
                AddrbookUtil.phone(AddrbookPersonalParticularsActivity.this, user.getInnerPhone());
            else
                AddrbookUtil.phone(AddrbookPersonalParticularsActivity.this, phoneNo);
        }
    }

    /**
     * 发短信
     */@Click(R.id.personal_sms)
    void sms() {
        if (!StringUtils.isBlank(id)) {
            if (user.getIsFriend() == 1) {
                recordCommunicationState(3, id);
            }
            AddrbookUtil.sms(AddrbookPersonalParticularsActivity.this, phoneNo);
        }
    }

    /**
     * 发微信
     */ @Click(R.id.personal_wxsms)
    void wxsms() {
        if (!StringUtils.isBlank(id)) {
            if (user.getIsFriend() == 1) {
                recordCommunicationState(2, id);
            }
            AddrbookUtil.wxsms(AddrbookPersonalParticularsActivity.this, id, user.getName());
        }
    }

    // 加为常用联系人已经取消按钮点击事件
     @Click(R.id.personal_common)
    void frequenContactLisntener() {
        if (!StringUtils.isBlank(id)) {
            if (user.getIsFreq() == 1)
                removefrequenContact();
            else
                addFrequenContact();
            frequen.setEnabled(false);
            toast("正在处理中……");
        }
    }

    @UiThread
    void toast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(AddrbookPersonalParticularsActivity.this, str,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 加为常用
     */
    @Background
    void addFrequenContact() {
        AddFrequentlyContactEvent addcontact = new AddFrequentlyContactEvent();
        addcontact.setContactId(id);
        uiEventHandleFacade.handle(addcontact);
    }

    // 加为常用联系人UI
    @UiThread
    void addFrequenContactGUI() {
        frequen.setEnabled(true);
        user.setIsFreq(1);
        frequenContactView.setText(R.string.addrbook_remove_personal);
        mRemoveViewAdd.setVisibility(View.GONE);
        mRemoveViewDel.setVisibility(View.VISIBLE);
    }

    /**
     * 删除好友
     */
    @Background
    void removefrequenContact() {
        if(!mIsProduct.isPro) {
            RemoveFrequentlyContactEvent RemoveContact = new RemoveFrequentlyContactEvent();
            RemoveContact.setContactId(id);
            uiEventHandleFacade.handle(RemoveContact);
        }else {
            // TODO 删除好友测试
            showAlertDialog();
            RemoveFriendEvent event = new RemoveFriendEvent();
            event.setUserid(id);
            UiEventHandleFacade.getInstance().handle(event);
        }
    }

    @UiThread
    void showAlertDialog() {
        CustomDialog.showProgeress(this);
    }

    // TODO
    @Override
    @UiThread
    public void onHandleRemoveFriendRespEvent(RemoveFriendRespEvent event) {
        CustomDialog.closeProgress(this);
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            Toast.makeText(this, "删除好友成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "删除好友失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 取消常用联系人UI
     */
  @UiThread
    void removeFrequenContactGUI() {
        frequen.setEnabled(true);
        user.setIsFreq(0);
        frequenContactView.setText(R.string.addrbook_add_personal);
        mRemoveViewAdd.setVisibility(View.VISIBLE);
        mRemoveViewDel.setVisibility(View.GONE);
    }

    @Click(R.id.actionbar_back_btn)
    void reback() {
        finish();
    }

    /**
     * 记录最后一次通讯状态
     *
     * @param State 1是打电话，2是微信，3是短信
     */
    @Background
    void recordCommunicationState(int State, String id) {
        SetFrequentlyContactLastOpEvent event = new SetFrequentlyContactLastOpEvent();
        event.setContactId(id);
        if (State == 1)
            event.setType(FrequentlyContactType.PHONE);
        if (State == 2)
            event.setType(FrequentlyContactType.WEIXIN);
        if (State == 3)
            event.setType(FrequentlyContactType.SMS);
        uiEventHandleFacade.handle(event);
    }

    /**
     * 添加常用联系人事件
     */
    @Override
    public void onHandleAddFrequentlyContactRespEvent(
            AddFrequentlyContactRespEvent event) {
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            //刷新常用联系人列表
            addNetFrequen(event.getContactId());
        } else {
            frequen.setEnabled(true);
            addNetFrequenFail(event.getContactId());
        }
    }

    /* 添加常用联系人成功事件 */
    @Background
    void addNetFrequen(String id) {
        if (id.equals(this.id)) {
            toast("添加" + user.getName() + "为常用联系人成功");
            addFrequenContactGUI();
        }
    }

    /* 添加常用联系人失败事件 */
    @Background
    void addNetFrequenFail(String contactId) {
        if (contactId.equals(id)) {
            toast("添加" + user.getName() + "为常用联系人失败,请检查网络");
        } else {
            GetContactDetailEvent contactEvent = new GetContactDetailEvent();
            contactEvent.setContactId(contactId);
            ContactVO previousContact = (ContactVO) uiEventHandleFacade
                    .handle(contactEvent);
            toast("添加" + previousContact.getName() + "为常用联系人失败,请检查网络");
        }
    }

    /**
     * 移除常用联系人事件
     */
    @Override
    public void onHandleRemoveFrequentlyContactRespEvent(
            RemoveFrequentlyContactRespEvent event) {
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            removerNetFrequently(event.getContactId());

        } else {
            removerNetFrequentlyFail(event.getContactId());
        }
    }

    /* 移除成功事件 */
    @Background
    void removerNetFrequently(String id) {
        if (id.equals(this.id)) {
            removeFrequenContactGUI();
            toast("移除" + user.getName() + "成功");
        }
    }

    /* 移除失败 */
    void removerNetFrequentlyFail(String contactId) {
        frequen.setEnabled(true);
        if (contactId.equals(id)) {
            toast("移除" + user.getName() + "失败,请检查网络");
        } else {
            GetContactDetailEvent contactEvent = new GetContactDetailEvent();
            contactEvent.setContactId(contactId);
            ContactVO previousContact = (ContactVO) uiEventHandleFacade
                    .handle(contactEvent);
            toast("移除" + previousContact.getName() + "失败,请检查网络");
        }
    }

    /* 头像点击事件 */
    @Click(R.id.addrbook_personal_headimg)
    void showPicture() {
        if (!StringUtils.isBlank(user.getUrl())) {
            Intent intent = new Intent(context,
                    AddrBookShowPictureActivity_.class);
            intent.putExtra("headImgUrl", user.getUrl());
            startActivity(intent);
        }
    }

    /**
     * 任务分配接口
     */
    @Click(R.id.addrbook_task)
    void toTask() {
//        Intent intent = new Intent(this, com.aspirecn.corpsocial.bundle.task.ui.TaskCreateActivity_.class);
//        List<Contact> mCantact = new ArrayList<Contact>();
//        Contact contact1 = new Contact();
//        contact1.setId(user.getUserid());
//        contact1.setName(user.getName());
//        mCantact.add(contact1);
//
//        Bundle mBundle = new Bundle();
//        mBundle.putSerializable("ReData", (Serializable) mCantact);
//        intent.putExtras(mBundle);
//        startActivity(intent);
    }

    /**
     * 发送电子邮件
     */
    @Click(R.id.personal_sendeamil)
    void sendEmail() {
        if (!StringUtils.isBlank(user.getEmail()))
            ContactInformationUtil.sendEmail(AddrbookPersonalParticularsActivity.this, user.getEmail());
        else
            ContactInformationUtil.sendEmail(AddrbookPersonalParticularsActivity.this, "");
    }


    protected class IsProduct {
        boolean isPro;
    }
}
