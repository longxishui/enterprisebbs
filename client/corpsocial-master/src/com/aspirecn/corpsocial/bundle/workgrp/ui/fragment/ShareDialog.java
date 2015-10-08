package com.aspirecn.corpsocial.bundle.workgrp.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspiren.corpsocial.R;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShareDialog extends Dialog {

	private Context mContext;
    private String appid;
    private String textTitle;
    private String textcontent;
    private String imageUrl;
    private boolean sendFriend;
    public View.OnClickListener weixinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            IWXAPI api = WXAPIFactory.createWXAPI(mContext, "wxd9e637fe30c9280e");
            boolean sendFiend = view.getTag().equals("0");

//            WXWebpageObject webpage = new WXWebpageObject();
//            webpage.webpageUrl = "http://baidu.com";
//            WXMediaMessage msg = new WXMediaMessage();
//
//            msg.title = "title";
//            msg.description = "今晚打老虎";
//            Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(),
//                    R.drawable.app_icon);
//            msg.setThumbImage(thumb);


//            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
//                    R.drawable.app_icon);
//            WXImageObject wxImageObject = new WXImageObject(bmp);
            WXTextObject wxTextObject = new WXTextObject(textcontent);
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = wxTextObject;
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 60, 60, true);
//            bmp.recycle();
//            msg.thumbData = bmpToByteArray(thumbBmp, true);
//                msg.title = textcontent;
//                msg.description = textcontent;

//            msg.setThumbImage(thumb);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "img"+System.currentTimeMillis();
                req.message = msg;
                req.scene = sendFiend?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
        }
    };
    private View.OnClickListener qqClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Tencent mTencent = Tencent.createInstance("1104846556", mContext.getApplicationContext());
            Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, textTitle);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  textcontent);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "ICT生态圈");
            if(view.getTag().equals("0")){
                mTencent.shareToQQ((Activity)mContext, params, null);
            }else{
                mTencent.shareToQzone((Activity) mContext, params, null);
            }
        }
    };
	public ShareDialog(Context context,String appid,String textContent,String title,String imageUrl) {
		super(context, R.style.MyDialog);
		mContext = context;
        this.appid = appid;
        this.textcontent = textContent;
        this.textTitle = title;
        this.imageUrl = imageUrl;
		init();
	}

	private void init() {
		// 设置它的ContentView
		setContentView(R.layout.common_share_dialog);
        TextView textView = (TextView) findViewById(R.id.common_share_dialog_tv_btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialog.this.hide();
            }
        });
        GridView gridView = (GridView) findViewById(R.id.common_share_dialog_gv);
        List<ShareBean> shareBeans = new ArrayList<ShareBean>();
        shareBeans.add(new ShareBean("微信朋友圈",ShareBean.WEIXIN_PYQ));
        shareBeans.add(new ShareBean("微信好友",ShareBean.WEIXIN_PY));
//        shareBeans.add(new ShareBean("QQ好友",ShareBean.QQ));
//        shareBeans.add(new ShareBean("QQ空间",ShareBean.QQ_ZONE));
        gridView.setAdapter(new MGridViewAdapter(mContext,shareBeans));
	}

   class MGridViewAdapter extends BaseAdapter{
       private List<ShareBean> shareBeanList;
       private Context mContext;
       public MGridViewAdapter(Context context,List<ShareBean> shareBeanList){
            this.shareBeanList = shareBeanList;
           this.mContext = context;
       }

       @Override
       public int getCount() {
           return shareBeanList.size();
       }

       @Override
       public Object getItem(int i) {
           return shareBeanList.get(i);
       }

       @Override
       public long getItemId(int i) {
           return i;
       }

       @Override
       public View getView(int i, View convertview, ViewGroup viewGroup) {
           View view = View.inflate(mContext,R.layout.common_share_dialog_gv_item,null);
           ShareBean shareBean = shareBeanList.get(i);
           ImageView appIcon = (ImageView) view.findViewById(R.id.common_share_dialog_gv_item_iv);
           TextView appInfo = (TextView) view.findViewById(R.id.common_share_dialog_gv_item_tv);
           appInfo.setText(shareBean.getName());
            switch (shareBean.getShareApp()){
                case ShareBean.WEIXIN_PY:
                    appIcon.setImageResource(R.drawable.common_share_dialog_weixinpy_selector);
                    appIcon.setTag("0");
                    appIcon.setOnClickListener(weixinClickListener);
                    break;
                case ShareBean.WEIXIN_PYQ:
                    appIcon.setTag("1");
                    appIcon.setImageResource(R.drawable.common_share_dialog_weixinpyq_selector);
                    appIcon.setOnClickListener(weixinClickListener);
                    break;
                case ShareBean.QQ:
                    appIcon.setImageResource(R.drawable.common_share_dialog_qq_selector);
                    appIcon.setTag("0");
                    appIcon.setOnClickListener(qqClickListener);
                    break;
                case ShareBean.QQ_ZONE:
                    appIcon.setImageResource(R.drawable.common_share_dialog_qzone_selector);
                    appIcon.setTag("1");
                    appIcon.setOnClickListener(qqClickListener);
                    break;
            }
           return view;
       }
   }
    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}