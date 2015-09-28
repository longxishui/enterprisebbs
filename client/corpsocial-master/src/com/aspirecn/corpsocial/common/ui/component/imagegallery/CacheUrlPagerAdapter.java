package com.aspirecn.corpsocial.common.ui.component.imagegallery;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

public class CacheUrlPagerAdapter extends BasePagerAdapter {
    private String imageSubPath;

    public CacheUrlPagerAdapter(Context context, List<String> resources, String imageSubPath) {
        super(context, resources);
        this.imageSubPath = imageSubPath;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager) container).mCurrentView = ((CacheTouchImageView) object).getImageView();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        final CacheTouchImageView iv = new CacheTouchImageView(mContext, imageSubPath);
        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        collection.addView(iv, 0);
        return iv;
    }

}
