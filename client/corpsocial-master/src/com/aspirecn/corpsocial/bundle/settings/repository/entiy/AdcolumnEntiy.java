package com.aspirecn.corpsocial.bundle.settings.repository.entiy;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenziqiang on 15-3-24.
 */
@DatabaseTable(tableName = "adcolumn")
public class AdcolumnEntiy {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    /**
     * 用户标识
     */
    @DatabaseField
    private String belongUserId;
    /**
     * 序列号
     */
    @DatabaseField(generatedId = true)
    private int seqNo;
    @DatabaseField
    private String sid;
    @DatabaseField
    private String sourceId;//企业源业务ID
    @DatabaseField
    private String titleType;//内容类型
    @DatabaseField
    private String title;//根据内容类型确定，TEXT时是标题文字，PIC时是图片URL
    @DatabaseField
    private String publishTime;//发布时间
    @DatabaseField
    private String creator; //创建人
    @DatabaseField
    private String iconUrl;//缩略图下载地址
    @DatabaseField
    private String bigIconUrl;//原图下载地址
    @DatabaseField
    private String content;//内容
    @DatabaseField
    private String viewUrl;//详细信息查看地址
    @DatabaseField
    private String lastModifiedTime;//最后更新时间
    @DatabaseField
    private String description;//描述
    @DatabaseField
    private String corpId;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = dateFormat.format(new Date(Long.parseLong(publishTime)));
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBigIconUrl() {
        return bigIconUrl;
    }

    public void setBigIconUrl(String bigIconUrl) {
        this.bigIconUrl = bigIconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AdcolumnEntiy{" +
                "belongUserId='" + belongUserId + '\'' +
                ", seqNo=" + seqNo +
                ", sid='" + sid + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", titleType='" + titleType + '\'' +
                ", title='" + title + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", creator='" + creator + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", bigIconUrl='" + bigIconUrl + '\'' +
                ", content='" + content + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", lastModifiedTime='" + lastModifiedTime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
