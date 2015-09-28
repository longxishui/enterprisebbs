package com.aspirecn.corpsocial.bundle.settings.view;

import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingItem;

import java.util.List;

/**
 * Created by chenziqiang on 15-8-25.
 */
public interface WorkbenchSettingView {

    void initView(List<WorkbenchSettingItem> settingItems);

    void showToast(String msg);
}
