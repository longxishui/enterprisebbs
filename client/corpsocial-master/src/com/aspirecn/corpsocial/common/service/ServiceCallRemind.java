//package com.aspirecn.corpsocial.common.service;
//
//import java.util.List;
//
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.IBinder;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.aspirecn.corpsocial.bundle.addrbook.repository.ContactDao;
//import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
//import com.aspirecn.corpsocial.common.config.Config;
//import com.aspiren.corpsocial.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
//public class ServiceCallRemind extends Service {
//    private static final String TAG = "ServiceCallRemind";
//    private TelephonyManager tm;
//    private MyPhoneListener listener;
//    /**
//     * 显示的布局
//     */
//    private View view;
//    private TextView tvRemindName;
//    private TextView tvRemindDept;
//    private TextView tvRemindPhoneNumber;
//    private ImageView ivRemindPhoto;
//    private Context context = null;
//    // 系统的窗体管理者
//    private WindowManager wm;
//    private CallReceive callReceive;
//    /**
//     * 联系人
//     */
//    private ContactBriefVO contactBriefVO;
//    // private String comingNumber;
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        System.out.println("启动电话弹窗监听");
//        context = getApplicationContext();
//
//        // Toast.makeText(getApplicationContext(), "启动服务注册监听来电",
//        // Toast.LENGTH_SHORT).show();
//        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        listener = new MyPhoneListener();
//        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.setPriority(10000);
//        intentFilter.addAction(Intent.ACTION_CALL);
//        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
//        intentFilter.addAction(Intent.ACTION_CALL_BUTTON);
//        callReceive = new CallReceive();
//        registerReceiver(callReceive, intentFilter);
//        super.onCreate();
//    }
//
//
//    private class CallReceive extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String imei = tm.getSubscriberId();
//            /** 设置来电弹窗是否开启 */
//            if (!Config.getInstance().isCommingCall()) {
//                return;
//            }
//            if (!TextUtils.isEmpty(imei)) {
//                String callNumber = getResultData();
//                System.out.println("拨打电话了:" + callNumber);
//                if (!TextUtils.isEmpty(callNumber) && view == null) {
//                    System.out.println("显示号码");
//                    callNumber = callNumber.replace("+86", "");
//                    ContactDao contactDao = new ContactDao();
//                    List<ContactBriefVO> contacts = contactDao.findByKeyWord(Config.getInstance().getUserId(), callNumber);
//                    if (contacts != null && contacts.size() > 0) {
//                        contactBriefVO = contacts.get(0);
//                        showLoaction(contactBriefVO, callNumber);
//                    } else {
//                        showLoaction(null, callNumber);
//                        ;
//                    }
//
//                }
//
//            }
//        }
//
//    }
//
//    private class MyPhoneListener extends PhoneStateListener {
//
//        // 当电话的呼叫状态发生改变的时候 调用的方法.
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            switch (state) {
//                // 空闲状态.
//                case TelephonyManager.CALL_STATE_IDLE:
//                    if (view != null) {
//                        wm.removeView(view);
//                        view = null;
//                    }
//                    break;
//                // 响铃状态
//                case TelephonyManager.CALL_STATE_RINGING:
//                    /** 设置来电弹窗是否开启 */
//                    if (!Config.getInstance().isCommingCall()) {
//                        return;
//                    }
//                    System.out.println("监听状态：CALL_STATE_RINGING");
//                    // 根据号码和短号查询
//                    if (!TextUtils.isEmpty(incomingNumber) && view == null) {
//                        // String number = SuiteApplication.get
//                        // comingNumber=incomingNumber;
//                        // if(!TextUtils.isEmpty(number)){
//                        // incomingNumber=number;
//                        // }
//                        // if (contact != null) {
//                        incomingNumber = incomingNumber.replace("+86", "");
//                        ContactDao contactDao = new ContactDao();
//                        List<ContactBriefVO> contacts = contactDao.findByKeyWord(Config.getInstance().getUserId(), incomingNumber);
//                        if (contacts != null && contacts.size() > 0) {
//                            contactBriefVO = contacts.get(0);
//                            showLoaction(contactBriefVO, incomingNumber);
//                        } else {
//                            showLoaction(null, incomingNumber);
//                            ;
//                        }
//                    }
//                    break;
//                /** 接听状态 */
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        listener = null;
//        tm = null;
//        unregisterReceiver(callReceive);
//        System.out.println("来去电监听结束");
//    }
//
//    /**
//     * 弹出窗口
//     */
//    private void showLoaction(ContactBriefVO baseContact, String number) {
//        try {
//            wm = (WindowManager) context
//                    .getSystemService(Context.WINDOW_SERVICE);
//            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//            params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
//            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
//            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//            // params.height =300;
//            params.gravity = Gravity.LEFT | Gravity.TOP;
//            params.x = 0;
//            params.y = 0;
//            // <span style="white-space: pre;"> </span>params.format =
//            // PixelFormat.RGBA_8888;
//            view = LayoutInflater.from(getApplicationContext()).inflate(
//                    R.layout.common_calls_to_remind, null);
//            tvRemindDept = (TextView) view.findViewById(R.id.tv_remind_dept);
//            tvRemindName = (TextView) view.findViewById(R.id.tv_remind_name);
//            tvRemindPhoneNumber = (TextView) view
//                    .findViewById(R.id.tv_remind_phone_number);
//            ivRemindPhoto = (ImageView) view
//                    .findViewById(R.id.iv_call_remind_photo);
//            // 点击隐藏
//            view.findViewById(R.id.rl_remove).setOnTouchListener(
//                    new OnTouchListener() {
//
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                if (view != null) {
//                                    wm.removeView(view);
//                                    view = null;
//                                }
//                            }
//                            return true;
//                        }
//                    });
//            if (baseContact != null) {
//                String departName = baseContact.getDeptName();
//                tvRemindDept.setText(departName);
//                tvRemindName.setText(baseContact.getName());
//                tvRemindPhoneNumber.setText(number);
//                ivRemindPhoto.setBackgroundResource(R.drawable.addrbook_contact);
////                ImageDownloadUtil.INSTANCE.showImage(baseContact.getHeadImageUrl(), "contact", ivRemindPhoto);
//                ImageLoader.getInstance().displayImage(baseContact.getHeadImageUrl(), ivRemindPhoto);
//            } else {
//                ivRemindPhoto.setImageResource(R.drawable.addrbook_contact);
//                tvRemindName.setText("陌生号码");
//                tvRemindDept.setVisibility(View.GONE);
//                if (TextUtils.isEmpty(number)) {
//                    tvRemindPhoneNumber.setText("未知号码");
//                } else {
//                    tvRemindPhoneNumber.setText(number);
//                }
//            }
//
//            wm.addView(view, params);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//    }
//
//}
