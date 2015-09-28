package com.aspirecn.corpsocial.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aspiren.corpsocial.R;

/**
 * 自定义Dialog
 *
 * @author lihaiqiang
 */
public class CustomAlertDialog extends Dialog {

    private TextView mAlertMsgTV;
    private EditText mInputMsgET;
    private Button mBtn1BT;
    private View mBtn2Layout;
    private Button mBtn2BT;

    public CustomAlertDialog(Context context) {
        super(context, R.style.MyDialog);
        setCancelable(true);
        setContentView(R.layout.widget_custom_alert_dialog);
        initView();
    }

    private void initView() {
        mAlertMsgTV = (TextView) findViewById(R.id.widget_custom_alert_dialog_alert_msg_tv);
        mInputMsgET = (EditText) findViewById(R.id.widget_custom_alert_dialog_alert_input_msg_et);
        mBtn1BT = (Button) findViewById(R.id.widget_custom_alert_dialog_btn_1_bt);
        mBtn2Layout = findViewById(R.id.widget_custom_alert_dialog_btn_2_layout);
        mBtn2BT = (Button) findViewById(R.id.widget_custom_alert_dialog_btn_2_bt);

        mBtn2Layout.setVisibility(View.GONE);
    }

    public void setBtn1ClickListener(final View.OnClickListener listener) {
        mBtn1BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listener.onClick(arg0);
            }
        });
    }

    public void setBtn2ClickListener(final View.OnClickListener listener) {
        mBtn2BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                listener.onClick(arg0);
            }
        });
        mBtn2Layout.setVisibility(View.VISIBLE);
    }

    public TextView getAlertMsgTV() {
        return mAlertMsgTV;
    }

    public EditText getInputMsgET() {
        return mInputMsgET;
    }

    public void setAlertMsg(CharSequence charSequence) {
        mAlertMsgTV.setText(charSequence);
    }

    public Button getBtn1() {
        return mBtn1BT;
    }

    public void setBtn1Text(CharSequence charSequence) {
        mBtn1BT.setText(charSequence);
    }

    public Button getBtn2() {
        return mBtn2BT;
    }

    public void setBtn2Text(CharSequence charSequence) {
        mBtn2BT.setText(charSequence);
    }

}
