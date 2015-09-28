package com.aspirecn.corpsocial.common.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.aspirecn.corpsocial.common.eventbus.EventActivity;

/**
 * 通讯操作类
 *
 * @author chenziqiang
 */
public class ContactInformationUtil extends EventActivity {

    /**
     * 发微信点对点聊天
     *
     * @param context Activity
     * @param id      联系人id
     * @param name    联系人名称
     */
    public static void wxsms(Activity context, String id, String name) {
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
     * @param context Activity
     * @param phoneNo 电话号码
     */
    public static void sendMessage(Activity context, String phoneNo) {
        Uri phonenum = Uri.parse("smsto:" + phoneNo);
        Intent message = new Intent(Intent.ACTION_SENDTO, phonenum);
        message.putExtra("sms_body", "");
        context.startActivity(message);
    }

    /**
     * 打电话
     *
     * @param context Activity
     * @param phoneNo 电话号码
     */
    public static void callPhone(Activity context, String phoneNo) {
        Uri phonenum;
        phonenum = Uri.parse("tel:" + phoneNo);
        Intent iphone = new Intent(Intent.ACTION_CALL, phonenum);
        context.startActivity(iphone);
    }

    /**
     * 发送电子邮件
     */

    public static void sendEmail(Activity context, String emaildrees) {
        //设置对方邮件地址
        Uri uri = Uri.parse("mailto:" + emaildrees);
        //建立Intent对象,设置对象动作
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置标题内容
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        //设置邮件文本内容
        intent.putExtra(Intent.EXTRA_TEXT, "");
        //启动一个新的ACTIVITY,"Sending mail..."是在启动这个
        // ACTIVITY的等待时间时所显示的文字
        context.startActivity(Intent.createChooser(intent, "Sending E-mail..."));
    }
}
