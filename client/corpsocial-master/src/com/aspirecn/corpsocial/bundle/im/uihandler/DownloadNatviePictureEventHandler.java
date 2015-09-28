package com.aspirecn.corpsocial.bundle.im.uihandler;


import com.aspirecn.corpsocial.bundle.im.event.DownloadNativePictureEvent;
import com.aspirecn.corpsocial.bundle.im.repository.MsgDao;
import com.aspirecn.corpsocial.bundle.im.repository.entity.MsgEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.config.Constant;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.UIEventHandler;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.aspirecn.corpsocial.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.common.util.JSONUtils;

/**
 * 下载图片消息原图
 *
 * @author lizhuo_a
 */
@UIEventHandler(eventType = DownloadNativePictureEvent.class)
public class DownloadNatviePictureEventHandler implements
        IHandler<Null, DownloadNativePictureEvent> {

    private EventListenerSubjectLoader eventListener = EventListenerSubjectLoader
            .getInstance();

//    private IIMNetClient imNetClient = IMNetClientImpl.getIMNetClient();

    private MsgDao msgDao = new MsgDao();

    @Override
    public Null handle(DownloadNativePictureEvent busEvent) {
        String messageId = busEvent.getMessageId();
        Map<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("msgId", messageId);
        String userId = Config.getInstance().getUserId();
        wheres.put("belongUserId", userId);
        final MsgEntity msgEntity = msgDao.findByWhere(wheres);

        String nativePicFilePath = Constant.PICTURE_PATH + "picture_"
                + System.currentTimeMillis() + ".jpg";
        msgEntity.setNativePicture(nativePicFilePath);

        return new Null();

    }

}
