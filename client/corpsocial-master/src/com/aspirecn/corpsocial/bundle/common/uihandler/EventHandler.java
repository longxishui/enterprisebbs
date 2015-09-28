package com.aspirecn.corpsocial.bundle.common.uihandler;

import com.aspirecn.corpsocial.bundle.addrbook.event.vo.CorpModified;
import com.aspirecn.corpsocial.bundle.common.domain.GetSelfCorpListRespBean;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.BusEvent;
import com.aspirecn.corpsocial.common.eventbus.EventListenerSubjectLoader;
import com.aspirecn.corpsocial.common.eventbus.UiEventHandleFacade;
import com.aspirecn.corpsocial.common.util.EventChainUtil;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amos on 15-6-16.
 */
public class EventHandler {

    public EventListenerSubjectLoader instance = EventListenerSubjectLoader
            .getInstance();


    public List<CorpModified> getCorpModified(){
        List<CorpModified> cms = new ArrayList();
            List<UserCorp> ucs = GetSelfCorpListRespBean.find(Config.getInstance().getUserId());
            for (UserCorp uc : ucs) {
                UserCorp entity = GetSelfCorpListRespBean.findByCorpId(uc.getCorpId());
                CorpModified cm = new CorpModified();
                cm.setCorpId(entity.getCorpId());
                cm.setLastModifiedTime(entity.getUserLastModifiedTime());
                cms.add(cm);
            }

        return cms;
    }

    public void runNextevents(String eventname){
        List<BusEvent> nextevents= EventChainUtil.getNextEvents(eventname);
        if(nextevents!=null&&nextevents.size()>0){
            for(BusEvent nevent:nextevents){
                LogUtil.i("aspire----nextevent:"+nevent.getClass().getName());
                UiEventHandleFacade.getInstance().handle(nevent);
            }
        }
    }

    public void netFaiNotify(BusEvent event) {
        instance.notifyListener(event);
    }
}


