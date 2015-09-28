/**
 * Summary: 异步回调页面JS函数管理对象
 * Version 1.0
 * Date: 13-11-26
 * Time: 下午7:55
 * Copyright: Copyright (c) 2013
 */

package com.aspirecn.corpsocial.common.ui.component.safewebviewbridge;

import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsCallback {
    private static final String CALLBACK_JS_FORMAT = "javascript:HostApp.callback(%d, %d %s);";
    private int index;
    private WebView webView;
    private int isPermanent;

    public JsCallback(WebView view, int index) {
        this.index = index;
        this.webView = view;
    }

    public void apply(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(",");
            boolean isStrArg = arg instanceof String;
            if (isStrArg) {
                List<String> l = new ArrayList<String>();
                l.add((String) arg);
                String jsonArray = new JSONArray(l).toString();
                sb.append(jsonArray.substring(1, jsonArray.length() - 1));
            } else if (arg == null) {
                sb.append("null");
            } else {
                sb.append(arg.toString());
            }
        }
        String execJs = String.format(CALLBACK_JS_FORMAT, index, isPermanent, sb.toString());
        Log.d("JsCallBack", execJs);
        webView.loadUrl(execJs);
    }

    public void setPermanent(boolean value) {
        isPermanent = value ? 1 : 0;
    }
}