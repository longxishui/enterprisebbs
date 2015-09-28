package com.aspirecn.corpsocial.bundle.settings.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment;
import com.aspirecn.corpsocial.bundle.common.ui.fragment.ActionBarFragment_;
import com.aspirecn.corpsocial.common.eventbus.EventFragmentActivity;
import com.aspiren.corpsocial.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wangdeng on 2014/12/19.
 */

@EActivity(R.layout.setting_ui_about_activity)
public class WorkbenchSettingAboutActivity extends EventFragmentActivity {

    private static final int[] pics = {R.drawable.common_exhibition_pir_1, R.drawable.common_exhibition_pir_2,
    };
    @ViewById(R.id.viewpagerLayout)
    ViewPager myViewPager;
    private LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);

    @AfterViews
    void doAfterViews() {
        ActionBarFragment fab = ActionBarFragment_.builder().
                title(getString(R.string.workbench_setting_about)).
                build();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_actionbar, fab).commit();
        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return pics.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //初始化引导图片列
                ImageView iv = new ImageView(WorkbenchSettingAboutActivity.this);
                iv.setLayoutParams(mParams);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                try {
//                    iv.setBackgroundResource(pics[position]);
                    ImageLoader.getInstance().displayImage("drawable://" + pics[position], iv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                container.addView(iv, 0);
                return iv;
            }
        };

        myViewPager.setAdapter(pagerAdapter);

    }

}
