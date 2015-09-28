package com.aspirecn.corpsocial.bundle.settings.presenter.impl;


import android.content.Context;
import android.content.SharedPreferences;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.event.RefreshAddrbookRespEvent;
import com.aspirecn.corpsocial.bundle.common.uitils.UserUtil;
import com.aspirecn.corpsocial.bundle.settings.event.ModifyHeadImgRespEvent;
import com.aspirecn.corpsocial.bundle.settings.presenter.WorkbenchSettingPresenter;
import com.aspirecn.corpsocial.bundle.settings.view.WorkbenchSettingView;
import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingItem;
import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingViewModel;
import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.ErrorCode;
import com.aspirecn.corpsocial.common.util.AppUtil;
import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspiren.corpsocial.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by chenziqiang on 15-8-24.
 */
public class WorkbenchSettingPresenterImpl implements WorkbenchSettingPresenter {

    private WorkbenchSettingViewModel viewModel;
    private WorkbenchSettingView view;
    private User user;
    private Context context;

    public WorkbenchSettingPresenterImpl(WorkbenchSettingView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void doAfterViews() {
        viewModel = AssetsUtils.getInstance().read(AspirecnCorpSocial.getContext(), "settings.json", WorkbenchSettingViewModel.class);
        SharedPreferences sharedPre = context.getSharedPreferences(
                Constant.SHAREDPREFE_NAME_OAWEIXIN, Context.MODE_PRIVATE);
        String userId = sharedPre.getString("user_id", "null");
        user = UserUtil.getUser(userId);
        Collections.sort(viewModel.settingItems);
        viewModel.settingItems = compositeData(viewModel.settingItems);
        viewModel = getShowwingSelect(viewModel);
        view.initView(viewModel.settingItems);

    }

    @Override
    public void updateVersion() {

    }

    /**
     * 去除要隐藏的选项
     * @param vm
     * @return
     */
    private  WorkbenchSettingViewModel getShowwingSelect(WorkbenchSettingViewModel vm){
        for(int i=0; i<vm.settingItems.size();i++){
            if(vm.settingItems.get(i).hide){
                vm.settingItems.remove(i);
            }
        }
        return vm;
    }

    @Override
    public void onHandleModifyHeadPortraitRespEvent(ModifyHeadImgRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue())
            notifyHeadPor();
    }

    @Override
    public void onHandleRefreshAddrbookRespEvent(RefreshAddrbookRespEvent event) {
        if (event.getErrorCode() == ErrorCode.SUCCESS.getValue())
            notifyHeadPor();
    }

    /**
     * 数据合并
     */
    private List<WorkbenchSettingItem> compositeData(List<WorkbenchSettingItem> list) {
        WorkbenchSettingItem setHead = new WorkbenchSettingItem();
        setHead.name = user.getName();
        setHead.code = 0;
        setHead.imgSrc = user.getUrl();
        setHead.label = user.getSignature();
        setHead.jumpImg = true;

        WorkbenchSettingItem setExit = new WorkbenchSettingItem();
        setExit.name = context.getResources().getString(R.string.workbench_setting_exit);
        setExit.code = -1;
        setExit.imgSrc = "7f020229";
        setExit.jumpImg = false;
        setExit.iconPath="EXIT";


        for (WorkbenchSettingItem item : list) {
            if (item.code == 5) {
                item.label = "当前版本:" + AppUtil.getAppVersionInfo().name;
                break;
            }

        }
//        for (WorkbenchSettingItem item : list) {
//            if (item.code == 1 && Config.getInstance().isVisitorMode()) {
//                list.remove(item);
//                break;
//            }
//        }
        WorkbenchSettingItem space = new WorkbenchSettingItem();
        space.code = -10;

        list.add(0, space);
        list.add(0, setHead);
        list.add(0, space);

        list.add(space);
        list.add(setExit);
        return list;
    }

    private void notifyHeadPor() {
        SharedPreferences sharedPre = context.getSharedPreferences(
                Constant.SHAREDPREFE_NAME_OAWEIXIN, Context.MODE_PRIVATE);
        String userId = sharedPre.getString("user_id", "null");
        user = UserUtil.getUser(userId);
        WorkbenchSettingItem setHead = viewModel.settingItems.get(1);
        viewModel.settingItems.remove(setHead);
        setHead.name = user.getName();
        setHead.code = 0;
        setHead.imgSrc = user.getUrl();
        setHead.label = user.getSignature();
        setHead.jumpImg = true;
        viewModel.settingItems.add(1, setHead);
        view.initView(viewModel.settingItems);
    }

}
