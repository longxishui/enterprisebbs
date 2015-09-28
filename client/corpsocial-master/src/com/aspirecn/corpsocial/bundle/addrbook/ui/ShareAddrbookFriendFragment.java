package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;

/**
 * Created by yinghuihong on 15/8/28.
 */
@EFragment(R.layout.addrbook_friend_activity)
public class ShareAddrbookFriendFragment extends AddrbookFriendFragment {

    private Bundle bundle;

    private CustomTipsDialog mDialog;

    @AfterInject
    void doAfterInject() {
        Intent intent = getActivity().getIntent();
        bundle = intent.getExtras();
    }

    @Override
    public void onClickItem(final User user) {
        // 屏蔽分享对象
        if (Config.getInstance().getUserId().equals(user.getUserid())) {
            return;
        }
        getDialog().setTitle("分享给" + user.getName());
        if ("image".equals(bundle.getString("type"))) {
            getDialog().setContentImg(bundle.getString("imgPath"));
        } else {
            getDialog().setContentText(bundle.getString("content") + bundle.getString("url"));
        }
        getDialog().setConfirmBtnText("分享");
        getDialog().setOnCancelBtnClickListener(new CustomTipsDialog.OnClickListener() {// 取消
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        getDialog().setOnConfirmBtnClickListener(new CustomTipsDialog.OnClickListener() {// 确定
            @Override
            public void onClick(View view) {
                share(user);
                getDialog().dismiss();
            }
        });
        getDialog().show();
    }

    /**
     * 分享
     */
    private void share(User user) {
        Intent intent = new Intent();
        bundle.putString("chatId", user.getUserid());
        bundle.putString("chatName", user.getName());
        bundle.putInt("chatType", ChatEntity.ChatType.P2P.value);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), com.aspirecn.corpsocial.bundle.im.ui.ShareChatActivity_.class);
        startActivity(intent);
    }

    /**
     * 获取Dialog
     */
    private CustomTipsDialog getDialog() {
        if (mDialog == null) {
            mDialog = new CustomTipsDialog(getActivity());
        }
        return mDialog;
    }
}
