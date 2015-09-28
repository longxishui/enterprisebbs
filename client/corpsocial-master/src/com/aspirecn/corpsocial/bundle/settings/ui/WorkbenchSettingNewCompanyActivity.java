package com.aspirecn.corpsocial.bundle.settings.ui;

import android.app.ProgressDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.PropertyInfo;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2015/7/2.
 */
@EActivity(R.layout.setting_ui_workbench_newcompany_activity)
public class WorkbenchSettingNewCompanyActivity extends EventFragmentActivity {

    private static final String PARAMETERS = "telephone=%s&creatorId=%s";

    ProgressDialog mProgressDialog;

    @ViewById(R.id.web_view)
    WebView mWebView;

    @ViewById(R.id.tv_retry)
    TextView mTvRetry;

    @Click(R.id.tv_retry)
    void retry() {
        mWebView.setVisibility(View.VISIBLE);
        mTvRetry.setVisibility(View.GONE);
        mWebView.reload();
    }


    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_newcompany)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.workbench_setting_newcompany_loading));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(View.GONE);
                mTvRetry.setVisibility(View.VISIBLE);
            }
        });
        load();
    }

    private void load() {
        mProgressDialog.show();
        String url = PropertyInfo.getInstance().getString("web_url");
        mWebView.loadUrl(String.format(url + PARAMETERS, Config.getInstance().getUserName(), Config.getInstance().getUserId()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
