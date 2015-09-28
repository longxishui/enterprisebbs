package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyPasswordEvent;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyPasswordRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyPasswordRespSubject.ModifyPasswordRespEventListener;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

@EActivity(R.layout.setting_ui_update_activity)
public class WorkbenchSettingUpdateActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener,
        ModifyPasswordRespEventListener {

    OsgiServiceLoader osgiServiceLoader = OsgiServiceLoader.getInstance();
    // 手机号码
    @ViewById(R.id.setting_ui_workbench_update_phone_tv)
    TextView phone;
    // 旧密码
    @ViewById(R.id.setting_ui_workbench_oldpwd_et)
    EditText oldPwd;
    // 新密码
    @ViewById(R.id.setting_ui_workbench_newpwd1_et)
    EditText newPwd1;
    // 确认密码
    @ViewById(R.id.setting_ui_workbench_newpwd2_et)
    EditText newPwd2;
    private String oldPassword, newPassword1, newPassword2, neterPassword;
    private User mUser;

    @AfterViews
    void initial() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_updatepwd_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);


        SharedPreferences sharedPre = getSharedPreferences(
                Constant.SHAREDPREFE_NAME_OAWEIXIN, Context.MODE_PRIVATE);
        String userId = sharedPre.getString("user_id", "null");
        neterPassword = sharedPre.getString("password", "null");
        doLoadChat(userId);
    }

    // 加载电话
    @Background
    void doLoadChat(String userId) {
        mUser = getUser(userId);
        ShowInfo();
    }

    // 显示个人电话
    @UiThread
    void ShowInfo() {
        if (mUser != null)
            phone.setText("手机号码: " + mUser.getCellphone());
    }

    /**
     * 修改密码
     *
     * @param modifyPasswordEvent
     */
    @Background
    void doModifyPassword(ModifyPasswordEvent modifyPasswordEvent) {
        this.uiEventHandleFacade.handle(modifyPasswordEvent);
    }

    /**
     * 修改密码反馈
     */
    @Override
    public void onHandleModifyPasswordRespEvent(ModifyPasswordRespEvent event) {
        int errorCode = event.getErrorCode();
        if (ErrorCode.SUCCESS.getValue() == errorCode) {
            show("密码修改成功!");
            finish();
        } else if (ErrorCode.NO_NETWORK.getValue() == errorCode) {
            show("网络连接不可用!");
        } else {
            show("密码修改失败!");
        }
    }

    @UiThread
    void show(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonText(R.string.workbench_setting_updatepwd_ok).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = oldPwd.getText().toString().trim();
                newPassword1 = newPwd1.getText().toString().trim();
                newPassword2 = newPwd2.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword1)
                        || TextUtils.isEmpty(newPassword2)) {
                    show("密码不能为空!");
                } else if (!neterPassword.equals(oldPassword)) {
                    show("输入的旧密码不正确!");
                } else if (!newPassword1.equals(newPassword2)) {
                    show("输入的新密码不一致!");
                } else {
                    ModifyPasswordEvent modifyPasswordEvent = new ModifyPasswordEvent();
                    modifyPasswordEvent.setNewPasswd(newPassword1);
                    modifyPasswordEvent.setOldPasswd(oldPassword);
                    doModifyPassword(modifyPasswordEvent);
                }
            }
        }).apply();
    }

    /**
     * 根据userId获取用户
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
//        UserService service = (UserService) OsgiServiceLoader.getInstance()
//                .getService(UserService.class);
//        UserServiceParam param = new UserServiceParam();
//        param.setServie("FindContactService");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userid", userId);
//        param.setParams(map);
//        UserServiceResult<List<User>> result = service.invoke(param);
//        List<User> users = result.getData();
//        if (users != null && users.size() > 0) {
//            return users.get(0);
//        }
//        return null;
        return (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(userId);
    }
}
