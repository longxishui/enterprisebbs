package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aspirecn.corpsocial.bundle.im.facade.Group;
import com.aspirecn.corpsocial.bundle.im.repository.entity.ChatEntity.ChatType;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog.OnClickListener;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EFragment;

/**
 * 我的群组
 *
 * @author chenziqiang
 */
@EFragment(R.layout.addrbook_contacts_activity)
public class ShareAddrbookGroupFragment extends AddrbookGroupFragment {

    private Bundle mBundle;

    private CustomTipsDialog mDialog;

    @AfterInject
    void doAfterInject() {
        Intent intent = getActivity().getIntent();
        mBundle = intent.getExtras();
    }

    @Override
    public void onClickItem(final Group group) {
        // super.onClickItem(group);
        getDialog().setTitle("分享到" + group.getName());
        String type = mBundle.getString("type");
        if ("image".equals(type)) {
            String imgPath = mBundle.getString("imgPath");
            getDialog().setContentImg(imgPath);
        } else {
            String url = mBundle.getString("url");
            String content = mBundle.getString("content");
            getDialog().setContentText(content + url);
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
                share(group);
                getDialog().dismiss();
            }
        });
        getDialog().show();
    }

    /**
     * 分享
     *
     * @param group
     */
    private void share(Group group) {
        Intent intent = new Intent();
        mBundle.putString("chatId", group.getGroupId());
        mBundle.putString("chatName", group.getName());
        mBundle.putInt("chatType", ChatType.GROUP.value);
        intent.putExtras(mBundle);
        intent.setClass(getActivity(), com.aspirecn.corpsocial.bundle.im.ui.ShareChatActivity_.class);
        startActivity(intent);
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    private CustomTipsDialog getDialog() {
        if (mDialog == null) {
            mDialog = new CustomTipsDialog(getActivity());
        }
        return mDialog;
    }
}
