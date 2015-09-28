package com.aspirecn.corpsocial.common.ui.component.safewebviewbridge;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InjectedWebViewClient extends WebViewClient {
    private final String TAG = "InjectedWebViewClient";
    private JsCallJava mJsCallJava;
    private WebViewClient mWebViewClient;

    @SuppressWarnings("rawtypes")
    public InjectedWebViewClient(String injectedName, Class injectedCls) {
        super();
        mJsCallJava = new JsCallJava(injectedName, injectedCls);
    }

    public InjectedWebViewClient(String injectedName, Class injectedCls,
                                 WebViewClient webViewClient) {
        super();
        mJsCallJava = new JsCallJava(injectedName, injectedCls);
        mWebViewClient = webViewClient;
    }

    private void initInjectWebClient(WebView view) {
        view.loadUrl(mJsCallJava.getPreloadInterfaceJS());
        Log.d(TAG, " inject js interface completely on progress ");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        initInjectWebClient(view);
        super.onPageStarted(view, url, favicon);
        if (mWebViewClient != null) {
            mWebViewClient.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        initInjectWebClient(view);
        super.onPageFinished(view, url);
        if (mWebViewClient != null) {
            mWebViewClient.onPageFinished(view, url);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        initInjectWebClient(view);
        super.onLoadResource(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        initInjectWebClient(view);
        super.doUpdateVisitedHistory(view, url, isReload);
    }

}
