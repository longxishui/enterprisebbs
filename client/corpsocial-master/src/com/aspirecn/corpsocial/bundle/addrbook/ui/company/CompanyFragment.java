package com.aspirecn.corpsocial.bundle.addrbook.ui.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindFriendEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.FindUserEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetAddressListRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByDeptEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactByIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactDetailEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetContactIdsByDeptIdsEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetDeptChildrenEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.GetSelectContactCountEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshDepartmentRespEvent;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.QueryResult;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddrbookSelectConfig;
import com.aspirecn.corpsocial.bundle.addrbook.facade.AddressBookConfig;
import com.aspirecn.corpsocial.bundle.addrbook.listener.GetAddressListRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshAddrBookRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.listener.RefreshDepartmentRespSubject;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.CompanyEntity;
import com.aspirecn.corpsocial.bundle.addrbook.uihandler.GetSelectContactCountEventHandler;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.bundle.settings.listener.ModifyHeadImgRespSubject;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;

/**
 * Created by chenziqiang on 15-5-4.
 */
@EFragment(R.layout.addrbook_company_main)
public class CompanyFragment extends EventFragment implements
        RefreshDepartmentRespSubject.RefreshDepartmentRespEventListener,
        RefreshAddrBookRespSubject.RefreshAddrbookRespEventListener
        , ModifyHeadImgRespSubject.ModifyHeadPortraitRespEventListener, GetAddressListRespSubject.GetAddressListRespEventListener {

    @ViewById(R.id.addrbook_company_pulllist)
    ListView listView;
    @ViewById(R.id.addrbook_select_contacts)
    LinearLayout showSelectView;
    @ViewById(R.id.addrbook_contact_submit)
    RelativeLayout bottomHeadView;
    @ViewById(R.id.addrbook_contacts_submit)
    Button submitBtn;
    @ViewById(R.id.addrbook_company_empty_tv)
    TextView empty_tv;

    private String userId;
    private String groupId;
    private CompanyAdapter mAdapter;
    private List<CompanyEntity> mEntitiys;//需要展示的数据
    private AddrbookSelectConfig selectConfig;//联系人配置文件
    private List<CompanyEntity> companySelects;//选择联系人操作数据
    private Toast mToast;
    private int selectCount = 0;
    private CompanyEntity root;
    private Object robj = new Object();
    private boolean refreshing = false;

    public AddrbookSelectConfig getSelectConfig() {
        return selectConfig;
    }

    @AfterViews
    void doAfterViews() {
        root = getRoot();
        userId = Config.getInstance().getUserId();
        mEntitiys = new ArrayList<>();
        companySelects = new ArrayList<>();

        mAdapter = new CompanyAdapter(this, mEntitiys);

        try {
            Intent intent = CompanyFragment.this.getActivity().getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                selectConfig = (AddrbookSelectConfig) bundle.getSerializable(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY);
            } else {

            }
        } catch (NullPointerException e) {
            Log.e("AddrbookSelectEntity", "AddrbookSelectEntity接收失败" + e);
        }
        if (selectConfig == null) {
            selectConfig = new AddrbookSelectConfig();
            selectConfig.setSelectType(AddrbookSelectConfig.AddrbookSelectType.WATCH);
        }
        if (null == selectConfig.getExistingContactIds())
            selectConfig.setExistingContactIds(new ArrayList<String>());
        if (null == selectConfig.getContactIds())
            selectConfig.setContactIds(new ArrayList<String>());
        if (null == selectConfig.getGroupIds())
            selectConfig.setGroupIds(new ArrayList<String>());
        if (selectConfig.getMaxNumber() == 0) {
            selectConfig.setMaxNumber(200);
        }

        if (selectConfig.isSelectForMe() && selectConfig.getExistingContactIds().indexOf(userId) == -1) {
            selectConfig.getExistingContactIds().add(userId);
        }

        listView.setAdapter(mAdapter);
        listView.setEmptyView(empty_tv);


        getMyData();
//        getData(root);

    }

    //初始化底部头像
    private void initializeHeadImg() {
        GetContactByIdsEvent event = new GetContactByIdsEvent();
        event.setIds(selectConfig.getContactIds());
        FindUserEvent findUserEvent = new FindUserEvent();
        findUserEvent.setEvent(event);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
        List<User> list = (List<User>) queryResult.getResult();
        for (User item : list) {
            CompanyEntity companyEntity = new CompanyEntity();

            companyEntity.setType(CompanyEntity.CompanyType.CONTACT);
            companyEntity.setId(item.getUserid());
            companyEntity.setParentId(item.getDeptCode());
            companyEntity.setAllContactCount(1);
            companyEntity.setHeadImgUrl(item.getUrl());
            companyEntity.setSelector(true);
            companyEntity.setUnSelect(false);
            companySelects.add(companyEntity);
        }
    }

    /**
     * 初始化数据
     */
    @UiThread
    public void getMyData() {
        mEntitiys.clear();
        companySelects.clear();
        if (selectConfig.getSelectType() != AddrbookSelectConfig.AddrbookSelectType.WATCH) {
            initializeHeadImg();
//            appFeedback();
        }
        GetContactDetailEvent getEvent = new GetContactDetailEvent();
        getEvent.setContactId(userId);
        FindUserEvent findUserEvent = new FindUserEvent();
        findUserEvent.setEvent(getEvent);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
        User contact = (User) queryResult.getResult();


        if (contact != null) {
            groupId = contact.getDeptCode();
            getData(root);
            if (selectConfig.getGroupIds() != null && selectConfig.getGroupIds().size() > 0) {
                List<CompanyEntity> deleteGroups = new ArrayList<>();

                for (CompanyEntity deleteGroup : companySelects) {
                    if (selectConfig.getGroupIds().indexOf(deleteGroup.getCorpId()) == -1) {
                        deleteGroups.add(deleteGroup);
                    }
                }
                companySelects.removeAll(deleteGroups);
                mEntitiys.removeAll(deleteGroups);
            }
            if (contact.getCorpList().size() == 1) {
                expandMyData(contact);
            }

        }

        if (selectConfig.isShowFriend() && (selectConfig.getSelectType() != AddrbookSelectConfig.AddrbookSelectType.WATCH)) {
            List<User> users = getFriendData();
            if (users.size() > 0)
                addFriendGroup();
        }
    }

    //展开到我的部门
    private void expandMyData(User contact) {
        if (!TextUtils.isEmpty(contact.getDeptId()) && findCompanyItem(contact.getUserid(), groupId) == null) {

            String[] deptIds = contact.getDeptCode().split("\\.");
            String dept = null;
            for (int i = 0; i < deptIds.length; i++) {
                if (dept != null) {
                    dept = dept + "." + deptIds[i];
                } else {
                    dept = deptIds[i];
                }
                if (findCompanyGroupItem(dept) != null) {
                    CompanyEntity deptGroup = findCompanyGroupItem(dept);
                    deptGroup.setExpanded(true);
                    getData(deptGroup);
                }

            }
        }

        int index = mEntitiys.indexOf(findCompanyItem(userId, contact.getDeptCode()));
        if (index - 3 > 0)
            listView.setSelection(index - 3);
        else
            listView.setSelection(index);
    }

    /**
     * 展开部门数据
     *
     * @param entity
     */
    public void getData(CompanyEntity entity) {
        List<Dept> deptEntities = getGroupData(entity.getId());
        List<User> users = getContactData(entity.getId());
        getCompanyData(deptEntities, users, entity);
        notifyData();
        refreshing = false;
    }

    private CompanyEntity getRoot() {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(null);
        entity.setLevel(0);
        entity.setSelector(false);
        entity.setExpanded(false);
        entity.setUnSelect(false);
        return entity;
    }

    private List<Dept> getGroupData(String id) {
        GetDeptChildrenEvent event = new GetDeptChildrenEvent();
        event.setDeptId(id);
        FindUserEvent findUserEvent = new FindUserEvent();
        findUserEvent.setEvent(event);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
        return (List<Dept>) queryResult.getResult();
    }

    private List<User> getContactData(String id) {
        GetContactByDeptEvent event = new GetContactByDeptEvent();
        event.setDeptId(id);
        FindUserEvent findUserEvent = new FindUserEvent();
        findUserEvent.setEvent(event);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
        return (List<User>) queryResult.getResult();
    }

    /**
     * 处理部门数据和联系人数据
     *
     * @param deptEntities 部门数据列表
     * @param users        联系人数据列表
     */
    private void getCompanyData(List<Dept> deptEntities, List<User> users,
                                CompanyEntity parent) {
        List<CompanyEntity> companyEntities = new ArrayList<CompanyEntity>();
        int level = 0;
        int index = 0;
        for (CompanyEntity item : mEntitiys) {
            if (item.getId().equals(parent.getId())) {
                level = item.getLevel() + 1;
                index = mEntitiys.indexOf(item) + 1;
                break;
            }
        }
        boolean isNewCompany;
        //处理联系人数据
        if (users != null && users.size() > 0) {

            for (User item : users) {

                CompanyEntity companyEntity = findCompanyItem(item.getUserid(), item.getDeptCode());
                if (companyEntity == null) {
                    companyEntity = new CompanyEntity();
                    companyEntity.setParentId(item.getDeptCode());

                    companyEntity.setType(CompanyEntity.CompanyType.CONTACT);
                    companyEntity.setId(item.getUserid());

                    companyEntity.setAllContactCount(1);

                    isNewCompany = true;
                } else {
                    isNewCompany = false;
                }
                companyEntity.setName(item.getName());
                companyEntity.setSignature(item.getSignature());
                companyEntity.setHeadImgUrl(item.getUrl());
                companyEntity.setLevel(level);
                companyEntity.setCorpId(item.getCorpId());
                //选择框在非观看模式中的设置
                if (selectConfig.getSelectType() != AddrbookSelectConfig.AddrbookSelectType.WATCH) {
                    /*当配置文件中处于不可选中状态时的操作*/
                    if (selectConfig.getExistingContactIds().indexOf(item.getUserid()) != -1) {
                        companyEntity.setUnSelect(true);
                        companyEntity.setSelector(false);
                    } else {
                        companyEntity.setUnSelect(false);
                        /*如果是新数据，并且配置文件中存在，则为选中状态*/
                        if (isNewCompany) {
                            if (selectConfig.getContactIds().indexOf(item.getUserid()) != -1) {
                                companyEntity.setSelector(true);
                            } else {
                                companyEntity.setSelector(false);
                            }
                        }
                        /*如果部门为全选状态，则改变为选中状态*/
                        if (parent.isSelector()) {
                            companyEntity.setSelector(true);
                        }
                    }
                }
                companyEntities.add(companyEntity);
            }
        }

        //处理部门数据
        if (deptEntities != null && deptEntities.size() > 0) {
            for (Dept dept : deptEntities) {
                CompanyEntity companyEntity = findCompanyItem(dept.getCode(), dept.getPcode());
                if (companyEntity == null) {
                    companyEntity = new CompanyEntity();
                    /*设置部门的总人数*/
                    GetSelectContactCountEvent event = new GetSelectContactCountEvent();
                    event.setId(dept.getCode());
                    event.setCorpId(dept.getCorpId());
                    //GetSelectContactCountEventHandler handler = new GetSelectContactCountEventHandler();
                    //companyEntity.setAllContactCount(handler.handle(event));
                    companyEntity.setAllContactCount((Integer)UiEventHandleFacade.getInstance().handle(event));

                    companyEntity.setLevel(dept.getLevel());
                    companyEntity.setType(CompanyEntity.CompanyType.GROUP);
                    companyEntity.setId(dept.getCode());
                    companyEntity.setName(dept.getName());
                    companyEntity.setParentId(dept.getPcode());
                    companyEntity.setCorpId(dept.getCorpId());

                    isNewCompany = true;
                } else {
                    isNewCompany = false;
                }

                companyEntity.setExpanded(false);

                if (selectConfig.getSelectType() != AddrbookSelectConfig.AddrbookSelectType.WATCH) {
                    companyEntity.setUnSelect(false);

                    /*如果是新创建的，则不作选中处理*/
                    if (isNewCompany) {
                        /*如果缓存数据中存在部门*/
                        if (selectConfig.getGroupIds().indexOf(dept.getCode()) != -1) {
                            companyEntity.setSelector(true);
                        } else {
                            companyEntity.setSelector(false);
                        }
                    }
                    /*如果部门为全选状态，则改变为选中状态*/
                    if (parent.isSelector()) {
                        companyEntity.setSelector(true);
                    }
                }

                companyEntities.add(companyEntity);
            }
        }
        //倒序排序，加载完毕后为正序显示
        Collections.reverse(companyEntities);
        for (CompanyEntity item : companyEntities) {
            mEntitiys.add(index, item);
            if (companySelects.indexOf(item) == -1) {
                companySelects.add(item);
            }
        }
    }

    //根据USERID和部门ID找出item
    private CompanyEntity findCompanyItem(String userId, String parentId) {
        if (!StringUtils.isBlank(userId))
            for (CompanyEntity item : companySelects) {
                if (userId.equals(item.getId()) && parentId.equals(item.getParentId())) {
                    return item;
                }
            }
        return null;
    }

    //根据部门ID，找出部门
    private CompanyEntity findCompanyGroupItem(String userId) {
        if (!StringUtils.isBlank(userId))
            for (CompanyEntity item : companySelects) {
                if (userId.equals(item.getId())) {
                    return item;
                }
            }
        return null;
    }

    /**
     * 刷新数据
     */
    @UiThread
    void notifyData() {
        mAdapter.notifyDataSetChanged();
        if (selectConfig.getSelectType() != AddrbookSelectConfig.AddrbookSelectType.WATCH)
            notifyBottomHeadImgView();
    }

    /**
     * 收缩部门数据
     */
    public void closeGroupData(String groupId) {
        List<Dept> depts = getGroupData(groupId);
        List<User> users = getContactData(groupId);

        if (!depts.isEmpty())
            delete(deleteGroup(depts));

        if (!users.isEmpty())
            delete(deleteContact(users));

        notifyData();
    }

    /**
     * 查找出要删除的联系人数据
     */
    private List<CompanyEntity> deleteContact(List<User> users) {
        List<CompanyEntity> companyEntities = new ArrayList<CompanyEntity>();
        for (User item : users) {
            for (CompanyEntity companyEntity : mEntitiys) {
                if (item.getUserid().equals(companyEntity.getId()) && item.getDeptCode().equals(companyEntity.getParentId())) {
                    companyEntities.add(companyEntity);
                    break;
                }
            }
        }
        return companyEntities;
    }

    /**
     * 查找出要删除的部门数据
     */
    private List<CompanyEntity> deleteGroup(List<Dept> depts) {
        List<CompanyEntity> companyEntities = new ArrayList<CompanyEntity>();
        for (Dept item : depts) {
            for (CompanyEntity companyEntity : mEntitiys) {
                if (item.getCode().equals(companyEntity.getId())) {
                    companyEntities.add(companyEntity);
                    if (companyEntity.isExpanded()) {
                        closeGroupData(item.getCode());
                    }
                    break;
                }

            }
        }
        return companyEntities;
    }

    /**
     * 执行删除操作
     */
    private void delete(List<CompanyEntity> companyEntities) {
        for (CompanyEntity item : companyEntities) {
            int index = mEntitiys.indexOf(item);
            if (index != -1)
                mEntitiys.remove(index);
        }
    }

    //选择联系人
    public void selectCompanyData(CompanyEntity companyEntity) {
        selectCompany(companyEntity);
        notifyData();
    }

    //选择联系人数据处理
    private void selectCompany(CompanyEntity companyEntity) {
        int index = companySelects.indexOf(companyEntity);
        CompanyEntity companySelect = companySelects.get(index);
        companySelect.setSelector(true);

        companyEntity.setSelector(true);
        /*如果是选择部门*/
        if (companyEntity.getType() == CompanyEntity.CompanyType.GROUP) {
            //处理下级的部门和联系人为全选状态
            allSelectContacts(companyEntity.getId(), true);
            allSelectGroups(companyEntity.getId(), true);
        } else {
            /*如果是选择联系人*/

        }
        CompanyEntity groupItem = findCompanyGroupItem(companyEntity.getParentId());
        //如果上级没有处于选中状态，则进行一同级查询
        //检查同级的联系人和部门是不是都已经全部选中了
        //如果已经全部选中则处理上级
        if (groupItem != null && !groupItem.isSelector())
            if (isAllContactSelect(companyEntity.getParentId()) && isAllGroupSelect(companyEntity.getParentId())) {
                selectCompany(groupItem);
            }

    }

    //检查是不是全选了联系人
    private boolean isAllContactSelect(String parentId) {
        List<User> contact = getContactData(parentId);
        for (User item : contact) {
            for (CompanyEntity entity : mEntitiys) {
                if (item.getUserid().equals(entity.getId())) {
                    if (!entity.isSelector())
                        return false;
                    break;
                }
            }
        }
        return true;
    }

    //检查是不是全选了部门
    private boolean isAllGroupSelect(String parentId) {
        List<Dept> deptEntities = getGroupData(parentId);
        for (Dept item : deptEntities) {
            for (CompanyEntity entity : mEntitiys) {
                if (item.getCode().equals(entity.getId())) {
                    if (!entity.isSelector())
                        return false;
                    break;
                }
            }
        }
        return true;
    }

    /**
     * 部门选择操作时的 联系人 循环处理方式
     *
     * @param parentId
     * @param selected 为true的时候表示选中操作，false表示取消选择操作
     */
    private void allSelectContacts(String parentId, boolean selected) {
        List<User> users = getContactData(parentId);
        for (User item : users) {
            //操作全部数据
            for (CompanyEntity entity : companySelects) {
                int index = mEntitiys.indexOf(entity);
                CompanyEntity mEntity = null;
                if (index != -1)
                    mEntity = mEntitiys.get(index);
                if (item.getUserid().equals(entity.getId())) {
                    if (selected) {
                        if (!entity.isSelector()) {
                            selectCompany(entity);
                            if (mEntity != null)
                                selectCompany(mEntity);
                        }
                    } else {
                        if (entity.isSelector()) {
                            deSelectCompany(entity);
                            if (mEntity != null)
                                deSelectCompany(mEntity);
                        }
                    }
                    break;
                }
            }

        }
    }

    /**
     * 部门选择操作时的 部门 循环处理方式
     *
     * @param parentId
     * @param selected 为true的时候表示选中操作，false表示取消选择操作
     */
    private void allSelectGroups(String parentId, boolean selected) {
        List<Dept> deptEntities = getGroupData(parentId);
        for (Dept item : deptEntities) {
            for (CompanyEntity entity : companySelects) {
                if (item.getCode().equals(entity.getId())) {
                    if (selected) {
                        if (!entity.isSelector())
                            selectCompany(entity);
                    } else {
                        if (entity.isSelector())
                            deSelectCompany(entity);
                    }
                    break;
                }
            }
        }
    }

    //取消联系人选择
    public void deSelectCompanyData(CompanyEntity companyEntity) {
        deSelectCompany(companyEntity);
        deSelectParent(companyEntity);
        notifyData();
    }

    private void deSelectCompany(CompanyEntity companyEntity) {
        /*如果是取消选择部门*/
        companyEntity.setSelector(false);
        if (companyEntity.getType() == CompanyEntity.CompanyType.GROUP) {
            //处理下级的部门和联系人为全部取消选状态
            allSelectContacts(companyEntity.getId(), false);
            allSelectGroups(companyEntity.getId(), false);

        } else {
            /*如果是取消选择联系人*/
        }
    }

    //单独取消上级全选状态,不改变已选择的其他部门和联系人的状态
    private void deSelectParent(CompanyEntity companyEntity) {
        CompanyEntity groupItem = findCompanyGroupItem(companyEntity.getParentId());
        if (groupItem != null && groupItem.isSelector()) {
            int index = mEntitiys.indexOf(groupItem);
            if (index != -1) {
                CompanyEntity entity = mEntitiys.get(index);
                entity.setSelector(false);
            }
            groupItem.setSelector(false);
            deSelectParent(groupItem);
        }
    }

    //单选事件
    public void onlySelect(CompanyEntity companyEntity) {
        companyEntity.setSelector(true);
        for (CompanyEntity entity : companySelects) {
            if (entity.getId().equals(companyEntity.getId())) {
                entity.setSelector(true);
            } else {
                entity.setSelector(false);
            }
        }
        notifyData();

    }

    /*创建并展示顶部头像*/
    private void createHeadView(final CompanyEntity companyEntity) {
        View view = showSelectView.findViewWithTag(companyEntity.getId());
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.addrbook_component_headimg, null);
            ImageView headImgView = (ImageView) view.findViewById(R.id.addrbook_component_headimg);
            TextView groupName = (TextView) view.findViewById(R.id.addrbook_component_headimg_text);

            if (companyEntity.getType() == CompanyEntity.CompanyType.GROUP) {
                groupName.setText(companyEntity.getName().substring(0, 1));
                headImgView.setImageResource(R.drawable.im_main_tab_head_portrait_1);
                groupName.setTextColor(Color.parseColor("#7d7d7d"));
            } else {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                        .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                        .build();
                ImageLoader.getInstance().displayImage(companyEntity.getHeadImgUrl(), headImgView, options);
            }
            view.setTag(companyEntity.getId());
            showSelectView.addView(view);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deSelectCompanyData(companyEntity);
                }
            });
        }
    }

    /*删除底部头像*/
    private void deleteHeadView(String id) {
        View view = showSelectView.findViewWithTag(id);
        if (view != null) {
            showSelectView.removeView(view);
        }
    }

    /**
     * 底部头像更新
     */
    private void notifyBottomHeadImgView() {
        selectCount = 0;
        List<Integer> contactCount = new ArrayList<Integer>();
        for (CompanyEntity item : companySelects) {
            CompanyEntity parent = findCompanyGroupItem(item.getParentId());
            if (item.isSelector()) {
                createHeadView(item);
            } else {
                deleteHeadView(item.getId());
            }

            if (parent != null) {
                if (parent.isSelector()) {
                    deleteHeadView(item.getId());
                } else {
                    if (item.isSelector())
                        contactCount.add(item.getAllContactCount());
                }
            } else {
                if (item.isSelector()) {
                    contactCount.add(item.getAllContactCount());
                }
            }
        }

        for (Integer count : contactCount) {
            selectCount = selectCount + count;
        }

        showSelectCount(selectCount);
        bottomHeadView.setVisibility(View.VISIBLE);
        if (showSelectView.getChildCount() != 0) {
            if (!showSelectView.isShown())
                bottomHeadView.setVisibility(View.VISIBLE);
        } else {
            bottomHeadView.setVisibility(View.GONE);
        }
    }

    @UiThread
    void showSelectCount(int selectCount) {

        if (getSelectConfig().getMaxNumber() != -1) {
            if (selectCount > getSelectConfig().getMaxNumber()) {
                showToast("人数不能超过上限(" + getSelectConfig().getMaxNumber() + ")");
                submitBtn.setText("超出" + (selectCount - getSelectConfig().getMaxNumber()) + "人");
                submitBtn.setBackgroundResource(R.drawable.addrbook_btn_no);
            } else if (selectCount < getSelectConfig().getMinNumber()) {
                showToast("人数不能低于下限(" + getSelectConfig().getMaxNumber() + ")");
                submitBtn.setBackgroundResource(R.drawable.addrbook_btn_no);
                submitBtn.setText("低于" + (getSelectConfig().getMinNumber() - selectCount) + "人");
            } else {
                submitBtn.setBackgroundResource(R.drawable.addrbook_select_submit_bg);
                submitBtn.setText("确定(" + selectCount + ")");
            }
        } else {
            if (selectCount < getSelectConfig().getMinNumber()) {
                showToast("人数不能低于下限(" + getSelectConfig().getMaxNumber() + ")");
                submitBtn.setBackgroundResource(R.drawable.addrbook_btn_no);
                submitBtn.setText("低于" + (getSelectConfig().getMinNumber() - selectCount) + "人");
            } else {
                submitBtn.setBackgroundResource(R.drawable.addrbook_select_submit_bg);
                submitBtn.setText("确定(" + selectCount + ")");
            }
        }
    }

    @Click(R.id.addrbook_contacts_submit)
    void submit() {
        resultData();
    }

    private void resultData() {
        if (selectCount >= selectConfig.getMinNumber() && selectCount <= selectConfig.getMaxNumber()) {
            ArrayList<String> resultIds = new ArrayList<String>();
            List<String> groupIds = new ArrayList<String>();

            for (CompanyEntity item : companySelects) {
                CompanyEntity groupItem = findCompanyGroupItem(item.getParentId());
                //如果是好友
                if (item.isFriend()) {
                    if (item.getType() == CompanyEntity.CompanyType.GROUP) {
                        if (item.isSelector()) {
                            List<User> users = getFriendData();
                            for (User user : users) {
                                resultIds.add(user.getUserid());
                            }
                        }
                    } else {
                        if (item.isSelector()) {
                            resultIds.add(item.getId());
                        }
                    }
                } else {
            /*如果是联系人，直接加入反馈的list*/
                    if (item.getType() == CompanyEntity.CompanyType.CONTACT) {
                        if (groupItem != null) {
                            if (!groupItem.isSelector() && item.isSelector()) {
                                resultIds.add(item.getId());
                            }
                        } else {
                            if (item.isSelector()) {
                                resultIds.add(item.getId());
                            }
                        }
                    } else {
                 /*如果是部门，先存入部门list，查询出这些部门下面所有的联系人id*/
                        if (groupItem != null) {
                            if (!groupItem.isSelector() && item.isSelector()) {
                                groupIds.add(item.getId());
                            }
                        } else {
                            if (item.isSelector()) {
                                groupIds.add(item.getId());
                            }
                        }
                    }
                }
            }

            GetContactIdsByDeptIdsEvent event = new GetContactIdsByDeptIdsEvent();
            event.setDeptIds(groupIds);
            FindUserEvent findUserEvent = new FindUserEvent();
            findUserEvent.setEvent(event);
            QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(findUserEvent);
            List<User> list = (List<User>) queryResult.getResult();

            for (User item : list) {
                if (resultIds.indexOf(item.getUserid()) == -1 &&
                        selectConfig.getExistingContactIds().indexOf(item.getUserid()) == -1)
                    resultIds.add(item.getUserid());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(AddressBookConfig.ADDRESS_BOOK_COMPANY_KEY, resultIds);
            this.getActivity().setResult(100, intent);
            this.getActivity().finish();
        }
    }

    @UiThread
    void showToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(this.getActivity(), str,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    @UiThread
    void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
        super.getActivity().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 101 || data == null) {
            return;
        }
        User item = (User) data.getBundleExtra("select").getSerializable("selectVO");
        CompanyEntity companyEntity = findCompanyItem(item.getUserid(), item.getDeptCode());
        boolean isNew = false;
        if (companyEntity == null) {
            isNew = true;
            companyEntity = new CompanyEntity();

            companyEntity.setType(CompanyEntity.CompanyType.CONTACT);
            companyEntity.setId(item.getUserid());
            companyEntity.setParentId(item.getDeptId());
            companyEntity.setName(item.getName());
            companyEntity.setSignature(item.getSignature());
            companyEntity.setHeadImgUrl(item.getUrl());
            companyEntity.setAllContactCount(1);
            String[] levelArray = item.getDeptId().split("\\.");
            companyEntity.setLevel(levelArray.length);
        }
        CompanyEntity entity = null;
        if (isNew) {
            companySelects.add(companyEntity);
        } else {
            int i = mEntitiys.indexOf(companyEntity);
            if (i != -1)
                entity = mEntitiys.get(i);
        }
            /*当配置文件中处于不可选中状态时的操作*/
        if (selectConfig.getExistingContactIds().indexOf(item.getUserid()) != -1) {
            companyEntity.setUnSelect(true);
            companyEntity.setSelector(false);
            showToast(item.getName() + "不可选择");
        } else {
            companyEntity.setUnSelect(false);
            companyEntity.setSelector(true);

            if (selectConfig.getSelectType() == AddrbookSelectConfig.AddrbookSelectType.ONLYSELECT) {
                if (entity != null)
                    onlySelect(entity);
                else {
                    for (CompanyEntity company : mEntitiys) {
                        company.setSelector(false);
                    }
                }
            } else if (selectConfig.getSelectType() == AddrbookSelectConfig.AddrbookSelectType.SELECT) {
                if (entity != null)
                    selectCompanyData(entity);
            }
            notifyData();
        }
    }

    @Override
    public void onHandleRefreshDepartmentRespEvent(RefreshDepartmentRespEvent event) {
        if (event.getErrorCode() != ErrorCode.SUCCESS.getValue() &&
                (companySelects == null || companySelects.size() == 0)) {
            empty_tv.setText("通讯录加载失败,请检查网络");
        }
    }

    @UiThread
    @Override
    public void onHandleRefreshAddrbookRespEvent(RefreshAddrbookRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue() && (companySelects == null || companySelects.size() == 0)) {
            getMyData();
        } else {
            empty_tv.setText("通讯录加载失败,请检查网络");
        }
    }

    @Background
    void refreshContact() {
        CompanyEntity entity = findCompanyItem(userId, groupId);
        if (entity != null) {
            entity.setHeadImgUrl(Config.getInstance().getHeadImageUrl());
            notifyData();
        }
    }

    @Override
    public void onHandleModifyHeadPortraitRespEvent(ModifyHeadImgRespEvent event) {
        refreshContact();
    }

    @UiThread
    @Override
    public void onHandleGetAddressListRespEvent(GetAddressListRespEvent event) {
        LogUtil.i("aspire------onHandleGetAddressListRespEvent");
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
            synchronized (robj) {
                if (!refreshing) {
                    refreshing = true;
                    getMyData();
                }

            }
        }
    }

    private List<User> getFriendData() {
        FindFriendEvent getEvent = new FindFriendEvent();
        getEvent.setSort("initialKey");
        FindUserEvent event = new FindUserEvent();
        event.setEvent(getEvent);
        QueryResult queryResult = (QueryResult) uiEventHandleFacade.handle(event);
        return (List<User>) queryResult.getResult();
    }

    public void addFriend() {
        List<User> users = getFriendData();

        CompanyEntity friendGroup = findCompanyGroupItem("01");

        if (users != null && users.size() > 0) {

            for (User item : users) {

                CompanyEntity companyEntity = findCompanyItem(item.getUserid(), "01");
                if (companyEntity == null) {
                    companyEntity = new CompanyEntity();

                    companyEntity.setType(CompanyEntity.CompanyType.CONTACT);
                    companyEntity.setId(item.getUserid());

                    companyEntity.setAllContactCount(1);
                    companyEntity.setFriend(true);
                    companyEntity.setParentId("01");
                    companyEntity.setName(item.getName());
                    companyEntity.setSignature(item.getSignature());
                    companyEntity.setHeadImgUrl(item.getUrl());
                    companyEntity.setLevel(1);
                    companyEntity.setCorpId("01");

                    //选择框在非观看模式中的设置
                    /*当配置文件中处于不可选中状态时的操作*/
                    if (selectConfig.getExistingContactIds().indexOf(item.getUserid()) != -1) {
                        companyEntity.setUnSelect(true);
                        companyEntity.setSelector(false);
                    } else {
                        companyEntity.setUnSelect(false);
                        /*如果配置文件中存在，则为选中状态*/
                        if (selectConfig.getContactIds().indexOf(item.getUserid()) != -1) {
                            companyEntity.setSelector(true);
                        } else {
                            companyEntity.setSelector(false);
                        }
                    }


                    companySelects.add(companyEntity);
                }
                mEntitiys.add(1, companyEntity);

                if (friendGroup.isSelector()) {
                    companyEntity.setSelector(true);
                }
            }
        }
        notifyData();
    }

    private void addFriendGroup() {
        CompanyEntity group = new CompanyEntity();
        group.setParentId(root.getId());
        group.setType(CompanyEntity.CompanyType.GROUP);
        group.setId("01");
        group.setName("我的好友");
        group.setLevel(0);
        group.setCorpId("01");
        group.setFriend(true);

        List<User> users = getFriendData();
        group.setAllContactCount(users.size());

        mEntitiys.add(0, group);
        companySelects.add(group);
    }

    public void closeFriend() {
        List<User> users = getFriendData();
        for (User item : users) {
            CompanyEntity companyEntity = findCompanyItem(item.getUserid(), "01");
            int index = mEntitiys.indexOf(companyEntity);
            if (index != -1)
                mEntitiys.remove(index);
        }
        notifyData();
    }

    public void selectAllFriend(CompanyEntity companyEntity, boolean isSelect) {
        selectCompany(companyEntity, isSelect);
        List<User> users = getFriendData();
        for (User item : users) {
            CompanyEntity entity = findCompanyItem(item.getUserid(), "01");
            selectCompany(entity, isSelect);
        }
        notifyData();
    }

    public void selectFriend(CompanyEntity companyEntity, boolean isSelect) {
        selectCompany(companyEntity, isSelect);
        CompanyEntity friendGroup = findCompanyGroupItem("01");
        if (isSelect) {
            if (isAllSelectFriend()) {
                selectCompany(friendGroup, isSelect);
            }
        } else {
            selectCompany(friendGroup, isSelect);
        }
        notifyData();
    }

    private boolean isAllSelectFriend() {
        List<User> users = getFriendData();
        for (User item : users) {
            CompanyEntity companyEntity = findCompanyItem(item.getUserid(), "01");
            if (companyEntity != null && !companyEntity.isSelector()) {
                return false;
            }
        }
        return true;
    }

    private void selectCompany(CompanyEntity companyEntity, boolean isSelect) {
        try {
            int index = mEntitiys.indexOf(companyEntity);
            CompanyEntity companyEntity1 = mEntitiys.get(index);
            companyEntity1.setSelector(isSelect);
        } catch (Exception e) {

        }

        try {
            int index = companySelects.indexOf(companyEntity);
            CompanyEntity companyEntity2 = companySelects.get(index);
            companyEntity2.setSelector(isSelect);
        } catch (Exception e) {

        }

    }

//    private void appFeedback() {
//    OsgiServiceLoader.getInstance().getService(AppFeedBackService.class).invoke(AppFeedBackEntity.ADDRESS_ADDFRIEND);
//    }

}
