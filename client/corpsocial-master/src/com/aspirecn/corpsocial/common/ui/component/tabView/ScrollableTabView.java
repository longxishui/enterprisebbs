package com.aspirecn.corpsocial.common.ui.component.tabView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspirecn.corpsocial.bundle.common.domain.AppConfig;
import com.aspirecn.corpsocial.common.util.ColorUtil;
import com.aspiren.corpsocial.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollableTabView extends HorizontalScrollView {

    private LinearLayout mContainer;
    /**
     * tab列表
     */
    private List<TabEntity> listTabEntity;
    /**
     * 选择监听回调
     */
    private OntabChangeListener listener;
    /**
     * 传入的上下文
     */
    private Context context;
    /**
     * 被选中时字体的颜色
     */
    private int textColor;
    /**
     * 当前的索引
     */
    private int currentIndex = 0;
    /**
     * entityViewMap集合
     */
    private Map<TabEntity, View> tabEntityViewMap = new HashMap<TabEntity, View>();

    public ScrollableTabView(Context context) {
        this(context, null);
    }

    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);
        mContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setLayoutParams(params);

        this.addView(mContainer);
    }

    public void init(Context context, OntabChangeListener listener, List<TabEntity> entityList) {
        this.listTabEntity = entityList;
        this.listener = listener;
        this.context = context;
        AppConfig appConfig = AppConfig.getInstance();
        int fontColor = ColorUtil.convert(appConfig.topViewDef.backgroundColor);
        this.textColor = fontColor;
        tabEntityViewMap.clear();
        initTheTabs();
    }

    public void init(Context context, OntabChangeListener listener, List<TabEntity> entityList, int backColor) {
        this.listTabEntity = entityList;
        this.listener = listener;
        this.context = context;
        this.textColor = backColor;
        tabEntityViewMap.clear();
        initTheTabs();
    }

    private void initTheTabs() {
        if (tabEntityViewMap.size() > 0) {
            updateView();
            selectTab(currentIndex);
            return;
        }
        mContainer.removeAllViews();
        for (int i = 0; i < listTabEntity.size(); i++) {
            TabEntity tabEntity = listTabEntity.get(i);
            View view = getTabView(i);
            mContainer.addView(view);
            tabEntityViewMap.put(tabEntity, view);
        }
        selectTab(currentIndex);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//			selectTab(currentIndex);
    }

    private void selectTab(int position) {
        for (int i = 0, pos = 0; i < mContainer.getChildCount(); i++, pos++) {
            View tab = mContainer.getChildAt(i);
            tab.setSelected(pos == position);
        }
        View selectView = mContainer.getChildAt(position);
        final int w = selectView.getMeasuredWidth();
        final int l = selectView.getLeft();
        final int x = l - this.getWidth() / 2 + w / 2;
        smoothScrollTo(x, this.getScrollY());
    }

    public View getTabView(final int position) {
        TabEntity tabEntity = listTabEntity.get(position);
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout tab = (RelativeLayout) inflater.inflate(R.layout.common_tabview, null);
        int width = context.getResources().getDisplayMetrics().widthPixels / (listTabEntity.size() > 4 ? 4 : listTabEntity.size());
        tab.setMinimumWidth(width);
//        tab.setWidth(width);
        viewHolder.mTV_name = (TextView) tab.findViewById(R.id.common_tabview_tv_name);
        viewHolder.mTV_name.setText(tabEntity.getName());
        viewHolder.mTV_notifynum = (TextView) tab.findViewById(R.id.common_tabview_tv_count);
        if (tabEntity.getNotifyNum() > 0) {
            viewHolder.mTV_notifynum.setText(tabEntity.getNotifyNum() + "");
            viewHolder.mTV_notifynum.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTV_notifynum.setVisibility(View.GONE);
        }
        viewHolder.mTV_line = (TextView) tab.findViewById(R.id.common_tabview_iv_select);
//        line.setWidth(width);
        viewHolder.mTV_line.setMinWidth(width);
        viewHolder.mTV_line.setBackgroundColor(textColor);
        if (currentIndex == position) {
            viewHolder.mTV_name.setTextColor(textColor);
            viewHolder.mTV_line.setVisibility(View.VISIBLE);
//        tab.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.workgrp_tab_selected_holo));
        } else {
            viewHolder.mTV_name.setTextColor(context.getResources().getColor(R.color.level_2));
            viewHolder.mTV_line.setVisibility(View.INVISIBLE);
//        tab.setBackgroundDrawable(null);
        }
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.notifyTabChange(position);
                }
            }
        });
        tab.setTag(viewHolder);
        return tab;
    }

    public void updateTabColor(int color) {
        this.textColor = color;
        initTheTabs();
    }

    public void updateTabView(int newIndex) {
        if (listTabEntity != null) {
            if (currentIndex != newIndex) {
                this.currentIndex = newIndex;
                initTheTabs();
            } else {
                return;
            }
        }
    }

    public void updateTabUnRead() {
        initTheTabs();
    }

    private void updateView() {
        for (int i = 0; i < listTabEntity.size(); i++) {
            TabEntity tabEntity = listTabEntity.get(i);
            View view = tabEntityViewMap.get(tabEntity);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (tabEntity.getNotifyNum() > 0) {
                viewHolder.mTV_notifynum.setText(tabEntity.getNotifyNum() + "");
                viewHolder.mTV_notifynum.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTV_notifynum.setVisibility(View.GONE);
            }
//            viewHolder.mTV_line.setBackgroundColor(context.getResources().getColor(R.color.blue));
            viewHolder.mTV_line.setBackgroundColor(textColor);
            if (currentIndex == i) {
//                viewHolder.mTV_name.setTextColor(context.getResources().getColor(R.color.level_1));
                viewHolder.mTV_name.setTextColor(textColor);
                viewHolder.mTV_line.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTV_name.setTextColor(context.getResources().getColor(R.color.level_2));
                viewHolder.mTV_line.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 点击切换监听
     */
    public interface OntabChangeListener {
        void notifyTabChange(int position);
    }

    class ViewHolder {
        TextView mTV_name;
        TextView mTV_notifynum;
        TextView mTV_line;
    }
}
