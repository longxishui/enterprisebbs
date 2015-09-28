package com.aspirecn.corpsocial.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspiren.corpsocial.R;

/**
 * 自定义进度条Dialog
 *
 * @author lihaiqiang
 */
public class CustomProgressBarDialog extends Dialog {

    private TextView mTitle;
    private TextView mPercent;
    private ProgressBar mProgressBar;

    public CustomProgressBarDialog(Context context) {
        super(context, R.style.MyDialog);
        setCancelable(false);
        setContentView(R.layout.widget_custom_progress_bar_dialog);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.widget_custom_progress_bar_title);
        mPercent = (TextView) findViewById(R.id.widget_custom_progress_bar_percent);
        mProgressBar = (ProgressBar) findViewById(R.id.widget_custom_progress_bar);
        mProgressBar.setMax(100);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        mPercent.setText(progress + "%");
    }

}
