package com.aspirecn.corpsocial.bundle.addrbook.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.CustomerServiceEntity;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.FrequentlyContactType;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.ContactBriefVO;
import com.aspirecn.corpsocial.bundle.addrbook.repository.vo.FrequentlyContactVO;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.DatabaseHelper;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation.Dao;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.aspirecn.corpsocial.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by chenziqiang on 15-1-14.
 */
@Dao(name = "customer_service")
public class CustomerServiceDao extends SqliteDao<CustomerServiceEntity, String> {

    private static final String CUSTOMERSERVICE = "customer_service";

    private static final String[] FIELDS = {"id", "belongUserId", "loginName", "name",
            "jobNumber", "gender", "deptName", "deptId", "signature",
            "mobilePhone", "mobilePhone2", "phoneNumber", "innerPhoneNumber",
            "email", "birthday", "headImageUrl", "headLocalImageUrl", "pinyin",
            "initialCode", "hidePhone", "corpId", "duty", "note", "location",
            "imStatus", "partTimeDept", "sortNo", "userType"};

    private static final String REPLACE_SQL;
    private static final String DELETE_SQL = "delete from customer_service where id = ?";
    private static final Map<String, Integer> FIELD_POSITION;

    static {
        FIELD_POSITION = new HashMap<String, Integer>();
        StringBuilder nameBuilder = new StringBuilder("");
        StringBuilder valueBuilder = new StringBuilder("");
        for (int i = 0; i < FIELDS.length; i++) {
            if (i > 0) {
                nameBuilder.append(",");
                valueBuilder.append(",");
            }
            nameBuilder.append(FIELDS[i]);
            valueBuilder.append("?");

            FIELD_POSITION.put(FIELDS[i].toLowerCase(), Integer.valueOf(i + 1));
        }

        REPLACE_SQL = String.format(
                "replace into customer_service(%s) values(%s)",
                nameBuilder.toString(), valueBuilder.toString());
    }


    private void deleteContact(SQLiteStatement statement, String id) {
        statement.clearBindings();

        statement.bindString(1, id);

        statement.execute();
    }

    private void bindStringParameter(SQLiteStatement statement,
                                     String paramName, String value) {
        statement.bindString(getFieldPosition(paramName), value);
    }

    private int getFieldPosition(String fieldName) {
        Integer position = FIELD_POSITION.get(fieldName.toLowerCase());
        if (position == null) {
            return -1;
        }

        return position.intValue();
    }

    public List<FrequentlyContactVO> findCustomerService(String belongUserId) {
        List<FrequentlyContactVO> mList;
        DatabaseHelper helper = DatabaseHelper.getInstance();
        SQLiteDatabase db = helper.getReadableDatabase();

        StringBuilder sb = new StringBuilder(getBriefSelectSql(belongUserId));

        Cursor cursor = db.rawQuery(sb.toString(), null);
        mList = inflateList(cursor);
        return mList;
    }


    private String getBriefSelectSql(String belongUserId) {
        StringBuilder sb = new StringBuilder("select ");
        boolean has = false;
        for (String fieldName : ContactBriefVO.FIELD_ARRAYS) {
            if (has) {
                sb.append(",");
            }
            sb.append(fieldName);

            has = true;
        }
        sb.append(" from customer_service");
        sb.append(" where ");
        sb.append(String.format(" belongUserId = '%s' ", belongUserId));

        return sb.toString();
    }

    private List<FrequentlyContactVO> inflateList(Cursor cursor) {
        List<FrequentlyContactVO> list = new ArrayList<FrequentlyContactVO>();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                FrequentlyContactVO.Builder c = new FrequentlyContactVO.Builder();
//                FrequentlyContactVO c = new FrequentlyContactVO();
                c.contactId(cursor.getString(ContactBriefVO.FIELD_INDEX.get("id")));
                c.name(cursor.getString(ContactBriefVO.FIELD_INDEX.get("name")));
                c.signature(cursor.getString(ContactBriefVO.FIELD_INDEX.get("signature")));
                c.type(FrequentlyContactType.getInstance(2));
                c.deptName(cursor.getString(ContactBriefVO.FIELD_INDEX.get("deptname")));


                list.add(c.build());

                cursor.moveToNext();
            }
        } catch (Exception e) {
            LogUtil.e(String.format("将查询出的联系人转换成ContactBriefVO时出错：%s", e.getMessage()), e);
        } finally {
            cursor.close();
        }

        return list;
    }

    public void setupBelongUserId() {
        List<CustomerServiceEntity> entities = findAll();
        for (CustomerServiceEntity entity : entities) {
            entity.setBelongUserId(Config.getInstance().getUserId());
            update(entity);
        }
    }


//    public static boolean isCustomerService(String id) {
//        DatabaseHelper helper = DatabaseHelper.getInstance();
//        SQLiteDatabase db = helper.getReadableDatabase();
//        StringBuilder sb = new StringBuilder("select * from " + CUSTOMERSERVICE + " where id = " + id);
//        Cursor cursor = db.rawQuery(sb.toString(), null);
//        if (cursor.getCount() > 0)
//            return true;
//        else
//            return false;
//    }
}
