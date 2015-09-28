package com.aspirecn.corpsocial.bundle.addrbook.ui;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptAddFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AcceptInviteRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.AddressBookUnReadMsgCountRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.BatchAddFrequentlyContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.BatchAddFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.CalculateAddressBookUnReadMsgCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FetchFrequentlyContactsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFrequentlyContactEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFrequentlyContactRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RemoveFriendRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddressBookConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.FindContactsByIdsService;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptAddFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AcceptInviteRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddFrequentlyContactsRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.AddressBookUnReadMsgCountRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.BatchAddFrequentlyContactsRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFrequentlyContactsRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RemoveFriendRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.ui.company.CompanyActivty_;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.CharacterParser;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.PinyinComparator;
import com.aspirecn.corpsocial.bundle.addrbook.ui.widget.SideBar;
import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
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
import java.util.Comparator;
import java.util.List;

@EFragment(R.layout.addrbook_often_activity)
public class AddrbookOftenFragment extends EventFragment implements
        AcceptInviteRespSubject.AcceptInviteRespEventListener,
        AcceptAddFriendRespSubject.AcceptAddFriendRespEventListener,
        RemoveFriendRespSubject.RemoveFriendRespEventListener,
        RemoveFrequentlyContactsRespSubject.RemoveFrequentlyContactRespEventListener,
        AddFrequentlyContactsRespSubject.AddFrequentlyContactRespEventListener,
        BatchAddFrequentlyContactsRespSubject.BatchAddFrequentlyContactRespEventListener,
        AddressBookUnReadMsgCountRespSubject.AddressBookUnReadMsgCountRespEventListener {

    @ViewById(R.id.addrbook_often_country_lvcountry)
    ListView sortListView;

    @ViewById(R.id.addrbook_often_sidrbar)
    SideBar sideBar;

    @ViewById(R.id.addrbook_often_dialog)
    TextView dialog;

    @ViewById(R.id.new_often_tv)
    LinearLayout layoutAdd;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private AddrbookOftenAdapter adapter;
    private List<User> mFriendUsers = new ArrayList<User>();
    private Bundle bundle = new Bundle();
    private List<String> companyIds = new ArrayList<String>();
    protected boolean addRl = true;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private int mThemeColor;

    @AfterViews
    void init() {
        initViews();
        getFriendListDate();
    }

    @ViewById(R.id.new_often_msg_count_tips_tv)
    TextView newMsgCount;
    @ViewById(R.id.often_group_tv)
    TextView groupTv;
    @ViewById(R.id.new_often_tv)
    LinearLayout oftenTv;

    private void initViews() {
        if (addRl)
            layoutAdd.setVisibility(View.VISIBLE);
        else
            layoutAdd.setVisibility(View.GONE);


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

        //添加常用联系人
        oftenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CompanyActivty_.class);
                AddrbookSelectConfig selectConfig = new AddrbookSelectConfig();
                selectConfig.setSelectType(AddrbookSelectConfig.AddrbookSelectType.SELECT);
                selectConfig.setSelectForMe(true);
                if (mFriendUsers != null) {
                    for (User item : mFriendUsers) {
                        companyIds.add(item.getUserid());
                    }
                }

                selectConfig.setExistingContactIds(companyIds);


                bundle.putSerializable(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY, selectConfig);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);


            }
        });

        //单击好友列表item
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickItem(mFriendUsers.get(position));

            }
        });
        //长按好友列表item删除好友
        sortListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showRemoveFriendDialog(mFriendUsers.get(i));
                return true;
            }
        });

        adapter = new AddrbookOftenAdapter(getActivity(), mFriendUsers);
        sortListView.setAdapter(adapter);

    }

    protected void onClickItem(User user) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {

            companyIds = data.getStringArrayListExtra(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY);
            List<User> members = (List<User>) OsgiServiceLoader.getInstance().getService(FindContactsByIdsService.class).invoke(companyIds);

            List<String> useridList = new ArrayList<String>();

            for (User item : members) {
                useridList.add(item.getUserid());
            }

            BatchAddFrequentlyContactEvent event = new BatchAddFrequentlyContactEvent();

            event.setContactIds(useridList);

            UiEventHandleFacade.getInstance().handle(event);

            CustomDialog.showProgeress(getActivity());

        }
    }

    @Override
    public void onHandleAddFrequentlyContactRespEvent(BatchAddFrequentlyContactRespEvent event) {
        CustomDialog.closeProgress(getActivity());
        if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
            onUpdate();
            showToast("添加常用联系人成功");
        } else {
            showToast("添加常用联系人失败，请检查网络或联系管理员");
        }
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
        FetchFrequentlyContactsEvent event = new FetchFrequentlyContactsEvent();

        List<User> users = (List<User>) uiEventHandleFacade.handle(event);

        Comparator<User> com = new Comparator<User>() {
            public int compare(User o1, User o2) {
                return o1.getSpellKey().compareTo(o2.getSpellKey());
            }
        };

        Collections.sort(users, com);

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

    @UiThread
    void show(String msg) {
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
        //  adapter.updateListView(mFriendUsers);
        getFriendListDate();
    }

    @UiThread
    @Override
    public void onHandleAddressBookUnReadMsgCountRespEvent(AddressBookUnReadMsgCountRespEvent event) {
        int unReadInviteFriendCount = event.getUnReadInviteFriendCount();
    }

    /**
     * 显示删除好友提示框
     *
     * @param user
     */
    private void showRemoveFriendDialog(final User user) {
        final CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
        dialog.setAlertMsg("【" + user.getName() + "】移除常用联系人？");
        dialog.setBtn1Text("取消");
        dialog.setBtn1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setBtn2Text("确认");
        dialog.getBtn2().setTextColor(mThemeColor);
        dialog.setBtn2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CustomDialog.showProgeress(getActivity());
                removeFrequentlyContact(user);
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
    void removeFrequentlyContact(User user) {
        RemoveFrequentlyContactEvent event = new RemoveFrequentlyContactEvent();

        event.setContactId(user.getUserid());

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
    public void onHandleRemoveFrequentlyContactRespEvent(RemoveFrequentlyContactRespEvent event) {
        //主动删除
        if (event.getStatus() == 0) {
            CustomDialog.closeProgress(getActivity());
            if (event.getStatus() == ErrorCode.SUCCESS.getValue()) {
                onUpdate();
                showToast("移除常用联系人成功");
            } else {
                showToast("移除常用联系人失败，请检查网络或联系管理员");
            }
        }
        //被删除
        else if (event.getStatus() == 1) {
            onUpdate();
        }
    }

    @UiThread
    void showToast(CharSequence charSequence) {
        Toast.makeText(getActivity(), charSequence, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHandleAddFrequentlyContactRespEvent(AddFrequentlyContactRespEvent event) {
        onUpdate();
    }
}
