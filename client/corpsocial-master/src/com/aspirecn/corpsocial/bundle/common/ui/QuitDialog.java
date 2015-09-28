package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.aspirecn.corpsocial.bundle.common.facade.QuitService;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.OsgiServiceLoader;
import com.aspiren.corpsocial.R;

public class QuitDialog extends Dialog {

    private Context mContext;

    public QuitDialog(Context context) {
        super(context, R.style.MyDialog);
        mContext = context;
        init();
    }

    private void init() {
        // 设置它的ContentView
        setContentView(R.layout.setting_ui_exit_dialog);

        // 关闭
        findViewById(R.id.setting_close_layout_rl).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        // 修改账号状态
                        Config.getInstance().setAccountStatus(true);
                        // 断开连接
                        disconnect();
                    }
                });

        // 退出账号
        findViewById(R.id.setting_exit_layout_rl).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        // 修改账号状态
                        Config.getInstance().setAccountStatus(false);
                        // 断开连接
                        disconnect();
                        // 跳转到登陆界面
                        mContext.startActivity(new Intent(
                                mContext,
                                LoginActivity_.class));
                    }
                });

    }

    /**
     * 断开连接
     *
     */
    private void disconnect() {
        OsgiServiceLoader instance = OsgiServiceLoader.getInstance();
        QuitService quitService = (QuitService) instance
                .getService(QuitService.class);
        quitService.invoke(null);
    }

}
