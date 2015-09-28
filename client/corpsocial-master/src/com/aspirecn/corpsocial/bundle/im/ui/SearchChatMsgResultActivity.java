package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.repository.CustomerServiceDao;
import com.aspirecn.corpsocial.bundle.im.event.SearchChatMsgEvent;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.LinkedList;

/**
 * 查看聊天记录：结果展示
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.im_search_chat_msg_result_activity)
public class SearchChatMsgResultActivity extends EventActivity {

    @ViewById(R.id.im_search_chat_msg_result_back_tv)
    TextView mBackBtnTV;
    @ViewById(R.id.im_search_chat_msg_result_list)
    ListView mChatListView;
    private String mChatId;
    private String mMsgId;
    private String mKeyword;
    private LinkedList<MsgEntity> mMsgs;
    private ChatAdapter mAdapter;

    @Click(R.id.im_search_chat_msg_result_back_tv)
    void onClickBackBtn() {
        finish();
    }

    @AfterViews
    void doAfterViews() {
        Intent intent = getIntent();
        this.mChatId = intent.getStringExtra("chatId");
        this.mMsgId = intent.getStringExtra("msgId");
        this.mKeyword = intent.getStringExtra("keyword");
        //if(CustomerServiceDao.isCustomerService(mChatId))
        CustomerServiceDao customerServiceDao = new CustomerServiceDao();
        if (customerServiceDao.findById(mChatId) != null) {
            mBackBtnTV.setText("企业服务号");
        } else
            mBackBtnTV.setText(intent.getStringExtra("chatName"));
        // 加载消息
        loadMsgs();
    }

    @SuppressWarnings("unchecked")
    private void loadMsgs() {
        // 加载数据
        SearchChatMsgEvent searchChatMsgEvent = new SearchChatMsgEvent();
        searchChatMsgEvent.setChatId(this.mChatId);
        searchChatMsgEvent.setSearchKeyWord(mKeyword);
        this.mMsgs = (LinkedList<MsgEntity>) uiEventHandleFacade
                .handle(searchChatMsgEvent);

        // 展示数据
        getAdapter().setData(this.mMsgs);
        mChatListView.setAdapter(getAdapter());
        for (int i = 0; i < mMsgs.size(); i++) {
            if (mMsgs.get(i).getMsgId().equals(mMsgId)) {
                mChatListView.setSelection(i);
                return;
            }
        }
    }

    /**
     * 获取列表适配器
     *
     * @return
     */
    private ChatAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(this);
        }
        return mAdapter;
    }

}
