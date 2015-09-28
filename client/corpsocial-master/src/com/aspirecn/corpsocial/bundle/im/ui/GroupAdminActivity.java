package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.common.config.Constant;
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

@EActivity(R.layout.im_group_chat_admin_show_activity)
public class GroupAdminActivity extends EventFragmentActivity {

    String creatorId;
    @ViewById(R.id.im_group_chat_admin_master_head)
    ImageView head;
    @ViewById(R.id.im_group_chat_admin_master_name)
    TextView name;
    @ViewById(R.id.group_chat_setting_master_department)
    TextView department;
    private User mUser;

    @AfterViews
    void initial() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.gronp_chat_setting_manager)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        Intent intent = getIntent();
        creatorId = intent.getStringExtra("creatorId");
        doLoadChat(creatorId);
    }

    // 加载个人信息
    @Background
    void doLoadChat(String userId) {
        mUser = UserUtil.getUser(userId);
        ShowInfo();
    }

    // 显示个人信息
    @UiThread
    void ShowInfo() {
        if (mUser != null) {
            name.setText(mUser.getName());
            department.setText(mUser.getSignature());

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .showImageForEmptyUri(R.drawable.component_initial_headimg)
                    .build();
            ImageLoader.getInstance().displayImage(mUser.getUrl(), head, options);
        }
    }

    /**
     * 查看群成员详情
     */
    @Click(R.id.im_group_chat_setting_master)
    void showGroupMasterDetailInfo() {

        Intent intent = new Intent();

//        UserServiceParam serviceParam = new UserServiceParam();
//        serviceParam.setServie("FindContactDetailService");
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("userid", creatorId);
//        serviceParam.setParams(param);
//
//        @SuppressWarnings("unchecked")
//        UserServiceResult<User> result = (UserServiceResult<User>) OsgiServiceLoader.getInstance()
//                .getService(UserService.class).invoke(serviceParam);
//        User user = result.getData();
        User user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(creatorId);

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

    @UiThread
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
