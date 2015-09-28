package com.aspirecn.corpsocial.bundle.common.uitils;

import android.content.Context;

import com.aspirecn.corpsocial.common.util.AssetsUtils;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenbin on 2015/9/1.
 */
public class NotifyConfigUtil {

    private static Map<Integer,Integer> configs=new HashMap<>();

    public static void init(Context context){
        try {
            Notifys notifys = AssetsUtils.getInstance().read(context, "notify.json", Notifys.class);
            for (NotifyConfig notifyConfig : notifys.data) {
                configs.put(notifyConfig.modelType, notifyConfig.enable);
            }
        }catch(Exception e){
            LogUtil.e("",e);

        }
    }

    public static boolean isEnable(int model){
        Integer enable=configs.get(model);
        if(enable==null) return false;
        return enable==1?true:false;
    }

    class Notifys{
        List<NotifyConfig> data;
    }

    class NotifyConfig{
        int modelType;
        int enable;
    }
}
