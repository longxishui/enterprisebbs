package com.aspirecn.corpsocial.bundle.im.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import cn.trinea.android.common.util.JSONUtils;

@DatabaseTable(tableName = "im_msg")
public class MsgEntity implements Serializable, Parcelable {

    public static final Parcelable.Creator<MsgEntity> CREATOR = new Creator<MsgEntity>() {
        public MsgEntity createFromParcel(Parcel source) {
            return new MsgEntity(source);
        }

        public MsgEntity[] newArray(int size) {
            return new MsgEntity[size];
        }
    };
    private static final long serialVersionUID = -8041865017911866203L;
    /**
     * 消息序列号
     */
    @DatabaseField(generatedId = true)
    private int seqNo;
    /**
     * 消息ID,与
     */
    @DatabaseField
    private String msgId;
    @DatabaseField
    private int status;

    /**
     * 所属对话ID，点对点聊天为对方用户ID，群聊为群ID
     */
    @DatabaseField
    private String chatId;

    /**
     * 是否我发送的消息
     */
    @DatabaseField
    private boolean owned;

    /**
     * 消息所属用户ID，如果与当前登录人ID一致既为发送的消息，否则为接收消息
     */
    @DatabaseField
    private String ownedUserId;

    @DatabaseField
    private String ownedUserName;

    /**
     * 内容的类型,0 //文本或者HTML片段;1 //语音;2 //图片消息;3 //视频消息;4 //位置信息;FILE = 5;
     */
    @DatabaseField
    private int contentType;
    /**
     * 消息内容
     */
    @DatabaseField
    private String content;
    /**
     * 图片消息，原图
     */
    @DatabaseField
    private String nativePicture;
    @DatabaseField
    private int nativePictureStatus;
    /**
     * 消息所属用户Id，适应多用户场景
     */
    @DatabaseField
    private String belongUserId;
    /**
     * 补充信息，json格式
     */
    @DatabaseField
    private String extraInfo;
    /**
     * 消息时间，如果是接收的消息为服务器下发消息的时间
     */
    @DatabaseField(dataType = DataType.DATE)
    private Date createTime;
    /**
     * 时间是否显示
     */
    @DatabaseField
    private boolean showCreateTime;

    public MsgEntity() {
    }

    public MsgEntity(Parcel source) {
        msgId = source.readString();
        chatId = source.readString();
        ownedUserId = source.readString();
        content = source.readString();
        extraInfo = source.readString();
        ownedUserName = source.readString();
        belongUserId = source.readString();
        nativePicture = source.readString();

        status = source.readInt();
        contentType = source.readInt();
        seqNo = source.readInt();

        createTime = (Date) source.readValue(Date.class.getClassLoader());
        owned = source.readByte() != 0;
        showCreateTime = source.readByte() != 0;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getOwnedUserId() {
        return ownedUserId;
    }

    public void setOwnedUserId(String ownedUserId) {
        this.ownedUserId = ownedUserId;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public String getOwnedUserName() {
        return ownedUserName;
    }

    public void setOwnedUserName(String ownedUserName) {
        this.ownedUserName = ownedUserName;
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

    public String getNativePicture() {
        return nativePicture;
    }

    public void setNativePicture(String nativePicture) {
        this.nativePicture = nativePicture;
    }

    public int getNativePictureStatus() {
        return nativePictureStatus;
    }

    public void setNativePictureStatus(int nativePictureStatus) {
        this.nativePictureStatus = nativePictureStatus;
    }

    public boolean isShowCreateTime() {
        return showCreateTime;
    }

    public void setShowCreateTime(boolean showCreateTime) {
        this.showCreateTime = showCreateTime;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msgId);
        dest.writeString(chatId);
        dest.writeString(ownedUserId);
        dest.writeString(content);
        dest.writeString(extraInfo);
        dest.writeString(ownedUserName);
        dest.writeString(belongUserId);
        dest.writeString(nativePicture);
        dest.writeInt(status);
        dest.writeInt(contentType);
        dest.writeInt(seqNo);
        dest.writeValue(createTime);// Date
        dest.writeByte((byte) (owned ? 1 : 0));
        dest.writeValue((byte) (showCreateTime ? 1 : 0));// boolean
    }

    public String getOriginalUrl() {
        String originalUrl = null;
        JSONObject fileInfoObj = JSONUtils.getJSONObject(getExtraInfo(), "fileInfo", null);
        if (fileInfoObj != null) {
            try {
                originalUrl = fileInfoObj.getString("orginalUrl");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return originalUrl;
    }

    /**
     * 是否发送成功(0表示发送中、1表示发送成功、2表示发送失败)
     *
     * @author lizhuo_a
     */
    public enum MsgStatus {
        Sending(0), Success(1), Fail(2), Receiving(3);

        public int value;

        MsgStatus(int value) {
            this.value = value;
        }

    }

    public interface MsgContentType {
        int TEXT = ContentType.TEXT.getValue();
        int VOICE = ContentType.VOICE.getValue();
        int PICTURE = ContentType.PICTURE.getValue();
        int VIDIO = ContentType.VIDIO.getValue();
        int POSITION = ContentType.POSITION.getValue();
        int FILE = ContentType.FILE.getValue();
        int SYSTEM = ContentType.SYSTEM.getValue();
    }
}
