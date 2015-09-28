/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.im.ui;

import android.content.Intent;
import android.view.View;

import com.aspirecn.corpsocial.bundle.im.domain.Chat;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog;
import com.aspirecn.corpsocial.common.ui.widget.CustomTipsDialog.OnClickListener;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.EActivity;

/**
 * @author lizhuo_a
 */
@EActivity(R.layout.im_chat_activity)
public class ShareChatActivity extends ChatActivity {

    private CustomTipsDialog mDialog;

    @Override
    public void setSpeakPower(Chat chat, boolean isSpeak) {
        super.setSpeakPower(chat, isSpeak);
        if (isSpeak) {
            showSendMsg();
        } else {
            showToast("您可向群管理员申请发言权限");
        }
    }

    private void showSendMsg() {
        String type = getIntent().getStringExtra("type");
        String content = getIntent().getStringExtra("content");
        String url = getIntent().getStringExtra("url");
        if ("textconnection".equals(type)) {// 带连接文本
            sendTextMsg(content + " " + url);
        } else if ("text".equals(type)) {// 纯文本
            sendTextMsg(content);
        } else if ("connection".equals(type)) {// 纯连接
            sendTextMsg(url);
        } else if ("image".equals(type)) {// 图片
            String imgPath = getIntent().getStringExtra("imgPath");
            sendPictureMsg(imgPath);
        } else {
            //
        }
        showDialogTips();
    }

    private void showDialogTips() {
        getDialog().setViewStyle(CustomTipsDialog.BUTTON_LIST);
        getDialog().setStatusTipsText("已发送");

        getDialog().setBtn1Text("继续分享到其他");
        getDialog().setBtn1ClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                ShareChatActivity.this.finish();
            }
        });
        getDialog().setBtn2Text("留在" + getString(R.string.app_name));
        getDialog().setBtn2ClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                //FIXME
//				mBackTV.performClick();
                com.aspirecn.corpsocial.bundle.common.ui.JumpActivity_
                        .finishActivity(com.aspirecn.corpsocial.bundle.addrbook.ui.ShareAddrbookMainTabActivity_.class
                                .getName());
                Intent intent = new Intent(ShareChatActivity.this, com.aspirecn.corpsocial.bundle.common.ui.LoginActivity_.class);
                startActivity(intent);
                finish();
            }
        });
        getDialog().setBtn3Text("返回第三方应用");
        getDialog().setBtn3ClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                com.aspirecn.corpsocial.bundle.common.ui.JumpActivity_
                        .finishActivity(com.aspirecn.corpsocial.bundle.addrbook.ui.ShareAddrbookMainTabActivity_.class
                                .getName());
                ShareChatActivity.this.finish();
            }
        });
        getDialog().show();
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    private CustomTipsDialog getDialog() {
        if (mDialog == null) {
            mDialog = new CustomTipsDialog(this);
        }
        return mDialog;
    }

}
