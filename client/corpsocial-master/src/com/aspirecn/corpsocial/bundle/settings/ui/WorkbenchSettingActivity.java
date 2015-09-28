package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.facade.AppStartPath;
import com.aspirecn.corpsocial.bundle.common.ui.QuitDialog;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.presenter.WorkbenchSettingPresenter;
import com.aspirecn.corpsocial.bundle.settings.presenter.impl.WorkbenchSettingPresenterImpl;
import com.aspirecn.corpsocial.bundle.settings.view.WorkbenchSettingView;
import com.aspirecn.corpsocial.bundle.settings.viewmodel.WorkbenchSettingItem;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 工作台设置
 *
 * @author wangdeng
 */
@EActivity(R.layout.setting_ui_workbench_activity)
public class WorkbenchSettingActivity extends EventFragmentActivity implements
        WorkbenchSettingView {

    @ViewById(R.id.setting_workbench_list)
    ListView mListView;

    private WorkbenchSettingPresenter mPresenter = new WorkbenchSettingPresenterImpl(this, this);

    @AfterViews
    void doAfterViews() {
        EventListenerSubjectLoader.getInstance().registerListener(mPresenter);
        mPresenter.doAfterViews();

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_title)).
                navigation(true).startPath(AppStartPath.SETTING.START_PATH).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();


    }

    // 设置个人信息
    private void settingPersonalInfo() {
        startActivity(new Intent(this, WorkbenchSettingPersonalInfoActivity_.class));
    }

    // 修改密码
    private void updatePwd() {
        Intent intent = new Intent();
        intent.setClass(
                this,
                WorkbenchSettingUpdateActivity_.class);
        this.startActivity(intent);
    }

    // 新的企业
    private void newCompany() {
        Intent intent = new Intent();
        intent.setClass(
                this,
                WorkbenchSettingNewCompanyActivity_.class);
        this.startActivity(intent);
    }

    // 设置新消息
    private void newMessage() {
        Intent intent = new Intent();
        intent.setClass(
                this,
                WorkbenchSettingMessageActivity_.class);
        this.startActivity(intent);
    }

    // 关于工作圈
    private void about() {
        Intent intent = new Intent();
        intent.setClass(
                this,
                WorkbenchSettingAboutActivity_.class);
        this.startActivity(intent);
    }

    // 退出系统
    private void exits() {
        new QuitDialog(this).show();
    }

    // 检查版本
    @Background
    void updateVersion() {
        mPresenter.updateVersion();
    }

    //关于移动
    private void aboutMobile() {
        Intent intent = new Intent(this, WorkbenchSettingAdcolumnActivity_.class);
        this.startActivity(intent);
    }

    //九宫格
    private void nineGrid() {
//        Intent intent = new Intent(this, WorkBenchSettingUpdateTypeActivity_.class);
//        this.startActivity(intent);
    }

    private void listItemClickListener(int code) {
        switch (code) {
            case 0:
                settingPersonalInfo();
                break;
            case 1:
                updatePwd();
                break;
            case 2:
                newCompany();
                break;
            case 3:
                newMessage();
                break;
            case 4:
                about();
                break;
            case 5:
                updateVersion();
                break;
            case 6:
                nineGrid();
                break;
            case 7:
                aboutMobile();
                break;
            case -1:
                exits();
                break;

        }
    }


    @Override
    @UiThread
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        EventListenerSubjectLoader.getInstance().unregisterListener(mPresenter);
        super.onDestroy();
    }

    @Override
    @UiThread
    public void initView(final List<WorkbenchSettingItem> settingItems) {
        WorkbenchSettingAdapter adapter = new WorkbenchSettingAdapter(WorkbenchSettingActivity.this, settingItems);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listItemClickListener(settingItems.get(position).code);
            }
        });
        adapter.notifyDataSetChanged();
    }


}
