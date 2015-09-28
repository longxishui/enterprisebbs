package com.aspirecn.corpsocial.bundle.settings.ui;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.setting_ui_message_activity)
public class WorkbenchSettingMessageActivity extends EventFragmentActivity {

    @ViewById(R.id.setting_ui_workbench_remind_switch)
    CheckBox remind;

    @ViewById(R.id.setting_ui_workbench_sound_switch)
    CheckBox sound;

    @ViewById(R.id.setting_ui_workbench_shake_switch)
    CheckBox shake;

    @ViewById(R.id.settings_ui_workbench_sound_rl)
    RelativeLayout soundRl;

    @ViewById(R.id.settings_ui_workbench_shake_rl)
    RelativeLayout shakeRl;

    @ViewById(R.id.setting_sound_shake_ll)
    LinearLayout soundShakeLl;

    @AfterViews
    void initial() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_newmessage_title)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();

        if (Config.getInstance().isNotify()) {
            remind.setChecked(true);
            soundShakeLl.setVisibility(View.VISIBLE);
            if (Config.getInstance().isNotifySound()) {
                sound.setChecked(true);
            } else {
                sound.setChecked(false);
            }
            if (Config.getInstance().isNotifyVibrate()) {
                shake.setChecked(true);
            } else {
                shake.setChecked(false);
            }
        } else {
            remind.setChecked(false);
            soundShakeLl.setVisibility(View.GONE);
        }
    }

    // 消息总开关
    @CheckedChange(R.id.setting_ui_workbench_remind_switch)
    void onRemindCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        if (!isChecked) {
            Config.getInstance().setNotify(false);
            Config.getInstance().setNotifySound(false);
            Config.getInstance().setNotifyVibrate(false);
            soundShakeLl.setVisibility(View.GONE);
        } else {
            Config.getInstance().setNotify(true);
            soundShakeLl.setVisibility(View.VISIBLE);
            Config.getInstance().setNotifySound(true);
            Config.getInstance().setNotifyVibrate(true);
            sound.setChecked(Config.getInstance().isNotifySound());
            shake.setChecked(Config.getInstance().isNotifyVibrate());
        }
    }

    // 声音开关
    @CheckedChange(R.id.setting_ui_workbench_sound_switch)
    void onSoundCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        Config.getInstance().setNotifySound(isChecked);
    }

    // 震动开关
    @CheckedChange(R.id.setting_ui_workbench_shake_switch)
    void onShakeCheckedChanged(CompoundButton checkBox, boolean isChecked) {
        Config.getInstance().setNotifyVibrate(isChecked);
    }

}
