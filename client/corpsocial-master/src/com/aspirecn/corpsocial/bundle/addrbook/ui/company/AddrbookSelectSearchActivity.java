package com.aspirecn.corpsocial.bundle.addrbook.ui.company;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.SearchContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.QueryResult;
import com.aspirecn.corpsocial.bundle.addrbook.ui.SearchView;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索联系人
 *
 * @author chenziqiang
 */
@EActivity(R.layout.addrbook_search_activity)
public class AddrbookSelectSearchActivity extends EventFragmentActivity implements
        OnItemClickListener, SearchView.OnTextChangedListener {

    private final static String NO_SEARCH_DATA = "未找到匹配的数据";

    private final static String SEARCH_HINT = "您可以搜索到人名，人名全拼，人名简拼，手机号码相关的记录";
    @ViewById(R.id.tv_tip)
    TextView tvTip;
    @ViewById(android.R.id.list)
    ListView listView;
    private List<User> mBeans;
    private AddrbookSelectSearchAdapter mAdapter;
    private SearchView mSearchView;

    private String corpId;

    @AfterViews
    void doAfterViews() {
        Intent intent = getIntent();
        corpId = intent.getStringExtra("corpId");

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
        mBeans = new ArrayList<User>();
        mAdapter = new AddrbookSelectSearchAdapter(this, mBeans);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new OnScrollListener() {

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
        onClickItem(mBeans.get(position));
    }

    public void onClickItem(User contactBriefVO) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectVO", contactBriefVO);
        intent.putExtra("select", bundle);
        AddrbookSelectSearchActivity.this.setResult(200, intent);
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            listView.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(SEARCH_HINT);
            mBeans.removeAll(mBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            // 搜索数据
            mBeans.clear();

            SearchContactEvent searchEvent = new SearchContactEvent();
            if (!corpId.equals("null")) {
                searchEvent.setCorpId(corpId);
            }
            searchEvent.setKeyword(charSequence.toString());
            FindUserEvent event = new FindUserEvent(searchEvent);
            QueryResult queryResult = (QueryResult) UiEventHandleFacade.getInstance().handle(event);

            mBeans.addAll((List<User>) queryResult.getResult());
            // 展示数据
            if (mBeans.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                tvTip.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                tvTip.setVisibility(View.VISIBLE);
                tvTip.setText(NO_SEARCH_DATA);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
