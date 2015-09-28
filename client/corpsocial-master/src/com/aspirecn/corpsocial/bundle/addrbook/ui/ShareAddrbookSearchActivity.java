package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog.OnClickListener;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;

/**
 * 搜索会话
 *
 * @author lizhuo_a
 */
@EActivity(R.layout.addrbook_search_activity)
public class ShareAddrbookSearchActivity extends AddrbookSearchActivity {

    private Bundle mBundle;

    private CustomTipsDialog mDialog;

    @AfterInject
    void doAfterInject() {
        Intent intent = getIntent();
        mBundle = intent.getExtras();
    }

    @Override
    public void onClickItem(final User user) {
        // 屏蔽分享对象
        if (Config.getInstance().getUserId().equals(user.getUserid())) {
            return;
        }
        getDialog().setTitle("分享给" + user.getName());
        if ("image".equals(mBundle.getString("type"))) {
            getDialog().setContentImg(mBundle.getString("imgPath"));
        } else {
            getDialog().setContentText(mBundle.getString("content") + mBundle.getString("url"));
        }
        getDialog().setConfirmBtnText("分享");
        getDialog().setOnCancelBtnClickListener(new OnClickListener() {// 取消
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        getDialog().setOnConfirmBtnClickListener(new OnClickListener() {// 确定
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                share(user);
            }
        });
        getDialog().show();
    }

    /**
     * 分享
     */
    private void share(User user) {
        Intent intent = new Intent();
        mBundle.putString("chatId", user.getUserid());
        mBundle.putString("chatName", user.getName());
        mBundle.putInt("chatType", ChatType.P2P.value);
        intent.putExtras(mBundle);
        intent.setClass(this, com.aspirecn.corpsocial.bundle.im.ui.ShareChatActivity_.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取Dialog
     */
    private CustomTipsDialog getDialog() {
        if (mDialog == null) {
            mDialog = new CustomTipsDialog(this);
        }
        return mDialog;
    }

}
