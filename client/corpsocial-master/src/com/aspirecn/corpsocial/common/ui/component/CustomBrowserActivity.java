package com.aspirecn.corpsocial.common.ui.component;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspiren.corpsocial.R;

/**
 * 网页浏览器
 * Created by lihaiqiang on 2015/8/27.
 */
public class CustomBrowserActivity extends FragmentActivity implements
        ActionBarFragment.LifeCycleListener,
        View.OnClickListener {

    private ActionBarFragment mActionBarFragment;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private ImageView mGoBack;
    private ImageView mGoForward;
    private ImageView mRefresh;
    private String mWebTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_browser_activity);

        initView();
        openUrl(getIntent().getStringExtra("url"));
    }

    private void initView() {
        initActionbar();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView = (WebView) findViewById(R.id.web_view);
        mGoBack = (ImageView) findViewById(R.id.go_back);
        mGoForward = (ImageView) findViewById(R.id.go_forward);
        mRefresh = (ImageView) findViewById(R.id.refresh);
        mGoBack.setOnClickListener(this);
        mGoForward.setOnClickListener(this);
        mRefresh.setOnClickListener(this);

        initWebViewSetting();
    }

    private void initActionbar() {
        ActionBarFragment af = ActionBarFragment_.builder().build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, af).commit();
        af.setLifeCycleListener(this);
    }

    @Override
    public void onActionBarViewCreated(ActionBarFragment fab) {
        mActionBarFragment = fab;
//        mActionBarFragment.build().setBackButtonIcon(R.drawable.pubaccount_closeweb_selector).apply();
        setWebTitle(mWebTitle);
    }

    private void setWebTitle(String title) {
        mWebTitle = TextUtils.isEmpty(title) ? "" : title;
        if (mActionBarFragment != null) {
            mActionBarFragment.build().setTitle(mWebTitle).apply();
        }
    }

    private void initWebViewSetting() {
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setInitialScale(80);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        //webSettings.setSupportZoom(true);
    }

    private void openUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        //上一页
        if (v.getId() == R.id.go_back) {
            mWebView.goBack();
        }
        //下一页
        else if (v.getId() == R.id.go_forward) {
            mWebView.goForward();
        }
        //重载
        else if (v.getId() == R.id.refresh) {
            mWebView.reload();
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
            setWebTitle(mWebTitle);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            setWebTitle(mWebView.getTitle());
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

}
