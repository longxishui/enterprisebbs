package com.aspirecn.corpsocial.bundle.im.domain;

import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.bundle.im.utils.ContentType;
import com.aspirecn.corpsocial.common.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片墙
 *
 * @author lizhuo_a
 */
public class GroupPictureWall {

    /**
     * 图片消息列表
     */
    // private LinkedList<MsgEntity> pictureMsgList = new
    // LinkedList<MsgEntity>();
    private ArrayList<MsgEntity> pictureMsgList = new ArrayList<MsgEntity>();

    private MsgDao msgDao = new MsgDao();

    private String chatId;

    public void loadPictures(String chatId, int index, int count) {
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("belongUserId", Config.getInstance().getUserId());
        wheres.put("chatId", chatId);
        wheres.put("contentType", ContentType.PICTURE.getValue());

        List<MsgEntity> msgList = msgDao.findAllByWhereAndIndex(wheres, index,
                count, "createTime DESC");

        pictureMsgList.addAll(msgList);
    }

    public List<MsgEntity> getPictureMsgList() {
        return pictureMsgList;
    }

    public int doGetPictureSize() {
        return pictureMsgList.size();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}
