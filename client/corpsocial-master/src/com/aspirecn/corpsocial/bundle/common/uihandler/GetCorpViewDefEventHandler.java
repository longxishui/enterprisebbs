package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.common.event.GetCorpViewDefEvent;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.IHandler;
import com.aspirecn.corpsocial.common.eventbus.Null;
import com.google.gson.Gson;



/**
 * Created by yinghuihong on 15/5/21.
 * <p/>
 * 获取企业配置信息（字体颜色、字体大小、应用排序）
 */
@EventBusAnnotation.UIEventHandler(eventType = GetCorpViewDefEvent.class)
public class GetCorpViewDefEventHandler implements IHandler<Null, GetCorpViewDefEvent> {

//    private Logger LOGGER = LoggerFactory.getLogger(GetCorpViewDefEventHandler.class);
    private Gson gson = new Gson();

    @Override
    public Null handle(final GetCorpViewDefEvent args) {
        // see HttpPortRequester.java in net library which had other default parameter
//        HttpRequest.request("GET_CORP_VIEW_DEF", gson.toJson(args), new IHttpCallBack() {
//            @Override
//            public void callBack(Result result) {
//                if (result.getErrorCode() == ErrorCode.SUCCESS.getValue()) {
//                    GetCorpViewDefRespBean respBean = new Gson().fromJson(result.getMessage(), GetCorpViewDefRespBean.class);
//                    if (respBean.rst == 0) {
//                        AppConfig appConfig = respBean.data;
//                        // save
//                        AppConfig appConfigTemp = new AppConfig();
//                        if (ConfigPersonal.getInstance().getMutiCorp() == 1) {
//                            appConfigTemp.workAreaDef = appConfig.workAreaDef;
//                        } else {
//                            appConfigTemp = appConfig;
//                        }
//                        ConfigPersonal.putString(ConfigPersonal.Key.APP_CONFIG + args.getCorpId(), new Gson().toJson(appConfigTemp));
//                        // notify
//                        GetCorpViewDefRespEvent event = new GetCorpViewDefRespEvent();
//                        event.fontColor = ColorUtil.convert(AppConfig.getInstance().topViewDef.backgroundColor);
//                        EventListenerSubjectLoader.getInstance().notifyListener(event);
//                    } else {
//                        //TODO handle the fail cause by internal server error
//                        LOGGER.debug(result.getMessage());
//                    }
//                } else {
//                    //TODO handle the fail cause by network
//                    LOGGER.debug(result.getMessage());
//                }
//            }
//        });
        return null;
    }

    // HINT 使用方式
    // AppInfoConfig appConfig = new Gson().fromJson(ConfigPersonal.getString(ConfigPersonal.Key.APP_CONFIG), AppInfoConfig.class);

}
