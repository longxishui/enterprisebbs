package com.aspirecn.corpsocial.bundle.workgrp.ui.widget;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.workgrp.domain.BBSItem;
import com.aspirecn.corpsocial.bundle.workgrp.event.BBSDeleteEvent;
import com.aspirecn.corpsocial.bundle.workgrp.repository.entity.BBSReplyInfoEntity;
import com.aspirecn.corpsocial.bundle.workgrp.util.DeleteType;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspiren.corpsocial.R;

/**
 * 弹出复制框的dialog
 */
public class BBSDialog extends Dialog implements
        View.OnClickListener {
    private ImageButton close_btn;// 关闭按钮
    private RelativeLayout copyText;
    private RelativeLayout close, delete;
    private String copyContent;
    private TextView dialog_title;
    private BBSReplyInfoEntity replyInfo;
    private boolean unShow;
    private BBSItem bbsItem;

    public BBSDialog(Context context) {
        super(context);
    }

    public BBSDialog(Context context, int style, String copyContent) {
        super(context, style);
        this.copyContent = copyContent;
    }

    public BBSDialog(Context context, int style, BBSReplyInfoEntity replyInfoEntity, BBSItem bbsItem, boolean unShow, String copyContent) {
        super(context, style);
        this.replyInfo = replyInfoEntity;
        this.unShow = unShow;
        this.bbsItem = bbsItem;
        this.copyContent = copyContent;
    }

    public void setTitle(String title) {
        dialog_title.setText(title);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.workgrp_copy_dialog);
        dialog_title = (TextView) findViewById(R.id.workgrp_copy_dialog_title);
        close = (RelativeLayout) findViewById(R.id.workgrp_copy_dialog_close_quit_rl);
        delete = (RelativeLayout) findViewById(R.id.workgrp_copy_dialog_delete_quit_rl);
        copyText = (RelativeLayout) findViewById(R.id.workgrp_copy_dialog_close_copy_rl);
        close_btn = (ImageButton) findViewById(R.id.workgrp_copy_dialog_close_ib);
        close_btn.setOnClickListener(this);
        close.setOnClickListener(this);
        delete.setOnClickListener(this);
        copyText.setOnClickListener(this);
        if (unShow) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.workgrp_copy_dialog_close_quit_rl:// 关闭dialog
                this.dismiss();
                break;
            case R.id.workgrp_copy_dialog_close_copy_rl:// 复制文本
                ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("文字", copyContent);
                cmb.setPrimaryClip(cData);
                this.dismiss();
                break;
            case R.id.workgrp_copy_dialog_delete_quit_rl:// 删除评论
                UiEventHandleFacade.getInstance().handle(
                        new BBSDeleteEvent(DeleteType.REPLY,
                                replyInfo.getItemId(),
                                replyInfo.getId(), bbsItem.getBbsItemEntity()
                                .getGroupId()));

                this.dismiss();
                break;
            case R.id.workgrp_copy_dialog_close_ib:// 关闭dialog
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
