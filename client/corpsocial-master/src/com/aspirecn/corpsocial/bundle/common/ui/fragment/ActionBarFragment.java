package com.aspirecn.corpsocial.bundle.common.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.bundle.common.domain.AppInfo;
import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefRespEvent;
import com.aspirecn.corpsocial.bundle.common.listener.GetCorpViewDefRespSubject.GetCorpViewDefRespListener;
import com.aspirecn.corpsocial.common.eventbus.EventFragment;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hongyinghui on 15/3/27.
 * <p/>
 * 页面顶部导航栏
 */
@EFragment(R.layout.fragment_actionbar)
public class ActionBarFragment extends EventFragment implements GetCorpViewDefRespListener {

    /**
     * 是否为导航页面
     */
    @FragmentArg
    boolean navigation;

    /**
     * 应用唯一识别字符串(非)
     */
    @FragmentArg
    String startPath;

    /**
     * 标题
     */
    @FragmentArg
    String title;

    /**
     * 背景是否为透明
     */
    @FragmentArg
    boolean transparent;

    @ViewById(R.id.layout_background)
    LinearLayout layoutBackground;

    @ViewById(R.id.layout_logo_title)
    LinearLayout layoutLogoTitle;

    @ViewById(R.id.iv_logo)
    ImageView ivLogo;

    @ViewById(R.id.tv_title)
    TextView tvTitle;

    @ViewById(R.id.tv_back)
    TextView tvBack;

    @ViewById(R.id.layout_custom)
    LinearLayout layoutCustom;

    @ViewById(R.id.img_btn_first)
    ImageButton imgBtnFirst;

    @ViewById(R.id.img_btn_second)
    ImageButton imgBtnSecond;

    @ViewById(R.id.img_btn_third)
    ImageButton imgBtnThird;

    @ViewById(R.id.btn_first)
    Button btnFirst;

    @ViewById(R.id.btn_second)
    Button btnSecond;

    @ViewById(R.id.btn_third)
    Button btnThird;

    private Builder builder;

    private LifeCycleListener listener;

    @Click(R.id.tv_back)
    public void onBack() {
        getActivity().finish();
    }

    public Builder build() {
        if (builder == null) {
            builder = new Builder(getActivity());
        }
        return builder;
    }

    @UiThread
    @AfterViews
    void doAfterViews() {
        AppConfig appConfig = AppConfig.getInstance();
        // custom navigation and title（根据传递的startPath，结合底部应用列表，判断该应用是否为navigation类型）
        if (!TextUtils.isEmpty(startPath) && appConfig.bottomDef != null && appConfig.bottomDef.size() != 0) {
            for (AppInfo appInfo : appConfig.bottomDef) {
                if (this.startPath.equals(appInfo.startPath)) {
                    this.navigation = true;
                    this.title = appInfo.name;
                    break;
                }
            }
        }
        // custom logo
        String logo = appConfig.icon;
        // custom background color
        int backgroundColor = ColorUtil.convert(appConfig.topViewDef.backgroundColor);
        // custom title font color
        int fontColor = ColorUtil.convert(appConfig.topViewDef.fontColor);

        // title
        if (navigation) {
            layoutLogoTitle.setVisibility(View.VISIBLE);
            tvBack.setVisibility(View.GONE);
            tvTitle.setText(title);
            tvTitle.setTextColor(fontColor);
            // logo
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .showImageForEmptyUri(R.drawable.actionbar_logo)
                    .showImageOnFail(R.drawable.actionbar_logo)
                    .build();
            ImageLoader.getInstance().displayImage(logo, ivLogo, options);
        } else {
            layoutLogoTitle.setVisibility(View.GONE);
            tvBack.setVisibility(View.VISIBLE);
            tvBack.setText(title);
            tvBack.setTextColor(fontColor);
        }
        btnFirst.setTextColor(fontColor);
        btnSecond.setTextColor(fontColor);
        btnThird.setTextColor(fontColor);

        // background
        if (transparent) {
            layoutBackground.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        } else {
            layoutBackground.setBackgroundColor(backgroundColor);
        }

        // life cycle
        if (listener != null) {
            listener.onActionBarViewCreated(this);
        }
    }

    public void setLifeCycleListener(LifeCycleListener listener) {
        this.listener = listener;
    }

    @Override
    public void onGetCorpViewDefEvent(GetCorpViewDefRespEvent event) {
        doAfterViews();
    }

    public interface LifeCycleListener {
        void onActionBarViewCreated(ActionBarFragment fab);
    }

    public class Builder {

        private final Param P;

        public Builder(Context context) {
            P = new Param(context);
        }

        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public Builder setBackButtonListener(View.OnClickListener listener) {
            P.mListenerBackButton = listener;
            return this;
        }

        public Builder setFirstButtonListener(View.OnClickListener listener) {
            P.mListenerFirstButton = listener;
            return this;
        }

        public Builder setSecondButtonListener(View.OnClickListener listener) {
            P.mListenerSecondButton = listener;
            return this;
        }

        public Builder setThirdButtonListener(View.OnClickListener listener) {
            P.mListenerThirdButton = listener;
            return this;
        }

        public Builder setFirstButtonIcon(int iconId) {
            P.mIconFirstButton = P.mContext.getResources().getDrawable(iconId);
            P.mTextFirstButton = null;
            P.mShowFirstButton = true;
            return this;
        }

        public Builder setSecondButtonIcon(int iconId) {
            P.mIconSecondButton = P.mContext.getResources().getDrawable(iconId);
            P.mTextSecondButton = null;
            P.mShowSecondButton = true;
            return this;
        }

        public Builder setThirdButtonIcon(int iconId) {
            P.mIconThirdButton = P.mContext.getResources().getDrawable(iconId);
            P.mTextThirdButton = null;
            P.mShowThirdButton = true;
            return this;
        }

        public Builder setFirstButtonText(int textId) {
            P.mTextFirstButton = P.mContext.getText(textId);
            P.mIconFirstButton = null;
            P.mShowFirstButton = true;
            return this;
        }

        public Builder setFirstButtonText(String text) {
            P.mTextFirstButton = text;
            P.mIconFirstButton = null;
            P.mShowFirstButton = true;
            return this;
        }

        public Builder setSecondButtonText(int textId) {
            P.mTextSecondButton = P.mContext.getText(textId);
            P.mIconSecondButton = null;
            P.mShowSecondButton = true;
            return this;
        }

        public Builder setSecondButtonText(String text) {
            P.mTextSecondButton = text;
            P.mIconSecondButton = null;
            P.mShowSecondButton = true;
            return this;
        }

        public Builder setThirdButtonText(int textId) {
            P.mTextThirdButton = P.mContext.getText(textId);
            P.mIconThirdButton = null;
            P.mShowThirdButton = true;
            return this;
        }

        public Builder setThirdButtonText(String text) {
            P.mTextThirdButton = text;
            P.mIconThirdButton = null;
            P.mShowThirdButton = true;
            return this;
        }

        public Builder setShowFirstButton(boolean b) {
            P.mShowFirstButton = b;
            return this;
        }

        public Builder setShowSecondButton(boolean b) {
            P.mShowSecondButton = b;
            return this;
        }

        public Builder setShowThirdButton(boolean b) {
            P.mShowThirdButton = b;
            return this;
        }

        public Builder setBackButtonIcon(int iconId) {
            P.mIconBackButton = P.mContext.getResources().getDrawable(iconId);
            return this;
        }

        public Builder setCustomView(View customView) {
            P.mCustomView = customView;
            return this;
        }

        public Builder apply() {
            P.apply();
            return this;
        }
    }

    /**
     * 对外提供的设置参数
     */
    public class Param {

        public Context mContext;
        public String mTitle;
        public View.OnClickListener mListenerBackButton;
        public View.OnClickListener mListenerFirstButton;
        public View.OnClickListener mListenerSecondButton;
        public View.OnClickListener mListenerThirdButton;
        public Drawable mIconBackButton;
        public Drawable mIconFirstButton;
        public Drawable mIconSecondButton;
        public Drawable mIconThirdButton;
        public CharSequence mTextFirstButton;
        public CharSequence mTextSecondButton;
        public CharSequence mTextThirdButton;
        public boolean mShowFirstButton;
        public boolean mShowSecondButton;
        public boolean mShowThirdButton;
        public View mCustomView;

        public Param(Context context) {
            mContext = context;
        }

        public void apply() {
            // 设置标题
            if (mTitle != null) {
                if (navigation) {
                    tvTitle.setText(mTitle);
                } else {
                    tvBack.setText(mTitle);
                }
            }
            // 设置返回点击事件
            if (mListenerBackButton != null) {
                tvBack.setOnClickListener(mListenerBackButton);
            }
            //设置返回按键图标
            if (mIconBackButton != null) {
//                tvBack.setCompoundDrawables(mIconBackButton,null,null,null);
//                tvBack.setCompoundDrawablesRelative(mIconBackButton,null,null,null);
                tvBack.setCompoundDrawablesWithIntrinsicBounds(mIconBackButton, null, null, null);
            }
            // 设置右1按钮点击事件
            if (mListenerFirstButton != null) {
                imgBtnFirst.setOnClickListener(mListenerFirstButton);
                btnFirst.setOnClickListener(mListenerFirstButton);
            }
            // 设置右2按钮点击事件
            if (mListenerSecondButton != null) {
                imgBtnSecond.setOnClickListener(mListenerSecondButton);
                btnSecond.setOnClickListener(mListenerSecondButton);
            }
            // 设置右3按钮点击事件
            if (mListenerThirdButton != null) {
                imgBtnThird.setOnClickListener(mListenerThirdButton);
                btnThird.setOnClickListener(mListenerThirdButton);
            }
            boolean showIconFirst = false;
            boolean showIconSecond = false;
            boolean showIconThird = false;
            // 设置右1图标
            if (mIconFirstButton != null) {
                showIconFirst = true;
                imgBtnFirst.setImageDrawable(mIconFirstButton);
            }
            // 设置右1文本
            if (mTextFirstButton != null) {
                showIconFirst = false;
                btnFirst.setText(mTextFirstButton);
            }
            // 设置右2图标
            if (mListenerSecondButton != null) {
                showIconSecond = true;
                imgBtnSecond.setImageDrawable(mIconSecondButton);
            }
            // 设置右2文本
            if (mTextSecondButton != null) {
                showIconSecond = false;
                btnSecond.setText(mTextSecondButton);
            }
            // 设置右3图标
            if (mListenerThirdButton != null) {
                showIconThird = true;
                imgBtnThird.setImageDrawable(mIconThirdButton);
            }
            // 设置右3文本
            if (mTextThirdButton != null) {
                showIconThird = false;
                btnThird.setText(mTextThirdButton);
            }
            // 显示(放在设置文本、图标后面才生效)
            if (mShowFirstButton) {
                if (showIconFirst) {
                    btnFirst.setVisibility(View.GONE);
                    imgBtnFirst.setVisibility(View.VISIBLE);
                } else {
                    btnFirst.setVisibility(View.VISIBLE);
                    imgBtnFirst.setVisibility(View.GONE);
                }
            } else {
                btnFirst.setVisibility(View.GONE);
                imgBtnFirst.setVisibility(View.GONE);
            }
            // 显示(放在设置文本、图标后面才生效)
            if (mShowSecondButton) {
                if (showIconSecond) {
                    btnSecond.setVisibility(View.GONE);
                    imgBtnSecond.setVisibility(View.VISIBLE);
                } else {
                    btnSecond.setVisibility(View.VISIBLE);
                    imgBtnSecond.setVisibility(View.GONE);
                }
            } else {
                btnSecond.setVisibility(View.GONE);
                imgBtnSecond.setVisibility(View.GONE);
            }
            // 显示(放在设置文本、图标后面才生效)
            if (mShowThirdButton) {
                if (showIconThird) {
                    btnThird.setVisibility(View.GONE);
                    imgBtnThird.setVisibility(View.VISIBLE);
                } else {
                    btnThird.setVisibility(View.VISIBLE);
                    imgBtnThird.setVisibility(View.GONE);
                }
            } else {
                btnThird.setVisibility(View.GONE);
                imgBtnThird.setVisibility(View.GONE);
            }
            // 填充位于中间的自定义View
            if (mCustomView != null) {
                layoutCustom.removeAllViews();
                if (mCustomView.getLayoutParams() != null) {
                    layoutCustom.addView(mCustomView, mCustomView.getLayoutParams());
                } else {
                    layoutCustom.addView(mCustomView);
                }
            }
        }
    }
}
