package com.aspirecn.corpsocial.bundle.settings.repository;

import android.util.Log;

import com.aspirecn.corpsocial.bundle.settings.repository.entiy.AdcolumnEntiy;
import com.aspirecn.corpsocial.bundle.settings.repository.vo.AdcolumnListVO;
import com.aspirecn.corpsocial.bundle.settings.utils.AdcolumnUtils;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.JSONUtils;

/**
 * Created by chenziqiang on 15-3-24.
 */
public class AdcolumnDao extends SqliteDao<AdcolumnEntiy, String> {

    public List<AdcolumnEntiy> toAdcolumEntiy(String adcolumn) {
//        String str="{data:[{bigIconUrl:,content:阿斌的​新增宣传,corpId:110,creator:阿斌,description:新增宣传,iconUrl:,lastModifiedTime:1426662525339,publishTime:1426662525000,sid:32705d6121ec4278b781cdec6d3fcf15,sourceId:,title:新增宣传,titleType:0,viewUrl:},{bigIconUrl:http://amoadev.aspirecn.com:8803/group1/M00/01/F6/CgEDtFUJJ4CAZWD8AALfrR1QJzQ840.jpg,content:图片宣传内容图片宣传内容图片宣传内容图片宣传内容图片宣传内容图片宣传内容图片宣传内容图片宣传内容,corpId:110,creator:阿斌,description:图片宣传内容图片宣传内容,iconUrl:http://amoadev.aspirecn.com:8803/group1/M00/01/F6/CgEDtFUJJ3iAUqTvAABk7CQuVvo250.jpg,lastModifiedTime:1426664265348,publishTime:1426664249000,sid:69b1f328fe1b4696b703bdf36e38fdcf,sourceId:,title:图片宣传内容,titleType:1,viewUrl:},{bigIconUrl:http://amoadev.aspirecn.com:8803/group1/M00/01/F6/CgEDtFUJJ-mAJd7VAALfrR1QJzQ384.jpg,content:<font face=tahoma, arial, helvetica, sans-serif><span style=font-size: 12px;>图文并茂图文并茂图文并茂图文并茂图文并茂图文并茂图文并茂图文并茂图文并茂</span></font>,corpId:110,creator:阿斌,description:图文并茂图文并茂,iconUrl:http://amoadev.aspirecn.com:8803/group1/M00/01/F6/CgEDtFUJJ-mANndkAABk7CQuVvo822.jpg,lastModifiedTime:1426664368594,publishTime:1426664368000,sid:15c45fea10224e4cb3a7243b47c39fab,sourceId:,title:图文并茂,titleType:2,viewUrl:}]}";

        List<AdcolumnEntiy> list = new ArrayList<AdcolumnEntiy>();

        try {

            JSONArray array = JSONUtils.getJSONArray(adcolumn, "data", null);

            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject executorObject = array.getJSONObject(i);
                AdcolumnEntiy entiy = new AdcolumnEntiy();
                if (!executorObject.isNull("sid"))
                    entiy.setSid(executorObject.getString("sid"));

                if (!executorObject.isNull("sourceId"))
                    entiy.setSourceId(executorObject.getString("sourceId"));

                if (!executorObject.isNull("titleType"))
                    entiy.setTitleType(executorObject.getString("titleType"));

                if (!executorObject.isNull("title"))
                    entiy.setTitle(executorObject.getString("title"));

                if (!executorObject.isNull("publishTime"))
                    entiy.setPublishTime(executorObject.getString("publishTime"));

                if (!executorObject.isNull("creator"))
                    entiy.setCreator(executorObject.getString("creator"));

                if (!executorObject.isNull("iconUrl"))
                    entiy.setIconUrl(executorObject.getString("iconUrl"));

                if (!executorObject.isNull("bigIconUrl"))
                    entiy.setBigIconUrl(executorObject.getString("bigIconUrl"));

                if (!executorObject.isNull("content"))
                    entiy.setContent(executorObject.getString("content"));

                if (!executorObject.isNull("viewUrl"))
                    entiy.setViewUrl(executorObject.getString("viewUrl"));

                if (!executorObject.isNull("lastModifiedTime"))
                    entiy.setLastModifiedTime(executorObject.getString("lastModifiedTime"));

                if (!executorObject.isNull("description"))
                    entiy.setDescription(executorObject.getString("description"));

                list.add(entiy);

                AdcolumnUtils.setAdcolumnLastTime(Long.parseLong(list.get(0).getLastModifiedTime()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("宣传栏数据:", e + "," + adcolumn);
            AdcolumnUtils.notiy(0);
        }
        return list;
    }


    public AdcolumnEntiy adcolumnEntiyUpData(AdcolumnEntiy entiy, AdcolumnEntiy update) {
        update.setBelongUserId(Config.getInstance().getUserId());
//        update.setSid(entiy.getSid());
        update.setSourceId(entiy.getSourceId());
        update.setTitleType(entiy.getTitleType());
        update.setTitle(entiy.getTitle());
//        update.setPublishTime(entiy.getPublishTime());
        update.setCreator(entiy.getCreator());
        update.setIconUrl(entiy.getIconUrl());
        update.setBigIconUrl(entiy.getBigIconUrl());
        update.setContent(entiy.getContent());
        update.setViewUrl(entiy.getViewUrl());
        update.setLastModifiedTime(entiy.getLastModifiedTime());
        update.setDescription(entiy.getDescription());
        return update;
    }

    /**
     * 获取广告栏列表
     *
     * @return
     */
    public List<AdcolumnListVO> getAdcolumnListVO(int index, int count) {
        List<AdcolumnListVO> result = new ArrayList<AdcolumnListVO>();
        List<AdcolumnEntiy> list = new ArrayList<AdcolumnEntiy>();
        QueryBuilder<AdcolumnEntiy, String> queryBuilder = dao.queryBuilder();
        try {
            Where<AdcolumnEntiy, String> where = queryBuilder.where();
            where.eq("belongUserId", Config.getInstance().getUserId());
            queryBuilder.offset(index);
            queryBuilder.limit(count);
            queryBuilder.orderByRaw("publishTime DESC");
            list = queryBuilder.query();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AdcolumnEntiy item : list) {
            AdcolumnListVO vo = new AdcolumnListVO();
            vo.setAdcolumnId(item.getSid());
            vo.setDescri(item.getDescription());
            vo.setIconUrl(item.getIconUrl());
            vo.setTitle(item.getTitle());

            vo.setInDate(item.getPublishTime());
            result.add(vo);
        }
        list.clear();
        return result;
    }

}
