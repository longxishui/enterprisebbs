package com.aspirecn.corpsocial.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspiren.corpsocial.R;

/***
 * 自定义提示Dialog
 *
 * @author lihaiqiang
 */
public class CustomTipsDialog extends Dialog {

    public static final int SHOW_CONTENT = 0;
    public static final int BUTTON_LIST = 1;
    private Context mContext;
    private int VIEW_STYLE = SHOW_CONTENT;

    // 内容展示
    private View mContentShowLayout;
    private TextView mTitleTV;
    private TextView mContentTV;
    private ImageView mContentIV;
    private Button mCancelBT;
    private Button mConfirmBT;
    // 按钮列表
    private View mBtnListLayout;
    private TextView mStatusTipsTV;
    private Button mBtn1BT;
    private Button mBtn2BT;
    private Button mBtn3BT;

    private boolean mIsNoTitle = false;
    private int mContentTextVisiable = View.VISIBLE;
    private int mContentImgVisiable = View.INVISIBLE;

    // 颜色
    private int mTitleColor = 0xff000000;
    private int mContentColor = 0xff333333;
    private int mCancelBtnColor = 0xff000000;
    private int mConfirmBtnColor = 0xff000000;

    // 内容
    private CharSequence mTitleText = "";
    private CharSequence mContentText = "";
    private Drawable mContentImg;
    private CharSequence mCancelBtnText = "取消";
    private CharSequence mConfirmBtnText = "确定";
    // 图标资源id
    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    private CharSequence mStatusTipsText;
    private CharSequence mBtn1Text;
    private CharSequence mBtn2Text;
    private CharSequence mBtn3Text;

    private OnClickListener mCancelBtnClickListener;
    private OnClickListener mConfirmBtnClickListener;
    private OnClickListener mBtn1ClickListener;
    private OnClickListener mBtn2ClickListener;
    private OnClickListener mBtn3ClickListener;

    public CustomTipsDialog(Context context) {
        super(context, R.style.MyDialog);
        this.mContext = context;
        this.setCancelable(true);
        // Log.e("info", "CustomTipsDialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.e("info", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_custom_tips_dialog);
        initViews();
    }

    private void initViews() {
        // Log.e("info", "initViews");
        // 内容展示
        mContentShowLayout = findViewById(R.id.content_show_layout);
        mTitleTV = (TextView) findViewById(R.id.title_tv);
        mContentTV = (TextView) findViewById(R.id.content_tv);
        mContentIV = (ImageView) findViewById(R.id.content_iv);
        mCancelBT = (Button) findViewById(R.id.cancel_bt);
        mConfirmBT = (Button) findViewById(R.id.confirm_bt);
        mCancelBT.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancelBtnClickListener != null) {
                    mCancelBtnClickListener.onClick(view);
                }
            }
        });
        mConfirmBT.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConfirmBtnClickListener != null) {
                    mConfirmBtnClickListener.onClick(view);
                }
            }
        });
        // 按钮列表
        mBtnListLayout = findViewById(R.id.btn_list_layout);
        mStatusTipsTV = (TextView) findViewById(R.id.status_tips_tv);
        mBtn1BT = (Button) findViewById(R.id.btn1_bt);
        mBtn2BT = (Button) findViewById(R.id.btn2_bt);
        mBtn3BT = (Button) findViewById(R.id.btn3_bt);
        mBtn1BT.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBtn1ClickListener != null) {
                    mBtn1ClickListener.onClick(view);
                }
            }
        });
        mBtn2BT.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBtn2ClickListener != null) {
                    mBtn2ClickListener.onClick(view);
                }
            }
        });
        mBtn3BT.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBtn3ClickListener != null) {
                    mBtn3ClickListener.onClick(view);
                }
            }
        });
    }

    /**
     * 设置显示的样式（显示内容或按钮列表）,默认是SHOW_CONTENT
     *
     * @param viewStyle SHOW_CONTENT or BUTTON_LIST
     */
    public void setViewStyle(int viewStyle) {
        this.VIEW_STYLE = viewStyle;
    }

    /**
     * 设置是否显示title(默认有标题)
     *
     * @param noTitle
     */
    public void setNoTitle(boolean noTitle) {
        mIsNoTitle = noTitle;
    }

    /**
     * 设置标题文本颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        mTitleColor = color;
    }

    /**
     * 设置内容文本颜色
     *
     * @param color
     */
    public void setContentColor(int color) {
        mContentColor = color;
    }

    /**
     * 设置取消按钮文本颜色
     *
     * @param color
     */
    public void setCancelBtnColor(int color) {
        mCancelBtnColor = color;
    }

    /**
     * 设置确定按钮文本颜色
     *
     * @param color
     */
    public void setConfirmBtnColor(int color) {
        mConfirmBtnColor = color;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleText = title;
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(mContext.getString(titleId));
    }

    /**
     * 设置内容文本
     *
     * @param content 文本内容
     */
    public void setContentText(CharSequence content) {
        mContentText = content;
        mContentTextVisiable = View.VISIBLE;
        mContentImgVisiable = View.INVISIBLE;
    }

    /**
     * 设置内容文本
     *
     * @param contentId 文本内容资源id
     */
    public void setContentText(int contentId) {
        setContentText(mContext.getString(contentId));
    }

    /**
     * 设置图片内容
     *
     * @param drawable
     */
    public void setContentImg(Drawable drawable) {
        mContentImg = drawable;
        mContentTextVisiable = View.INVISIBLE;
        mContentImgVisiable = View.VISIBLE;
    }

    /**
     * 设置图片内容
     *
     * @param pathName
     */
    public void setContentImg(String pathName) {
        setContentImg(BitmapDrawable.createFromPath(pathName));
    }

    /**
     * 设置取消按钮文本（默认“取消”）
     *
     * @param text 文本
     */
    public void setCancleBtnText(CharSequence text) {
        mCancelBtnText = text;
    }

    /**
     * 设置取消按钮文本（默认“取消”）
     *
     * @param resId 资源id
     */
    public void setCancleBtnText(int resId) {
        setCancleBtnText(mContext.getString(resId));
    }

    /**
     * 设置确定按钮文本（默认“确定”）
     *
     * @param text 文本
     */
    public void setConfirmBtnText(CharSequence text) {
        mConfirmBtnText = text;
    }

    /**
     * 设置确定按钮文本（默认“确定”）
     *
     * @param resId 资源id
     */
    public void setConfirmBtnText(int resId) {
        setConfirmBtnText(mContext.getString(resId));
    }

    /**
     * 设置图标
     *
     * @param left   Resource identifier of the left Drawable.
     * @param top    Resource identifier of the top Drawable.
     * @param right  Resource identifier of the right Drawable.
     * @param bottom Resource identifier of the bottom Drawable.
     */
    public void setStatusTipsIcon(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * 设置状态提示文本
     *
     * @param text
     */
    public void setStatusTipsText(CharSequence text) {
        this.mStatusTipsText = text;
    }

    /**
     * 设置状态提示文本
     *
     * @param resId
     */
    public void setStatusTipsText(int resId) {
        setStatusTipsText(mContext.getString(resId));
    }

    /**
     * 设置按钮1文本
     *
     * @param text
     */
    public void setBtn1Text(CharSequence text) {
        this.mBtn1Text = text;
    }

    /**
     * 设置按钮1文本
     *
     * @param resId
     */
    public void setBtn1Text(int resId) {
        setBtn1Text(mContext.getString(resId));
    }

    /**
     * 设置按钮2文本
     *
     * @param text
     */
    public void setBtn2Text(CharSequence text) {
        this.mBtn2Text = text;
    }

    /**
     * 设置按钮2文本
     *
     * @param resId
     */
    public void setBtn2Text(int resId) {
        setBtn2Text(mContext.getString(resId));
    }

    /**
     * 设置按钮3文本
     *
     * @param text
     */
    public void setBtn3Text(CharSequence text) {
        this.mBtn3Text = text;
    }

    /**
     * 设置按钮3文本
     *
     * @param resId
     */
    public void setBtn3Text(int resId) {
        setBtn3Text(mContext.getString(resId));
    }

    /**
     * 注册取消按钮点击事件监听器
     *
     * @param clickListener
     */
    public void setOnCancelBtnClickListener(OnClickListener clickListener) {
        this.mCancelBtnClickListener = clickListener;
    }

    /**
     * 注册确定按钮点击事件监听器
     *
     * @param clickListener
     */
    public void setOnConfirmBtnClickListener(OnClickListener clickListener) {
        this.mConfirmBtnClickListener = clickListener;
    }

    /**
     * 注册按钮1点击事件监听器
     *
     * @param clickListener
     */
    public void setBtn1ClickListener(OnClickListener clickListener) {
        this.mBtn1ClickListener = clickListener;
    }

    /**
     * 注册按钮2点击事件监听器
     *
     * @param clickListener
     */
    public void setBtn2ClickListener(OnClickListener clickListener) {
        this.mBtn2ClickListener = clickListener;
    }

    /**
     * 注册按钮3点击事件监听器
     *
     * @param clickListener
     */
    public void setBtn3ClickListener(OnClickListener clickListener) {
        this.mBtn3ClickListener = clickListener;
    }

    /**
     * 显示
     */
    public void show() {
        super.show();
        // Log.e("info", "show");
        if (VIEW_STYLE == SHOW_CONTENT) {
            mContentShowLayout.setVisibility(View.VISIBLE);
            mBtnListLayout.setVisibility(View.GONE);
            // 设置是否显示title
            if (mIsNoTitle) {
                mTitleTV.setVisibility(View.GONE);
            } else {
                mTitleTV.setVisibility(View.VISIBLE);
            }

            // 设置颜色
            mTitleTV.setTextColor(mTitleColor);
            mContentTV.setTextColor(mContentColor);
            mCancelBT.setTextColor(mCancelBtnColor);
            mConfirmBT.setTextColor(mConfirmBtnColor);

            // 设置内容
            mTitleTV.setText(mTitleText);
            mContentTV.setText(mContentText);
            mContentIV.setImageDrawable(mContentImg);
            mCancelBT.setText(mCancelBtnText);
            mConfirmBT.setText(mConfirmBtnText);

            mContentTV.setVisibility(mContentTextVisiable);
            mContentIV.setVisibility(mContentImgVisiable);
        } else if (VIEW_STYLE == BUTTON_LIST) {
            mContentShowLayout.setVisibility(View.GONE);
            mBtnListLayout.setVisibility(View.VISIBLE);
            mStatusTipsTV.setCompoundDrawablesWithIntrinsicBounds(left, top,
                    right, bottom);
            mStatusTipsTV.setText(mStatusTipsText);
            mBtn1BT.setText(mBtn1Text);
            mBtn2BT.setText(mBtn2Text);
            mBtn3BT.setText(mBtn3Text);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // 释放资源
        if (VIEW_STYLE == SHOW_CONTENT) {
            mTitleTV.setText(null);
            mContentTV.setText(null);
            mContentIV.setImageDrawable(null);
            mCancelBT.setText(null);
            mConfirmBT.setText(null);
            mTitleText = null;
            mContentText = null;
            mContentImg = null;
            mCancelBtnText = "取消";
            mConfirmBtnText = "确定";
        } else if (VIEW_STYLE == BUTTON_LIST) {
            mStatusTipsTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mStatusTipsTV.setText(null);
            mBtn1BT.setText(null);
            mBtn2BT.setText(null);
            mBtn3BT.setText(null);
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
            mStatusTipsText = null;
            mBtn1Text = null;
            mBtn2Text = null;
            mBtn3Text = null;
        }

    }

    /**
     * 点击事件监听器
     */
    public interface OnClickListener {
        void onClick(View view);
    }

}
