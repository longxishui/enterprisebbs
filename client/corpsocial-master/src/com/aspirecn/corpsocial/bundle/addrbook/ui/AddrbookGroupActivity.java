package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.view.View;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.bundle.im.ui.CreateGroupActivity_;
import com.aspirecn.corpsocial.bundle.im.ui.SearchChatActivity_;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by chenziqiang on 15-3-31.
 */
@EActivity(R.layout.addrbook_group_activity)
public class AddrbookGroupActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener {

    @AfterViews
    void doAfterView() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.addrbook_mycompany)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {

        fab.build().setFirstButtonIcon(R.drawable.actionbar_add_btn_bg_selector).apply();
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreateGroupBtn();
            }
        }).apply();

        fab.build().setSecondButtonIcon(R.drawable.actionbar_search_btn_bg_selector).apply();
        fab.build().setSecondButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearchBtn();
            }
        }).apply();
    }


    private void onClickCreateGroupBtn() {

        startActivity(new Intent(this, CreateGroupActivity_.class));
    }

    private void onClickSearchBtn() {
        Intent intent = new Intent(this, SearchChatActivity_.class);
        startActivity(intent);
    }
}
