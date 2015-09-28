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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.ui.SearchView;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.event.SearchChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看聊天记录：搜索
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.im_search_chat_msg_activity)
public class SearchChatMsgActivity extends EventFragmentActivity implements
        OnItemClickListener, SearchView.OnTextChangedListener {

    private final static String NO_SEARCH_DATA = "未找到匹配的数据";
    private final static String SEARCH_HINT = "您可以根据关键字搜索到聊天记录";
    @ViewById(R.id.tv_tip)
    TextView tvTip;
    @ViewById(android.R.id.list)
    ListView listView;
    private String mChatId;
    private String mChatName;
    private List<MsgEntity> mBeans;
    private SearchChatMsgAdapter mAdapter;
    private SearchView mSearchView;

    private String mKeyword;


    @AfterViews
    void doAfterViews() {
        mChatId = getIntent().getStringExtra("chatId");
        mChatName = getIntent().getStringExtra("chatName");

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
        mBeans = new ArrayList<MsgEntity>();
        mAdapter = new SearchChatMsgAdapter(this, mBeans);
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
        Intent intent = new Intent(this, SearchChatMsgResultActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("chatId", mChatId);
        bundle.putString("chatName", mChatName);
        bundle.putString("msgId", mBeans.get(position).getMsgId());
        bundle.putString("keyword", mKeyword);
        // bundle.putSerializable("msgs", mMsgs);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence charSequence) {
        mKeyword = charSequence.toString();
        if (TextUtils.isEmpty(charSequence)) {
            listView.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(SEARCH_HINT);
            mBeans.removeAll(mBeans);
            mAdapter.notifyDataSetChanged();
        } else {
            // 搜索数据
            SearchChatMsgEvent searchChatMsgEvent = new SearchChatMsgEvent();
            searchChatMsgEvent.setChatId(this.mChatId);
            searchChatMsgEvent.setSearchKeyWord(charSequence.toString());

            mBeans.removeAll(mBeans);
            mBeans.addAll((List<MsgEntity>) uiEventHandleFacade.handle(searchChatMsgEvent));
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
