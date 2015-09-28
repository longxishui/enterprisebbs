package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * 联系人通讯操作类
 *
 * @author chenziqiang
 */
public class AddrbookUtil {

    /**
     * 发微信
     *
     * @param id   联系人id
     * @param name 联系人名称
     */
    public static void wxsms(Context context, String id, String name) {
        Intent intent = new Intent();
        // 传递会话对象
        Bundle bundle = new Bundle();
        // bundle.putSerializable("chatEntity", chatEntity);
        bundle.putString("chatId", id);
        bundle.putString("chatName", name);
        bundle.putInt("chatType", 0);
        intent.putExtras(bundle);

        intent.setClass(context,
                com.aspirecn.corpsocial.bundle.im.ui.ChatActivity_.class);

        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param phoneNo 电话号码
     */
    public static void sms(Context context, String phoneNo) {
        Uri phonenum = Uri.parse("smsto:" + phoneNo);
        Intent message = new Intent(Intent.ACTION_SENDTO, phonenum);
        message.putExtra("sms_body", "");
        context.startActivity(message);
    }

    /**
     * 打电话
     *
     * @param phoneNo 电话号码
     */
    public static void phone(Context context, String phoneNo) {
        Uri phonenum;
        phonenum = Uri.parse("tel:" + phoneNo);
        Intent iphone = new Intent(Intent.ACTION_CALL, phonenum);
        context.startActivity(iphone);
    }
}
