package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindFriendService;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFriendRespSubject;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 联系人详情(服务器搜索)
 * Created by lihaiqiang on 2015/6/17.
 */
@EActivity(R.layout.addrbook_contact_person_info_for_search_server_activity)
public class ContactDetailActivity extends EventFragmentActivity implements
        AddFriendRespSubject.AddFriendRespEventListener {

    @ViewById(R.id.addrbook_contact_person_info_head_iv)
    ImageView mHeadIV;
    @ViewById(R.id.ddrbook_contact_person_info_name_tv)
    TextView mNameTv;
    @ViewById(R.id.ddrbook_contact_person_info_phone_number_tv)
    TextView mPhoneNumberTV;
    @ViewById(R.id.ddrbook_contact_person_info_company_name_tv)
    TextView mCompanyNameTV;
    @ViewById(R.id.ddrbook_contact_person_info_signature_name_tv)
    TextView mSignatureTV;
    @ViewById(R.id.ddrbook_contact_person_info_add_friend_btn_bt)
    Button mAddFriendBtnBT;
    @ViewById(R.id.ddrbook_contact_person_info_send_msg_btn_bt)
    Button mSendMsgBtnBT;
    private int mThemeColor;
    private int mNormalColor;
    private User mUser;

    @AfterInject
    void doAfterInject() {
        mUser = (User) getIntent().getBundleExtra("bundle").get("user");
    }

    @AfterViews
    void doAfterViews() {
        //主题色
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        mNormalColor = mThemeColor - 0x33000000;
        //初始化ActionBar
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.addrbook_personal)).
                transparent(true).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        initView();
    }

    private void initView() {
        mAddFriendBtnBT.setVisibility(View.GONE);
        setBackground(mAddFriendBtnBT, mNormalColor, mThemeColor);
        mSendMsgBtnBT.setVisibility(View.GONE);
        setBackground(mSendMsgBtnBT, mNormalColor, mThemeColor);
        if (mUser != null) {
            findFriendFromLocal(mUser);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.drawable.addrbook_contact_big)
                    .showImageOnFail(R.drawable.addrbook_contact_big)
                    .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                    .build();
            ImageLoader.getInstance().displayImage(mUser.getUrl(), mHeadIV, options);
            mHeadIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            mNameTv.setText(mUser.getName());
            mPhoneNumberTV.setText(mUser.getCellphone());
            mCompanyNameTV.setText(buildUserCorp(mUser.getCorpList()));
            mSignatureTV.setText(mUser.getSignature());
        }
    }

    @Background
    @SuppressWarnings("unchecked")
    void findFriendFromLocal(User user) {
        if (user != null) {
            //是自己时
            String userId = Config.getInstance().getUserId();
            if (userId != null && userId.equals(user.getUserid())) {
                return;
            }
            //查询是否在好友中
//            UserServiceParam userServiceParam = new UserServiceParam();
//            userServiceParam.setServie("FindFriendServiceImpl");
//            Map params = new HashMap();
//            params.put("key", user.getUserid());
//            userServiceParam.setParams(params);
//            UserServiceResult result = (UserServiceResult) OsgiServiceLoader.getInstance()
//                    .getService(UserService.class).invoke(userServiceParam);
//
//            if (result == null) {
//                mAddFriendBtnBT.setVisibility(View.VISIBLE);
//                return;
//            }
//            List<User> users = (List<User>) result.getData();

            List<User> users = (List<User>) OsgiServiceLoader.getInstance().getService(FindFriendService.class).invoke(user.getUserid());
            if (users == null) {
                mAddFriendBtnBT.setVisibility(View.VISIBLE);
                return;
            }
            //在好友中,显示发消息按钮
            if (users.size() > 0) {
                mSendMsgBtnBT.setVisibility(View.VISIBLE);
            }
            //不在好友中,显示添加好友按钮
            else {
                mAddFriendBtnBT.setVisibility(View.VISIBLE);
            }
        }
    }

    private String buildUserCorp(List<UserCorp> userCorps) {
        StringBuffer userCorpSb = new StringBuffer();
        if (userCorps != null) {
            for (int i = 0; i < userCorps.size(); i++) {
                userCorpSb.append(userCorps.get(i).getCorpName());
                if (i < userCorps.size() - 1) {
                    userCorpSb.append("\n");
                }
            }
        }
        return userCorpSb.toString();
    }

    /**
     * 添加好友
     */
    @Click(R.id.ddrbook_contact_person_info_add_friend_btn_bt)
    void onClickAddFriendBtn() {
        CustomDialog.showProgeress(this);
        AddFriendEvent event = new AddFriendEvent();
        event.setUser(mUser);
        UiEventHandleFacade.getInstance().handle(event);
    }

    /**
     * 发送消息
     */
    @Click(R.id.ddrbook_contact_person_info_send_msg_btn_bt)
    void onClickSendMsgBtn() {
        Intent intent = new Intent(this, com.aspirecn.corpsocial.bundle.im.ui.ChatActivity_.class);
        // 传递会话对象
        Bundle bundle = new Bundle();
        bundle.putString("chatId", mUser.getUserid());
        bundle.putString("chatName", mUser.getName());
        bundle.putInt("chatType", 0);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    /**
     * 处理添加好友响应
     *
     * @param event
     */
    @Override
    public void onHandleAddFriendRespEvent(AddFriendRespEvent event) {
        CustomDialog.closeProgress(this);
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            showToast("已发送");
            finish();
        } else {
            showToast(TextUtils.isEmpty(event.getMessage()) ? "发送失败" : event.getMessage());
        }
    }

    @UiThread
    void showToast(CharSequence charSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置点击背景
     *
     * @param view
     * @param normalColor
     * @param pressedColor
     */
    private void setBackground(View view, final int normalColor, final int pressedColor) {
        view.setBackgroundDrawable(getBackground(normalColor));
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundDrawable(getBackground(pressedColor));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setBackgroundDrawable(getBackground(normalColor));
                }
                return false;
            }
        });
    }

    /**
     * 根据颜色获取背景图
     *
     * @param color
     * @return
     */
    private Drawable getBackground(int color) {
        int d = 6;
        float[] outerRadii = new float[]{d, d, d, d, d, d, d, d};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable.getCurrent();
    }

}
