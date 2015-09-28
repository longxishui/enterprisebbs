package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenziqiang on 15-7-8.
 */
@EActivity(R.layout.addrbook_group_choose)
public class AddrbookGroupChoose extends EventFragmentActivity {
    @ViewById(R.id.addrbook_groups_view)
    LinearLayout root;
    private List<User> users = new ArrayList<User>();

    @AfterViews
    void afterView() {
        User user = (User) getIntent().getExtras().getSerializable("user");

        ActionBarFragment fab = ActionBarFragment_.builder().
                title(user.getName()).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
//        fab.setLifeCycleListener(this);
        List<UserCorp> corpList = user.getCorpList();
        for (UserCorp corp : corpList) {
            User userEntiy = new User();
            userEntiy.setCorpId(corp.getCorpId());
            userEntiy.setCorpName(corp.getCorpName());
            userEntiy.setName(user.getName());
            userEntiy.setUserid(user.getUserid());
            userEntiy.setUrl(user.getUrl());
            users.add(userEntiy);
        }
        for (User user1 : users) {
            initCorpView(user1);
        }

    }

    private void per(String userId, String corpId) {
        Intent intent = new Intent();
        intent.putExtra("ContactId", userId);
        intent.putExtra("corpId", corpId);
        intent.setClass(AddrbookGroupChoose.this, AddrbookPersonalParticularsActivity_.class);
        startActivity(intent);
    }

    @UiThread
    void initCorpView(final User user) {
        View view = LayoutInflater.from(this).inflate(R.layout.addrbook_friend_item, null);
        ImageView ivHead = (ImageView) view.findViewById(R.id.addrbook_friend_item_head_iv);
        TextView tvTitle = (TextView) view.findViewById(R.id.addrbook_friend_item_title_tv);
        TextView tvLetter = (TextView) view.findViewById(R.id.addrbook_friend_item_catalog_tv);
        TextView tvCorp = (TextView) view.findViewById(R.id.addrbook_friend_item_copyposition_tv);

         /*显示头像*/
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(Constant.CORNER_RADIUS_PIXELS))
                .showImageForEmptyUri(R.drawable.im_main_tab_head_portrait_3)
                .showImageOnFail(R.drawable.im_main_tab_head_portrait_3)
                .build();
        ImageLoader.getInstance().displayImage(user.getUrl(), ivHead, options);

        tvTitle.setText(user.getName());
        tvLetter.setVisibility(View.GONE);
        tvCorp.setText(user.getCorpName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per(user.getUserid(), user.getCorpId());
            }
        });

        root.addView(view);
    }
}
