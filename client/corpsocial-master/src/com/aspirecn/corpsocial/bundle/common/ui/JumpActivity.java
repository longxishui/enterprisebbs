/**
 * @(#) LoginActivity.java Created on 2013-11-19
 * <p/>
 * Copyright (c) 2013 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.bundle.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

import cn.trinea.android.common.util.FileUtils;

/**
 * @author lizhuo_a
 */
@EActivity(R.layout.common_login_activity)
public class JumpActivity extends LoginActivity {

    public static HashMap<String, Activity> acts = new HashMap<String, Activity>();
    private Bundle bundle = new Bundle();
    private boolean isExit = false;

    public static void addActivity(Activity act) {
        acts.put(act.getClass().getName(), act);
    }

    public static void finishActivity(String name) {
        Activity act = acts.remove(name);
        if (act != null) {
            act.finish();
        }
    }

    @AfterInject
    void doAfterInject() {
        Intent intent = getIntent();
        // type:text,connection,textconnection,image;
        String type = null;
        String url = null;
        String title = null;
        String body = null;
        String content = null;
        String imgPath = null;
        // 获取数据
        url = intent.getStringExtra("url");
        // String sms_body = intent.getStringExtra("sms_body");
        title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        Parcelable p = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        //
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        //
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        //
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("http")) {
                body = text.substring(0, text.indexOf("http")).trim();
                if (TextUtils.isEmpty(url)) {
                    url = text.substring(text.indexOf("http"));
                }
            } else {
                body = text;
            }
        }
        //
        if (TextUtils.isEmpty(body)) {
            body = "";
        }
        //
        if (title.equals(body)) {
            content = title;
        } else if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
            content = title + "\n" + body;
        } else {
            content = title + body;
        }
        //
        if (p != null) {
            Uri uri = Uri.parse(p.toString());
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver()
                        .query(uri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                imgPath = cursor.getString(column_index);
            } catch (Exception e) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if (!FileUtils.isFileExist(imgPath)) {
                imgPath = "";
            }
        }
        //
        if (!TextUtils.isEmpty(url)) {
            if (!TextUtils.isEmpty(content)) {
                type = "textconnection";
            } else {
                type = "connection";
            }
        } else if (!TextUtils.isEmpty(content)) {
            type = "text";
        } else if (!TextUtils.isEmpty(imgPath)) {
            type = "image";
        } else {
            Toast.makeText(this,
                    "此内容无法分享到" + getString(R.string.app_name) + ",请分享其他内容",
                    Toast.LENGTH_LONG).show();
            isExit = true;
            finish();
        }
        // 封装数据
        bundle.putString("type", type);
        bundle.putString("url", url);
        bundle.putString("content", content);
        bundle.putString("imgPath", imgPath);
    }

    @Override
    void doLoginSuccess() {
        // super.doLoginSuccess();
        if (isExit) {
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(
                    this,
                    com.aspirecn.corpsocial.bundle.addrbook.ui.ShareAddrbookMainTabActivity_.class);
            startActivity(intent);
            finish();
        }
    }

}
