package com.aspirecn.corpsocial.common.ui.component.safewebviewbridge;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewUtils {
    @SuppressWarnings("rawtypes")
    public static void injectJsCallJava(WebView webView, String jsName, Class hostJavaClass) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new InjectedWebViewClient(jsName, hostJavaClass));
        webView.setWebChromeClient(new InjectedChromeClient(jsName, hostJavaClass));
    }

    public static void injectJsCallJava(WebView webView, String jsName, Class hostJavaClass,
                                        WebViewClient webViewClient,
                                        WebChromeClient webChromeClient) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new InjectedWebViewClient(jsName, hostJavaClass, webViewClient));
        webView.setWebChromeClient(new InjectedChromeClient(jsName, hostJavaClass, webChromeClient));
    }

}
