package com.aspirecn.corpsocial.common.ui.component.safewebviewbridge;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.aspirecn.corpsocial.common.AspirecnCorpSocial;
import com.aspirecn.corpsocial.common.util.DownloadAsyncTask;
import com.aspirecn.corpsocial.common.util.FileUtils;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspirecn.corpsocial.common.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadTask extends DownloadAsyncTask {
    private String fileName;
    private JsCallback jsCallback;
    private Context context;

    public JsCallback getJsCallback() {
        return jsCallback;
    }

    public void setJsCallback(JsCallback jsCallback) {
        this.jsCallback = jsCallback;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        if (jsCallback != null) {
            try {
                jsCallback.apply("OK");
            } catch (Exception e) {
                LogUtil.e("下载结束，调用JSCallback失败。", e);
            }
        }

        if (result != null && result.length() > 0) {
            Intent intent;
            intent = FileUtils.openFile(result);

            if (intent != null) {
                AspirecnCorpSocial.getContext().startActivity(intent);
            }
        } else {
            showToastMessage("下载失败，请稍候再试。");
        }
    }

    private void showToastMessage(String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // updateProgress(values[0]);
    }

    @Override
    public String getFileName(String urlString) {
        if (!StringUtils.isBlank(fileName)) {
            return fileName;
        }

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String raw = connection.getHeaderField("Content-Disposition");
            // raw = "attachment; filename=abc.jpg"
            if (raw != null && raw.indexOf("=") != -1) {
                String[] fileNames = raw.split("=");
                if (fileNames.length >= 2) {
                    fileName = fileNames[fileNames.length - 1];
                }
            }

            if (StringUtils.isBlank(fileName)) {
                fileName = super.getFileName(urlString);
            }

            return fileName.trim();
        } catch (Exception e) {
            showToastMessage("无法获取下载文件的文件名。");
            LogUtil.e("无法获取下载文件的文件名。", e);
        }
        return "";
    }
}
