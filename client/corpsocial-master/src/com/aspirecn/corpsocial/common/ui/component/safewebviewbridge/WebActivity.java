package com.aspirecn.corpsocial.common.ui.component.safewebviewbridge;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author meixuesong
 */
public class WebActivity extends Activity {

    //public static Map<String, Object> params = new HashMap<String, Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		webview = new WebView(this);
//		setContentView(webview);
//
//		WebSettings ws = webview.getSettings();
//		ws.setJavaScriptEnabled(true);
//		webview.setWebChromeClient(new InjectedChromeClient("HostApp",
//				HostJsScope.class));
//
//		// TODO: 替换为相应的网页
//		webview.loadUrl("file:///android_asset/test.html");
//		String string = "file:///android_asset/app/"
//				+ WebActivity.params.get("appId") + "/0/"
//				+ WebActivity.params.get("templateUrl");
//		webview.loadUrl(string);
    }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 100) {
//			// 联系人选择
//			StringBuilder names = new StringBuilder("");
//
//			Bundle bundle = data.getBundleExtra("ReData");
//			List<Contact> contacts = (List<Contact>) bundle
//					.getSerializable("ReData");
//			if (contacts != null && contacts.size() > 0) {
//				for (Contact contact : contacts) {
//					names.append(contact.toJson() + ", ");
//				}
//			}
//			String result = names.toString();
//			if (result.length() > 0) {
//				result = result.substring(0, result.length() - 2);
//			}
//			result = "[" + result + "]";
//
//			webview.loadUrl("javascript:selectedContactsCallback('" + result
//					+ "')");
//		}
//	}

}
