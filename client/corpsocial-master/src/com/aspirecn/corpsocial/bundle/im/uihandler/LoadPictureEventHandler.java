package com.aspirecn.corpsocial.bundle.im.uihandler;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.im.event.LoadPictureEvent;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 下载聊天图片处理类
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = LoadPictureEvent.class)
public class LoadPictureEventHandler implements IHandler<Null, LoadPictureEvent> {

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader.getInstance();

    private MsgDao msgDao = new MsgDao();

    @Override
    public Null handle(LoadPictureEvent event) {
        MsgEntity msgEntity = event.getMsgEntity();
        MsgEntity findMsgEntity = msgDao.findById(msgEntity.getSeqNo());
        if (findMsgEntity != null) {
            doLoadPicture(findMsgEntity);
        } else {

        }

        return new Null();
    }

    private void doLoadPicture(final MsgEntity msgEntity) {
        String msgId = msgEntity.getMsgId();
//        FileInfo fileInfo = getFileInfo(msgEntity);
        String picturePath = msgEntity.getContent();

//        imNetClient.downloadBinary(msgId, fileInfo, picturePath, new IMessageStatusNotify() {
//                    @Override
//                    public void notify(MessageRst messageRst) {
//                        int errorCode = messageRst.getErrorCode();
//                        MsgEntity findMsgEntity = msgDao.findById(msgEntity.getSeqNo());
//
//                        float percent = messageRst.getPercent();
//                        findMsgEntity.setStatus(MsgEntity.MsgStatus.Receiving.value);
//                        LogUtil.i(String.format("图片下载进度：  %1$.2f", percent * 100));
//
//                        if ((ErrorCode.SUCCESS.getValue() == errorCode) && (percent == 1)) {
//                            findMsgEntity.setStatus(MsgEntity.MsgStatus.Success.value);
//
//                        } else if ((ErrorCode.SUCCESS.getValue() == errorCode) && (percent < 1)) {
//                            findMsgEntity.setStatus(MsgEntity.MsgStatus.Receiving.value);
//
//                        } else {
//                            LogUtil.i(String.format("图片下载错误  %s", errorCode));
//                            findMsgEntity.setStatus(MsgEntity.MsgStatus.Fail.value);
//                        }
//
//                        updateLoadPercent(findMsgEntity, percent);
//                        msgDao.update(findMsgEntity);
//
//                        SyncMessageRespEvent syncMessageListRespEvent = new SyncMessageRespEvent();
//                        syncMessageListRespEvent.setErrorCode(errorCode);
//                        syncMessageListRespEvent.setPercent(percent);
//                        syncMessageListRespEvent.setMsgEntity(findMsgEntity);
//                        eventListener.notifyListener(syncMessageListRespEvent);
//                    }
//                }
//        );
    }

    private void updateLoadPercent(MsgEntity msgEntity, float percent) {
        String extraInfoStr = msgEntity.getExtraInfo();
        try {
            JSONObject extraInfo = null;
            if (TextUtils.isEmpty(extraInfoStr)) {
                extraInfo = new JSONObject();
            } else {
                extraInfo = new JSONObject(extraInfoStr);
            }
            extraInfo.put("percent", percent);
            msgEntity.setExtraInfo(extraInfo.toString());
        } catch (JSONException e) {
            LogUtil.e("图片下载出错", e);
        }
    }

//    private FileInfo getFileInfo(MsgEntity msgEntity) {
//        FileInfo fileInfo = null;
//        String extraInfo = msgEntity.getExtraInfo();
//        try {
//            if (TextUtils.isEmpty(extraInfo)) {
//                return null;
//            }
//            JSONObject jsonObject = new JSONObject(extraInfo);
//            if (jsonObject == null) {
//                return null;
//            }
//            JSONObject fileInfoJo = jsonObject.getJSONObject("fileInfo");
//            if (fileInfoJo == null) {
//                return null;
//            }
//
//            String groupName = fileInfoJo.getString("groupName");
//            String address = fileInfoJo.getString("address");
//            Long length = fileInfoJo.getLong("length");
//            String orginalAddress = fileInfoJo.getString("orginalAddress");
//            String url = fileInfoJo.getString("url");
//            String orginalUrl = fileInfoJo.getString("orginalUrl");
//            Long orginalLength = fileInfoJo.getLong("orginalLength");
//
//            fileInfo = new FileInfo(
//                    groupName,
//                    address,
//                    length,
//                    orginalAddress,
//                    url,
//                    orginalUrl,
//                    orginalLength);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return fileInfo;
//    }

}
