package com.aspirecn.corpsocial.common.ui.component.imagegallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageGalleryInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String imageSubPath;
    private List<String> imageUrls;
    private int defaultPicIndex = 0;

    /**
     * 图片信息
     *
     * @param imageSubPath 图片文件夹的下级子文件夹名，
     * @param imageUrls    图片网址列表
     */
    public ImageGalleryInfo(String imageSubPath, List<String> imageUrls) {
        this.imageSubPath = imageSubPath;
        this.imageUrls = new ArrayList<String>();
        this.imageUrls.addAll(imageUrls);
    }

    /**
     * 图片信息
     *
     * @param imageSubPath    图片文件夹的下级子文件夹名，
     * @param imageUrls       图片网址列表
     * @param defaultPicIndex 打开时显示第几张图片（0为第1张）
     */
    public ImageGalleryInfo(String imageSubPath, List<String> imageUrls, int defaultPicIndex) {
        this.imageSubPath = imageSubPath;
        this.imageUrls = new ArrayList<String>();
        this.imageUrls.addAll(imageUrls);
        this.defaultPicIndex = defaultPicIndex;
    }

    public String getImageSubPath() {
        return imageSubPath;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public int getDefaultPicIndex() {
        return defaultPicIndex;
    }
}
