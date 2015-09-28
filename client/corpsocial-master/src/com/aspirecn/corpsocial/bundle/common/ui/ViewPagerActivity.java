package com.aspirecn.corpsocial.bundle.common.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.common_viewpager_startup)
public class ViewPagerActivity extends EventActivity implements OnClickListener, OnPageChangeListener {

    //引导图片资源
    private static final int[] pics = {R.drawable.common_exhibition_1, R.drawable.common_exhibition_2};
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private Button button;
    //记录当前选中位置
    private int currentIndex;

    /**
     * Called when the activity is first created.
     */
    @AfterViews
    void init() {
        button = (Button) findViewById(R.id.button);
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        //初始化引导图片列
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager_startup);
        //初始化Adapter  
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调  
        vp.setOnPageChangeListener(this);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(ViewPagerActivity.this, com.aspirecn.corpsocial.bundle.workgrp.ui.WorkGrpMainActivity_.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        currentIndex = positon;
    }

    //当滑动状态改变时调用  
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //当当前页面被滑动时调
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //当新的页面被选中时调
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状
        setCurDot(arg0);
        if (arg0 == views.size() - 1) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }

}