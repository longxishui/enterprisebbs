package com.aspirecn.corpsocial.bundle.settings.ui;

import android.app.Dialog;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshAddrBookRespSubject;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.bundle.settings.event.ModifySignatureEvent;
import com.aspirecn.corpsocial.bundle.settings.event.ModifySignatureRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyHeadImgRespSubject;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifySignatureRespSubject.ModifySignatureRespEventListener;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;
//import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactVO;

/**
 * 工作台设置_个人信息
 *
 * @author wangdeng
 */

@EActivity(R.layout.setting_ui_personalinfo_activity)
public class WorkbenchSettingPersonalInfoActivity extends EventFragmentActivity
        implements ModifySignatureRespEventListener, ModifyHeadImgRespSubject.ModifyHeadPortraitRespEventListener, RefreshAddrBookRespSubject.RefreshAddrbookRespEventListener {
    OsgiServiceLoader osgiServiceLoader = OsgiServiceLoader.getInstance();
    // 头像
    @ViewById(R.id.settings_ui_workbench_personalinfo_head_iv)
    ImageView head;
    // 名字
    @ViewById(R.id.settings_ui_workbench_personalinfo_name_tv)
    TextView name;
    // 短号
    @ViewById(R.id.settings_ui_workbench_personalinfo_number_tv)
    TextView number;
    // 电话
    @ViewById(R.id.settings_ui_workbench_personalinfo_phone_tv)
    TextView phone;
    // 部门
    @ViewById(R.id.settings_ui_workbench_personalinfo_department_tv)
    TextView department;
    // 个性签名
    @ViewById(R.id.settings_ui_workbench_personalinfo_signature_tv)
    TextView signature;
    //private ContactVO contactVO;
    //private Contact contact;
    private User user;
    private Dialog dialog;

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_personalinfo_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        initial();
    }

    @Background
    void initial() {
//        GetContactDetailEvent getContactDetailEvent = new GetContactDetailEvent();
//        getContactDetailEvent.setContactId(Config.getInstance().getUserId());
//        contactVO = (ContactVO) uiEventHandleFacade.handle(getContactDetailEvent);
//        if (!StringUtils.isBlank(contactVO.getId())) {
//            ShowInfo();
//        }
//        UserService userService = (UserService)osgiServiceLoader.getService(UserService.class);
//        UserServiceParam param=new UserServiceParam();
//        param.setServie("FindContactDetailService");
//        Map map=new HashMap();
//        map.put("userid", Config.getInstance().getUserId());
//        param.setParams(map);
//        UserServiceResult result= userService.invoke(param);
//        user=(User)result.getData();
        user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(Config.getInstance().getUserId());
        ShowInfo();
    }


    // 显示个人信息
    @UiThread
    void ShowInfo() {
//        if (contactVO != null) {
//            name.setText(contactVO.getName());
//            phone.setText(contactVO.getMobilePhone());
//            number.setText(contactVO.getInnerPhoneNumber());
//            department.setText(contactVO.getDeptName());
//            signature.setText(contactVO.getSignature());
//
//            setHeadImg(head);
//        }
        if (user != null) {
            name.setText(user.getName());
            phone.setText(user.getCellphone());
            number.setText(user.getInnerPhone());
            department.setText(user.getDeptName());
            signature.setText(user.getSignature());

            setHeadImg(head);
        }
    }

    /**
     * 显示头像：本地路径有头像则显示本地路径头像，否则显示服务器路径头像
     *
     * @param iv
     */
    private void setHeadImg(ImageView iv) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                .build();
        String localPath = Config.getInstance().getHeadImageUrl();
        ImageLoader.getInstance().displayImage(localPath, iv, options);
    }


    /**
     * 处理修改头像响应事件
     */
    @UiThread
    @Override
    public void onHandleModifyHeadPortraitRespEvent(ModifyHeadImgRespEvent event) {
        setHeadImg(head);
    }

    /**
     * 点击头像item
     */
    @Click(R.id.settings_ui_workbench_personalinfo_head_rl)
    void onClickHeadPortraitBtn() {
        Intent intent = new Intent(this, UploadHeadPortraitActivity_.class);
        startActivity(intent);
    }

    /**
     * @param
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == -1) {
            //      SystemClock.sleep(1500);
//            Intent intent = new Intent(WorkbenchSettingPersonalInfoActivity.this, WorkbenchSettingPersonalInfoActivity_.class);
//            startActivity(intent);
//            finish();
            String signatureData = data.getStringExtra("signature");
            ModifySignatureEvent modifySignatureEvent = new ModifySignatureEvent();
            modifySignatureEvent.setSignature(signatureData);
            doModifySignature(modifySignatureEvent);
            signature.setText(signatureData);
        }
    }

    /**
     */
    @Click(R.id.settings_ui_workbench_personalinfo_signature_rl)
    void onClickGroupName() {
        Intent intent = new Intent(this, WorkbenchSettingSignatureActivity_.class);
        String str = signature.getText().toString().trim();
        intent.putExtra("signature", str);
        startActivityForResult(intent, 0);
    }


//        final EditText editText;
//        final TextView textView;
//
//        dialog = new Dialog(this, R.style.MyDialog);
//        // 设置它的ContentView
//        dialog.setContentView(R.layout.im_chat_setting_dialog);
//        textView = (TextView) dialog
//                .findViewById(R.id.im_chat_setting_dialog_hint);
//        editText = (EditText) dialog
//                .findViewById(R.id.im_group_setting_groupname);
//        textView.setText("修改个性签名");
//        editText.setVisibility(View.VISIBLE);
//        editText.setText(signature.getText());
//        dialog.show();
//
//        // 确定修改个性签名
//        dialog.findViewById(R.id.dialog_button_ok).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (editText.length() > 30) {
//                            show("个性签名字数超出限制!");
//                            dialog.dismiss();
//                        } else {
//                            ModifySignatureEvent modifySignatureEvent = new ModifySignatureEvent();
//                            modifySignatureEvent.setSignature(editText
//                                    .getText().toString().trim());
//                            doModifySignature(modifySignatureEvent);
//                            signature.setText(editText.getText().toString()
//                                    .trim());
//                            dialog.dismiss();
//                        }
//                    }
//                });
//
//        // 取消修改个性签名
//        dialog.findViewById(R.id.dialog_button_cancel).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

    /**
     * 修改个性签名
     *
     * @param
     */
    @Background
    void doModifySignature(ModifySignatureEvent modifySignatureEvent) {
        this.uiEventHandleFacade.handle(modifySignatureEvent);
    }

    @Override
    public void onHandleModifySignatureRespEvent(ModifySignatureRespEvent event) {
        int errorCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == errorCode) {
            show("个性签名修改成功!");
        } else if (ErrorCode.NO_NETWORK.getValue() == errorCode) {
            show("网络连接不可用！");
        } else {
            show("个性签名修改失败!");
        }
    }

    @UiThread
    void show(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHandleRefreshAddrbookRespEvent(RefreshAddrbookRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            initial();
        }
    }
}
