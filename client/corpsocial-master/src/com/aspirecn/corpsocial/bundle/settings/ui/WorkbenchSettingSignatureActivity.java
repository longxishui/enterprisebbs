package com.aspirecn.corpsocial.bundle.settings.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspirecn.corpsocial.common.util.StringUtils;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by chenziqiang on 15-4-28.
 */
@EActivity(R.layout.setting_ui_personalinfo_signature)
public class WorkbenchSettingSignatureActivity extends EventFragmentActivity implements ActionBarFragment.LifeCycleListener {

    @ViewById(R.id.personal_signature_etv)
    EditText signature;
    @ViewById(R.id.personal_signature_number_tv)
    TextView num;

    private int signatureSize = 0;

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_personalinfo_signature)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        fab.setLifeCycleListener(this);

        Intent intent = getIntent();
        String str = intent.getStringExtra("signature");
        if (!StringUtils.isEmpty(str))
            signature.setText(str);

        signature.setSelection(str.length());

    }

    //    @Click(R.id.personalinfo_submit)
    private void submit() {
        if (signatureSize >= 0 && signatureSize <= 30) {
            Intent intent = new Intent();
            String data = signature.getText().toString().trim();
            intent.putExtra("signature", data);
            this.setResult(-1, intent);
            finish();
        } else if (signatureSize > 30) {
            Toast.makeText(this, "字数超出限制", Toast.LENGTH_SHORT).show();
        }
    }

    @TextChange(R.id.personal_signature_etv)
    void changeNumber() {
        signatureSize = signature.getText().length();
        change(30 - signatureSize);
    }

    @UiThread
    void change(int number) {
        if (number >= 0) {
            num.setText(number + "");
        } else {
            num.setText("字数超出最大限制");
        }
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        fab.build().setFirstButtonText(R.string.task_update_submit);
        fab.build().setFirstButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        }).apply();
    }
}
