package com.aspirecn.corpsocial.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.View;

import com.aspiren.corpsocial.R;

public class CustomDialog {
    private static Dialog mProgressDialog;

    private static int isShowProgressDialog = 0;

    /**
     * 显示进度条对话框
     */
    public static void showProgeress(final Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(context, R.style.DialogCommon);
            View progress_dialog = View.inflate(context,
                    R.layout.custom_dialog_loading, null);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setContentView(progress_dialog);
            mProgressDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    closeProgress(context);
                }

            });
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
        ++isShowProgressDialog;
    }

    /**
     * 关闭进度条对话框
     */
    public static void closeProgress(Context context) {

        if (--isShowProgressDialog <= 0) {
            if (mProgressDialog != null) {
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    Log.e("BaseDialog", "对话框出错");
                }
                mProgressDialog = null;
            }
            isShowProgressDialog = 0;
        }
    }

    /**
     * 显示进度对话框
     *
     * @param context
     * @param cancelable 是否可手动取消
     */
    public static void showProgeress(final Context context, boolean cancelable) {
        showProgeress(context);
        mProgressDialog.setCancelable(false);
    }

}
