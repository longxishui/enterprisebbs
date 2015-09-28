package com.aspirecn.corpsocial.bundle.addrbook.ui.company;

import android.content.Intent;
import android.view.View;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

/**
 * Created by chenziqiang on 15-5-6.
 */
@EActivity(R.layout.addrbook_company_activity)
public class CompanyActivty extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener {

    /*在这里，我们通过碎片管理器中的id，就是每个碎片的名称，来获取对应的fragment*/
    @FragmentById(R.id.address_book)
    CompanyFragment mCompanyFragment;

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.addrbook_choose_contacts)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);
    }

    /*在fragment的管理类中，我们要实现这部操作，而他的主要作用是，当这个activity回传数据到
     这里碎片管理器下面的fragnment中时，往往会经过这个管理器中的onActivityResult的方法。
     在实际开发中，往往会出现在子的fragment中，还会去管理下一层的多个碎片的管理,做法还是一样的*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*然后在碎片中调用重写的onActivityResult方法*/
        mCompanyFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonIcon(R.drawable.actionbar_search_btn_bg_selector);
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (mCompanyFragment.getSelectConfig().getGroupIds().size() > 0)
                    intent.putExtra("corpId", mCompanyFragment.getSelectConfig().getGroupIds().get(0));
                else
                    intent.putExtra("corpId", "null");
                intent.setClass(CompanyActivty.this, AddrbookSelectSearchActivity_.class);
                startActivityForResult(intent, 101);
            }
        }).apply();
    }


}
