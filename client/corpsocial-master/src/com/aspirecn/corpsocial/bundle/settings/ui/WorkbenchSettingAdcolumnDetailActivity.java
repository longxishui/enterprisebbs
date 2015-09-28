package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.settings.event.GetAdcolumnDetailEvent;
import com.aspirecn.corpsocial.bundle.settings.repository.entiy.AdcolumnEntiy;
import com.aspirecn.corpsocial.bundle.settings.utils.AdcolumnUtils;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.trinea.android.common.util.StringUtils;

import static android.content.Intent.ACTION_VIEW;

/**
 * Created by chenziqiang on 15-3-24.
 */
@EActivity(R.layout.setting_ui_adcolumn_detail_activity)
public class WorkbenchSettingAdcolumnDetailActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener {

    @ViewById(R.id.adcolumn_detail_img)
    ImageView img;
    @ViewById(R.id.adcolumn_detail_title)
    TextView title;
    @ViewById(R.id.adcolumn_detail_indate)
    TextView indata;
    @ViewById(R.id.adcolumn_detail_content)
    WebView content;

    private String viewUrl;

    @AfterViews
    void doAfterView() {

        Intent intent = getIntent();
        String id = intent.getStringExtra(AdcolumnUtils.ADCOLUMNID);

        GetAdcolumnDetailEvent event = new GetAdcolumnDetailEvent();
        event.setAdcolumnId(id);
        AdcolumnEntiy entiy = (AdcolumnEntiy) uiEventHandleFacade.handle(event);

        viewUrl = entiy.getViewUrl();
        showData(entiy);
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_adcolum_detail_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
    }

    @UiThread
    void showData(AdcolumnEntiy entiy) {
        ImageLoader.getInstance().displayImage(entiy.getBigIconUrl(), img);
        title.setText(entiy.getTitle());
        indata.setText(entiy.getPublishTime());
        content.loadDataWithBaseURL(null, entiy.getContent(), "text/html", "utf-8",
                null);

    }

    private void link() {
        if (!StringUtils.isBlank(viewUrl)) {
            Uri uri = Uri.parse(viewUrl);
            Intent intent = new Intent(ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        if (!StringUtils.isBlank(viewUrl))
            fab.build().setFirstButtonIcon(R.drawable.adcolumn_detail_link_icon).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link();
            }
        }).apply();
    }
}
