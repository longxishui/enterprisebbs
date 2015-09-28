package com.aspirecn.corpsocial.common.ui.component;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspirecn.corpsocial.common.ui.component.safewebviewbridge.WebViewUtils;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author lihaiqiang
 */
@EActivity(R.layout.component_common_webview_activity_1)
public class CommonWebActivity extends EventActivity {

    /**
     * 退出按钮
     */
    @ViewById(R.id.common_webview_back_tv)
    TextView mBackTv;

    @ViewById(R.id.common_webview_go_back_iv)
    ImageView mGoBackIv;

    @ViewById(R.id.common_webview_go_forward_iv)
    ImageView mGoForwardIv;

    @ViewById(R.id.common_webview_refresh_iv)
    ImageView mRefreshIv;

    @ViewById(R.id.common_webview_wv)
    WebView mWebView;

    @ViewById(R.id.common_webview_progress_bar)
    ProgressBar mProgressBar;

    String mTitle;
    String mUrl;

    @AfterInject
    void doAfterInfject() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mUrl = intent.getStringExtra("url");
    }

    @AfterViews
    void doAfterViews() {
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mBackTv.setText(mTitle);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
//        WebViewUtils.injectJsCallJava(mWebView, "HostApp", HostJsScope.class);
        mWebView.loadUrl(mUrl);
    }

    /**
     * 退出
     */
    @Click(R.id.common_webview_back_tv)
    void onClickQuitBtn() {
        finish();
    }

    /**
     * 上一页
     */
    @Click(R.id.common_webview_go_back_iv)
    void onClickGoBackBtn() {
        mWebView.goBack();
    }

    /**
     * 下一页
     */
    @Click(R.id.common_webview_go_forward_iv)
    void onClickGoForwardBtn() {
        mWebView.goForward();
    }

    /**
     * 刷新
     */
    @Click(R.id.common_webview_refresh_iv)
    void onClickRefreshBtn() {
        mWebView.reload();
    }

    /**
     * 加载页面
     */
    // private void load(WebView webView, String url) {
    // if (URLUtil.isNetworkUrl(url)) {
    // webView.loadUrl(url);
    // } else {
    // Toast.makeText(this, "输入的网址不正确", Toast.LENGTH_LONG).show();
    // }
    // }

    private class MyWebViewClient extends WebViewClient {

        // @Override
        // public void onLoadResource(WebView view, String url) {
        // Log.e("info", "onLoadResource url =" + url);
        // super.onLoadResource(view, url);
        // }

        // @Override
        // public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        // Log.e("info", "shouldOverrideKeyEvent event =" + event.getAction());
        // return super.shouldOverrideKeyEvent(view, event);
        // }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Log.e("info", "shouldOverrideUrlLoading url =" + url);
            // view.loadUrl(url);
            // return true;
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Log.e("info", "onPageStarted url =" + url);
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // Log.e("info", "onPageFinished url =" + url);
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // Log.e("info", "onProgressChanged newProgress = " + newProgress);
            mProgressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

    }

}
