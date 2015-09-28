package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactService;
import com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookGroupChoose_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.SearchView;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.SearchGroupMemberEvent;
import com.aspirecn.corpsocial.bundle.im.repository.entity.GroupMemberEntity;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

//import com.aspirecn.corpsocial.bundle.addrbook.facade.UserService;

/**
 * 搜索群成员
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.im_search_group_member_activity)
public class SearchGroupMemberActivity extends EventFragmentActivity implements
        OnItemClickListener, SearchView.OnTextChangedListener {

    private final static String NO_SEARCH_DATA = "未找到匹配的数据";
    private final static String SEARCH_HINT = "您可以根据人名，人名全拼，人名简拼，手机号码搜索到群成员";
    @ViewById(R.id.tv_tip)
    TextView tvTip;
    @ViewById(R.id.grid_view)
    GridView gridView;
    private String mChatId;
    private int groupType;
    private List<GroupMemberEntity> mBeans;
    private SearchGroupMemberAdapter mAdapter;
    private SearchView mSearchView;

    @AfterViews
    void doAfterViews() {
        mChatId = getIntent().getStringExtra("groupId");
        groupType = getIntent().getIntExtra("groupType", 0);

        mSearchView = new SearchView(this);
        mSearchView.setOnTextChangedListener(this);
        ActionBarFragment fab = ActionBarFragment_.builder().build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        fab.setLifeCycleListener(new ActionBarFragment.LifeCycleListener() {
            @Override
            public void onActionBarViewCreated(ActionBarFragment fab) {
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                ll.gravity = Gravity.CENTER;
                ll.setMargins(0, 0, 50, 0);

                mSearchView.setLayoutParams(ll);
                fab.build().setCustomView(mSearchView).apply();
            }
        });


        // list
        mBeans = new ArrayList<GroupMemberEntity>();
        mAdapter = new SearchGroupMemberAdapter(this, mBeans);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        tvTip.setText(SEARCH_HINT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();

//        UserServiceParam serviceParam = new UserServiceParam();
//        serviceParam.setServie("FindContactDetailService");
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("userid", this.mBeans.get(position).getMemberId());
//        serviceParam.setParams(param);
//
//        UserServiceResult<User> result = (UserServiceResult<User>) OsgiServiceLoader.getInstance()
//                .getService(UserService.class).invoke(serviceParam);
//        User user = result.getData();

        User user = (User) OsgiServiceLoader.getInstance().getService(FindContactService.class).invoke(this.mBeans.get(position).getMemberId());
        if (user != null) {
            if (user.getCorpList().size() > 1) {
                intent.setClass(this, AddrbookGroupChoose_.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user", user);
                intent.putExtras(mBundle);
            } else {
                intent.putExtra("ContactId", this.mBeans.get(position).getMemberId());
                intent.putExtra("corpId", user.getCorpId());
                intent.setClass(this, com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
            }
            startActivity(intent);
            finish();
        }


//        Intent intent = new Intent(this, com.aspirecn.corpsocial.bundle.addrbook.ui.AddrbookPersonalParticularsActivity_.class);
//        intent.putExtra("ContactId", this.mBeans.get(position).getMemberId());
//        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            gridView.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(SEARCH_HINT);
            mBeans.removeAll(mBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            // 搜索数据
            SearchGroupMemberEvent groupMemberEvent = new SearchGroupMemberEvent();
            groupMemberEvent.setGroupId(this.mChatId);
            groupMemberEvent.setSearchKeyWord(charSequence.toString());
            groupMemberEvent.setGroupType(groupType);

            mBeans.removeAll(mBeans);
            mBeans.addAll((List<GroupMemberEntity>) uiEventHandleFacade.handle(groupMemberEvent));
            // 展示数据
            if (mBeans.size() > 0) {
                gridView.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.GONE);
            } else {
                gridView.setVisibility(View.GONE);
                tvTip.setVisibility(View.VISIBLE);
                tvTip.setText(NO_SEARCH_DATA);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
