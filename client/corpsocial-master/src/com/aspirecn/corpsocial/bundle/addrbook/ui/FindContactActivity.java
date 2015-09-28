package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.listener.FindContactRespSubject;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 从服务器搜索联系人
 * Created by lihaiqiang on 2015/6/17.
 */
@EActivity(R.layout.addrbook_find_contact_from_server_activity)
public class FindContactActivity extends EventFragmentActivity implements
        SearchView.OnTextChangedListener, FindContactRespSubject.FindContactRespEventListener {

    @ViewById(R.id.search_layout)
    View mSearchLayout;
    @ViewById(R.id.query_key_tv)
    TextView mQueryKeyTV;
    private SearchView mSearchView;
    private int mThemeColor;

    @AfterViews
    void doAfterViews() {
        //主题色
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        mSearchLayout.setVisibility(View.GONE);
        mQueryKeyTV.setTextColor(mThemeColor);
        //初始化ActionBar
        mSearchView = new SearchView(this);
        mSearchView.setOnTextChangedListener(this);
        ActionBarFragment fab = ActionBarFragment_.builder().build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(new ActionBarFragment.LifeCycleListener() {
            @Override
            public void onActionBarViewCreated(ActionBarFragment fab) {
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                ll.gravity = Gravity.CENTER;
                ll.setMargins(0, 0, 50, 0);
                mSearchView.setLayoutParams(ll);
                fab.build().setCustomView(mSearchView).apply();
            }
        });
    }

    /**
     * 搜索输入框内容变化回调方法
     *
     * @param charSequence
     */
    @Override
    public void onTextChanged(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            mSearchLayout.setVisibility(View.VISIBLE);
        } else {
            mSearchLayout.setVisibility(View.GONE);
        }
        mQueryKeyTV.setText(charSequence);
    }

    /**
     * 搜索方法
     */
    @Click(R.id.search_layout)
    void onClickSearchLayout() {
        CustomDialog.showProgeress(this);
        //搜索关键字
        String queryKey = mQueryKeyTV.getText().toString();
        sendSearchUserEvent(queryKey);
    }

    /**
     * 发起搜索用户事件
     *
     * @param queryKey
     */
    private void sendSearchUserEvent(String queryKey) {
        FindContactEvent event = new FindContactEvent();
        event.setQueryKey(queryKey);
        UiEventHandleFacade.getInstance().handle(event);
    }

    /**
     * 处理查找联系人相应事件
     *
     * @param event
     */
    @UiThread
    @Override
    public void onHandleFindContactRespEvent(FindContactRespEvent event) {
        CustomDialog.closeProgress(this);
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            List<User> users = event.getUsers();
            //没有符合条件的联系人
            if (users.size() < 1) {
                showAlertDialog("用户不存在");
            }
            //有一条记录
            else if (users.size() < 2) {
                openContactInfoView(users.get(0));
            }
            //多于1条记录
            else {
                showToast("多于1个用户");
            }
        } else {
            showToast("查找用户失败，请检查网络或联系管理员。");
        }
    }

    /**
     * 打开联系人详情页
     *
     * @param user
     */
    private void openContactInfoView(User user) {
        Intent intent = new Intent(this, ContactDetailActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @UiThread
    void showAlertDialog(CharSequence charSequence) {
        final CustomAlertDialog dialog = new CustomAlertDialog(this);
        dialog.setAlertMsg(charSequence);
        dialog.getAlertMsgTV().setGravity(Gravity.CENTER);
        dialog.setBtn1Text("确定");
        dialog.getBtn1().setTextColor(mThemeColor);
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
