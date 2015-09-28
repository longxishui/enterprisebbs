/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.aspirecn.corpsocial.common.ui.component.imagegallery;

import com.aspirecn.corpsocial.common.eventbus.EventActivity;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspiren.corpsocial.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;
import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

/**
 * 图片浏览工具，用于显示多张图片，可以左右滑动、放大、缩小、双击放大/缩小。<br/>
 * 使用方法：
 * Intent intent = new Intent(context,com.aspirecn.corpsocial.common.ui.component.imagegallery.ImageGalleryActivity_.class);
 * <p/>
 * //生成图片url地址列表
 * List<String> urls = new ArrayList<String>();
 * urls.add("http://y3.ifengimg.com/cmpp/2014/09/22/14/f2de65cc-8dfe-4609-84c4-2110658ee3e3.jpg");
 * urls.add("http://d.ifengimg.com/mw950_/y3.ifengimg.com/cmpp/2014/09/22/14/c29fc0f4-92bb-46ab-9e5a-bfe7f76e95b8.jpg");
 * urls.add("http://d.ifengimg.com/mw950_/y0.ifengimg.com/cmpp/2014/09/22/14/1c36ff51-02e7-4f6a-bed9-095e20b0901b.jpg");
 * urls.add("http://d.ifengimg.com/mw950_/y0.ifengimg.com/cmpp/2014/09/22/14/4a65c73d-14eb-4ffb-b92d-260d7813ae0c.jpg");
 * <p/>
 * //创建图片信息。
 * //其中imageSubPath指图片的子文件夹，对应Constant.PICTURE_PATH文件夹的哪一个子文件夹。例如新闻的子文件夹为news2
 * ImageGalleryInfo info = new ImageGalleryInfo(imageSubPath, urls);
 * <p/>
 * intent.putExtra("imageGalleryInfo", info);
 * context.startActivity(intent);
 *
 * @author meixuesong
 */
@EActivity(R.layout.component_image_gallery_pager_view)
public class ImageGalleryActivity extends EventActivity {

    @ViewById(R.id.component_image_gallery_pager_view)
    GalleryViewPager mViewPager;

    private ImageGalleryInfo imageInfo;

    @AfterInject
    void doAfterInject() {
        imageInfo = (ImageGalleryInfo) getIntent().getSerializableExtra("imageGalleryInfo");
    }

    @AfterViews
    void doAfterViews() {
        List<String> items = new ArrayList<String>();

        if (imageInfo != null) {
            for (String url : imageInfo.getImageUrls()) {
                if (!StringUtils.isBlank(url)) {
                    items.add(url);
                } else {
                    LogUtil.e("ImageGallery recieved null url.");
                }
            }
        }

        CacheUrlPagerAdapter pagerAdapter = new CacheUrlPagerAdapter(this, items, imageInfo.getImageSubPath());
//		pagerAdapter.instantiateItem(container, position)
        pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                //Toast.makeText(GalleryFileActivity.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
            }
        });

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        if (imageInfo.getDefaultPicIndex() < items.size()) {
            mViewPager.setCurrentItem(imageInfo.getDefaultPicIndex());
        }
    }
}