package com.aspirecn.corpsocial.bundle.addrbook.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptInviteRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddressBookUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetFriendListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.QueryResult;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptAddFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptInviteRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddressBookUnReadMsgCountRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.GetFriendListRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.CharacterParser;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.ClearEditText;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.PinyinComparator;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.SideBar;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.ui.widget.CustomAlertDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomDialog;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EFragment(R.layout.addrbook_friend_activity)
public class AddrbookFriendFragment extends EventFragment implements
        AcceptInviteRespSubject.AcceptInviteRespEventListener,
        AcceptAddFriendRespSubject.AcceptAddFriendRespEventListener,
        RemoveFriendRespSubject.RemoveFriendRespEventListener,
        AddressBookUnReadMsgCountRespSubject.AddressBookUnReadMsgCountRespEventListener, GetFriendListRespSubject.GetFriendListRespEventListener {

    @ViewById(R.id.country_lvcountry)
    ListView sortListView;

    @ViewById(R.id.sidrbar)
    SideBar sideBar;

    @ViewById(R.id.dialog)
    TextView dialog;

    @ViewById(R.id.filter_edit)
    ClearEditText mClearEditText;

    //新的朋友新消息提示
    TextView newMsgCount;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private AddrbookFriendAdapter adapter;
    private List<User> mFriendUsers = new ArrayList<User>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private int mThemeColor;

    @AfterViews
    void init() {
        initViews();
        getFriendListDate();

        //统计通讯录未读条数
        calculateAddressBookUnReadMsgCount();
    }

    private void initViews() {
        //主题颜色
        mThemeColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0)) + 1;
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        View headView = View.inflate(getActivity(), R.layout.addrbook_friend_head_view, null);
        sortListView.addHeaderView(headView);

        newMsgCount = (TextView) headView.findViewById(R.id.new_friend_msg_count_tips_tv);
        //新的朋友
        headView.findViewById(R.id.new_friend_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewFriendsActivity_.class);
                startActivity(intent);
            }
        });
        //我的群组
        headView.findViewById(R.id.group_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddrbookGroupActivity_.class);
                startActivity(intent);
            }
        });
        //单击好友列表item
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                int newPosition = position - 1;
                User user = (User) adapter.getItem(newPosition);
                onClickItem(user);
            }
        });
        //长按好友列表item删除好友
        sortListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int newPosition = i - 1;
                showRemoveFriendDialog(mFriendUsers.get(newPosition));
                return true;
            }
        });

        adapter = new AddrbookFriendAdapter(getActivity(), mFriendUsers);
        sortListView.setAdapter(adapter);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    protected void onClickItem(final User user) {
        Intent intent = new Intent();
        if (user.getCorpList().size() > 1) {
            intent.setClass(getActivity(), AddrbookGroupChoose_.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("user", user);
            intent.putExtras(mBundle);
        } else {
            intent.putExtra("ContactId", user.getUserid());
            intent.putExtra("corpId", user.getCorpId());
            intent.setClass(getActivity(), AddrbookPersonalParticularsActivity_.class);
        }
        getActivity().startActivity(intent);
    }

    @Background
    void calculateAddressBookUnReadMsgCount() {
        UiEventHandleFacade.getInstance().handle(new CalculateAddressBookUnReadMsgCountEvent());
    }

    /**
     * 获取数据
     */
    @Background
    void getFriendListDate() {
        FindFriendEvent getEvent = new FindFriendEvent();
        getEvent.setSort("initialKey");
        FindUserEvent event = new FindUserEvent();
        event.setEvent(getEvent);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(event);

        List<User> users = (List<User>) queryResult.getResult();

        if (users != null)
            refreshUi(users);
    }

    @UiThread
    void refreshUi(List<User> users) {
        mFriendUsers.clear();
        mFriendUsers.addAll(users);
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<User> filterDateList = new ArrayList<User>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mFriendUsers;
        } else {
            filterDateList.clear();
            for (User index : mFriendUsers) {
                String name = index.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(index);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }


    private void show(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHandleAcceptInviteRespEvent(AcceptInviteRespEvent event) {
        onUpdate();
    }

    @Override
    public void onHandleAcceptAddFriendRespEvent(AcceptAddFriendRespEvent event) {
        onUpdate();
    }

    private void onUpdate() {
        //   getFriendListDate();
        //   adapter.updateListView(friendListDate);
        getFriendListDate();
    }

    @UiThread
    @Override
    public void onHandleAddressBookUnReadMsgCountRespEvent(AddressBookUnReadMsgCountRespEvent event) {
        int unReadInviteFriendCount = event.getUnReadInviteFriendCount();
        newMsgCount.setVisibility(unReadInviteFriendCount > 0 ? View.VISIBLE : View.GONE);
        newMsgCount.setText(String.valueOf(unReadInviteFriendCount));
    }

    /**
     * 显示删除好友提示框
     *
     * @param user
     */
    private void showRemoveFriendDialog(final User user) {
        final CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
        dialog.setAlertMsg("删除与【" + user.getName() + "】的好友关系？");
        dialog.setBtn1Text("取消");
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setBtn2Text("删除");
        dialog.getBtn2().setTextColor(mThemeColor);
        dialog.setBtn2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CustomDialog.showProgeress(getActivity());
                removeFriend(user);
            }
        });
        dialog.show();
    }

    /**
     * 发起删除好友事件
     *
     * @param user
     */
    @Background
    void removeFriend(User user) {
        RemoveFriendEvent event = new RemoveFriendEvent();
        event.setUserid(user.getUserid());
        UiEventHandleFacade.getInstance().handle(event);
    }

    /**
     * 处理删除好友响应
     *
     * @param event
     */
    @Override
    public void onHandleRemoveFriendRespEvent(RemoveFriendRespEvent event) {
        //主动删除
        if (event.getType() == 0) {
            CustomDialog.closeProgress(getActivity());
            if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
                onUpdate();
                showToast("删除好友成功");
            } else {
                showToast("删除好友失败，请检查网络或联系管理员");
            }
        }
        //被删除
        else if (event.getType() == 1) {
            onUpdate();
        }
    }

    @Override
    public void onHandleGetFriendListRespEvent(GetFriendListRespEvent event) {
        onUpdate();
    }

    @UiThread
    void showToast(CharSequence charSequence) {
        Toast.makeText(getActivity(), charSequence, Toast.LENGTH_SHORT).show();
    }

}
