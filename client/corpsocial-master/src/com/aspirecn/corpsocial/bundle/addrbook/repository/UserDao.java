package com.aspirecn.corpsocial.bundle.addrbook.repository;

import android.util.Log;

import com.aspirecn.corpsocial.bundle.addrbook.domain.User;
import com.aspirecn.corpsocial.bundle.addrbook.domain.UserDept;
import com.aspirecn.corpsocial.bundle.addrbook.event.vo.SearchParam;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.UserEntity;
import com.aspirecn.corpsocial.bundle.common.domain.UserCorp;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.aspirecn.corpsocial.common.util.StringUtils;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Amos on 15-6-16.
 */
@EventBusAnnotation.Dao(name = "userDao")
public class UserDao extends SqliteDao<UserEntity, String> {

    private static final int MAX_SEARCH_RESULT = 200;
    private static String selectsql = "select belongUserId,isFriend,isSameCorp,userid,corpId,deptId,deptCode,deptName,partTimeDept, duty,name,url,smallUrl,cellphone,spellKey,initialKey,imStatus,signature,lastModifiedTime,corpName,telephone,innerPhone,location,email,sortNo,loginName,status,userType,isFreq from addrbook_user";
    private DepartDao departDao = new DepartDao();

    /**
     * 根据部门Id查询所有联系人(不包括子部门)
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public List<User> find(String belongUserId, List<SearchParam> params, int index, int count, List<String> orders) throws SQLException {

        QueryBuilder<UserEntity, String> queryBuilder = dao.queryBuilder();
        Where<UserEntity, String> condition = queryBuilder.where().eq("belongUserId", belongUserId).and().eq("isTemp", 0);
        if (params != null) {
            for (SearchParam param : params) {
                if (param.getValue() != null) {
                    if (param.getOperate() == 0) {
                        condition = condition.and();
                    } else {
                        condition = condition.or();
                    }

                    if (param.isMisty()) {
                        condition = condition.like(param.getName(), param.getValue() + "%");
                    } else {
                        condition = condition.eq(param.getName(), param.getValue());
                    }
                }
            }
        }
        if (orders != null) {
            for (String order : orders) {
                queryBuilder.orderByRaw(order);
            }
        }

        if (count > 0) {
            queryBuilder.limit(count);
            queryBuilder.offset(index);
        }
        List<UserEntity> entities = queryBuilder.query();
        List<User> users = new ArrayList<User>();
        for (UserEntity entity : entities) {
            User user = build(entity);
            users.add(user);
        }

        return users;
    }

    /**
     * 根据关键字搜索联系人
     *
     * @param keyword
     * @return
     */
    public List<User> findByKeyWord(String belongUserId, String keyword, String deptId, String corpId, int isFriend, String sort, int type) throws SQLException {
        String sql = selectsql + " where belongUserId='" + belongUserId + "' and isTemp=0";

        if (deptId != null && deptId.length() > 0) {
            sql = sql + " and deptId ='" + deptId + "'";
        }
        if (corpId != null && corpId.length() > 0) {
            sql = sql + " and corpId='" + corpId + "'";
        }
        if (keyword != null && keyword.length() > 0) {
            String key = "%" + keyword + "%";
            sql = sql + " and (name like '" + key + "' or ( length('" + keyword + "') > 3 and cellphone like '" + key + "') or spellKey= '" + keyword.toLowerCase() + "' or initialKey = '" + keyword.toLowerCase() + "' )";
        }
//        if (isFriend >= 0) {
//            sql = sql + " and isFriend>=" + isFriend;
//
//        }
        if (isFriend == 0) {
            sql = sql + " and ((isFriend=0 and isSameCorp=1) or isFriend>=1)";
        } else if (isFriend == 1) {
            sql = sql + " and isFriend>=" + isFriend;
        }

        if (type > 0) {
            if (type == 1) {
                sql = sql + " and isSameCorp=1";
            } else if (type == 2) {
                sql = sql + " and isSameCorp=0";
            }
        }


        if (sort != null) {
            sql = sql + " order by " + sort;
        } else {
            sql = sql + " order by corpId,deptCode,name ";
        }

        GenericRawResults<UserEntity> results = dao.queryRaw(sql, new RawRowMapper<UserEntity>() {
            @Override
            public UserEntity mapRow(String[] columns, String[] resultColumns) throws SQLException {
                return buildEntity(resultColumns);
            }
        });

        List<User> users = new ArrayList<User>();
        Iterator<UserEntity> iterator = results.iterator();
        while (iterator.hasNext()) {
            UserEntity entity = iterator.next();
            User user = build(entity);
            users.add(user);

        }

        return users;
    }


    private UserEntity buildEntity(String[] resultColumns) {
        UserEntity entity = new UserEntity();
        entity.setCorpId(resultColumns[4]);
        entity.setBelongUserId(resultColumns[0]);
        entity.setIsFriend(Integer.parseInt(resultColumns[1]));
        entity.setIsSameCorp(Integer.parseInt(resultColumns[2]));
        entity.setUserid(resultColumns[3]);
        entity.setDeptId(resultColumns[5]);
        entity.setDeptCode(resultColumns[6]);
        entity.setDeptName(resultColumns[7]);
        entity.setPartTimeDept(resultColumns[8]);
        entity.setInitialKey(resultColumns[15]);
        entity.setImStatus(resultColumns[16]);
        entity.setSignature(resultColumns[17]);
        entity.setSpellKey(resultColumns[14]);
        entity.setDuty(resultColumns[9]);
        entity.setName(resultColumns[10]);
        entity.setUrl(resultColumns[11]);
        entity.setSmallUrl(resultColumns[12]);
        entity.setCellphone(resultColumns[13]);
        entity.setLastModifiedTime(Long.parseLong(resultColumns[18]));
        entity.setCorpName(resultColumns[19]);
        entity.setTelephone(resultColumns[20]);
        entity.setInnerPhone(resultColumns[21]);
        entity.setEmail(resultColumns[23]);
        entity.setSortNo(resultColumns[24]);
        entity.setLoginName(resultColumns[25]);
        entity.setStatus(resultColumns[26]);
        entity.setUserType(resultColumns[27]);
        entity.setIsFreq(Integer.parseInt(resultColumns[28]));

        return entity;
    }

    public List<User> findFilterByContactIds(String belongUserId, List<String> ids) throws SQLException {
        List<User> users = _findByField(belongUserId, "userid", ids);
        String cur = null;
        for (int i = users.size() - 1; i >= 0; i--) {
            if (users.get(i).getUserid().equals(cur)) {
                users.remove(i);
            } else {
                cur = users.get(i).getUserid();
            }
        }
        return users;
    }

    public List<User> filter(List<User> users) {
        String cur = null;
        for (int i = users.size() - 1; i >= 0; i--) {
            if (users.get(i).getUserid().equals(cur)) {
                users.remove(i);
            } else {
                cur = users.get(i).getUserid();
            }
        }
        return users;
    }

    public List<User> merge(List<User> users) {
        Map<String, User> map = new HashMap();
        for (User user : users) {
            if (map.containsKey(user.getUserid())) {
                User euser = map.get(user.getUserid());
                UserCorp uc = new UserCorp();
                uc.setCorpName(user.getCorpName());
                uc.setCorpId(user.getCorpId());
                euser.getCorpList().add(uc);
            } else {
                map.put(user.getUserid(), user);
                List<UserCorp> ucs = new ArrayList<UserCorp>();
                UserCorp uc = new UserCorp();
                uc.setCorpName(user.getCorpName());
                uc.setCorpId(user.getCorpId());
                ucs.add(uc);
                user.setCorpList(ucs);
            }
        }
        List<User> r = new ArrayList();
        r.addAll(map.values());
        return r;

    }

    /**
     * 根据联系人的ID列表，查询联系人。
     *
     * @param ids
     * @return
     */
    public List<User> findByContactIds(String belongUserId, List<String> ids) throws SQLException {
        return _findByField(belongUserId, "userid", ids);
    }

    /**
     * 根据联系人的loginName列表，查询联系人。
     *
     * @param loginNames
     * @return
     */
    public List<User> findByContactLoginNames(String belongUserId, List<String> loginNames) throws SQLException {
        return _findByField(belongUserId, "loginName", loginNames);
    }

    /**
     * 根据字段名和字段值列表，查询联系人。
     *
     * @param fieldValues 字段值的列表，例如id或者 loginName的列表
     * @param fieldName   字段名
     * @return
     */
    private List<User> _findByField(String belongUserId, String fieldName, final List<String> fieldValues) throws SQLException {

        QueryBuilder<UserEntity, String> queryBuilder = dao.queryBuilder();
        Where<UserEntity, String> condition = queryBuilder.where().eq("belongUserId", belongUserId);
        queryBuilder.orderByRaw(fieldName);

        //condition.and().in(fieldName, fieldValues.toArray());
//        condition.and();
//        try {
//
//            Method method = Where.class.getDeclaredMethod("in", Array.newInstance(String.class, fieldValues.size()+1).getClass());
//            Object values=Array.newInstance(String.class,fieldValues.size()+1);
//            Array.set(values,0,fieldName);
//            for(int i=0;i<fieldValues.size();i++){
//                Array.set(values,i+1,fieldValues.get(i));
//
//            }
//            method.invoke(condition, values);
//        }catch(Exception e){
//            e.printStackTrace();
//            LogUtil.e("aspire-----"+e.toString());
//        }

        condition.and().in(fieldName, new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int index = 0;

                    public boolean hasNext() {
                        return index < fieldValues.size();
                    }

                    public String next() {
                        return fieldValues.get(index++);
                    }

                    public void remove() {

                    }
                };
            }
        });
        //LogUtil.i("aspire-----"+condition.getStatement());
        List<UserEntity> entities = queryBuilder.query();
        List<User> users = new ArrayList<User>();
        for (UserEntity entity : entities) {
            User user = build(entity);
            users.add(user);
        }

        return users;


    }

    public int getCount() throws SQLException {
        String sql = "select count(*) as count from addrbook_user where belongUserId='" + Config.getInstance().getUserId() + "'";

        sql = sql + " and isTemp=0 ";
        int count = 0;
        GenericRawResults<Integer> results = dao.queryRaw(sql, new RawRowMapper<Integer>() {
            @Override
            public Integer mapRow(String[] columns, String[] resultColumns) throws SQLException {
                int count = Integer.parseInt(resultColumns[0]);
                return count;
            }
        });

        Iterator<Integer> it = results.iterator();
        while (it.hasNext()) {
            count = it.next();
        }
        return count;
    }

    private User build(UserEntity entity) {
        User user = new User();
        user.setName(entity.getName());
        user.setCellphone(entity.getCellphone());
        user.setUserid(entity.getUserid());
        user.setName(entity.getName());
        user.setSmallUrl(entity.getSmallUrl());
        user.setUrl(entity.getUrl());
        user.setImStatus(entity.getImStatus());
        user.setCorpId(entity.getCorpId());
        user.setCorpName(entity.getCorpName());
        user.setDeptId(entity.getDeptId());
        user.setDeptCode(entity.getDeptCode());
        user.setDeptName(entity.getDeptName());
        user.setInitialKey(entity.getInitialKey());
        user.setSpellKey(entity.getSpellKey());
        user.setIsSameCorp(entity.getIsSameCorp());
        user.setSignature(entity.getSignature());
        user.setEmail(entity.getEmail());
        user.setIsFreq(entity.getIsFreq());
        if (user.getDeptCode() != null && user.getDeptCode().indexOf(".") > 0) {
            user.setpDeptCode(user.getDeptCode().substring(0, user.getDeptCode().lastIndexOf(".")));
        }
        user.setDeptName(entity.getDeptName());
        user.setDuty(entity.getDuty());
        user.setIsTemp(entity.getIsTemp());
        user.setIsFriend(entity.getIsFriend());
        String ptds = entity.getPartTimeDept();
        if (ptds != null) {
            String ptd[] = ptds.split(";");
            List<UserDept> uds = new ArrayList<UserDept>();
            for (int i = 0; i < ptd.length; i++) {
                if (ptd[i].length() > 0) {
                    UserDept ud = new UserDept();
                    //部门编码_部门ID_部门名称_职务，如果有多个则以;分隔。
                    String dept[] = ptd[i].split("_");

                    ud.setCode(dept[0]);
                    ud.setDeptId(dept[1]);
                    ud.setName(dept[2]);
                    if (dept.length == 4) {
                        ud.setDuty(dept[3]);
                    }
                    uds.add(ud);
                }
            }
            user.setDepts(uds);

        }

        return user;
    }

    @Deprecated
    public List<User> findUsers(String belongUserId, List<String> userIds) throws SQLException {
        return _findByField(belongUserId, "userid", userIds);

    }


    /**
     * 根据Id查询联系人
     *
     * @return
     */
    public UserEntity findByUserId(String userId, String corpId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        User user = null;
        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userId).and().eq("corpId", corpId);

            UserEntity entity = query.queryForFirst();
//            if(entity!=null){
//                user=build(entity);
//            }
            return entity;
        } catch (SQLException e) {
            LogUtil.e("根据Id查询联系人出错", e);
        }

        return null;
    }

    public List<UserEntity> findMutiByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userId);

            List<UserEntity> entities = query.query();

            return entities;
        } catch (SQLException e) {
            LogUtil.e("根据Id查询联系人出错", e);
        }

        return null;
    }

    public List<User> findFrequent(String keyword) {

        List<User> users=new ArrayList<>();
        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("isFreq", 1);
            if (keyword != null && keyword.length() > 0) {
                String key = "%" + keyword + "%";
                where.and().raw(" (name like '" + key + "' or ( length('" + keyword + "') > 3 and cellphone like '" + key + "') or spellKey= '" + keyword.toLowerCase() + "' or initialKey = '" + keyword.toLowerCase() + "' )");

            }
            query.orderByRaw("spellKey ");

            List<UserEntity> entities = query.query();
            for(UserEntity entity:entities){
                User user=build(entity);
                users.add(user);
            }

        } catch (SQLException e) {
            LogUtil.e("根据Id查询联系人出错", e);
        }

        return users;
    }


    public List<User> findByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<User> users = new ArrayList<User>();
        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userId);

            List<UserEntity> entities = query.query();
            for (UserEntity entity : entities) {
                users.add(build(entity));
            }

        } catch (SQLException e) {
            Log.e("根据Id查询联系人出错", e.getMessage());
        }

        return users;
    }

    public List<UserEntity> findEntityByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();
        try {
            where.eq("belongUserId", Config.getInstance().getUserId()).and().eq("userid", userId);

            return query.query();


        } catch (SQLException e) {
            Log.e("根据Id查询联系人出错", e.getMessage());
        }

        return null;
    }

    public User findUserDetail(String userId, String deptId, String corpId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        QueryBuilder<UserEntity, String> query = dao.queryBuilder();
        Where<UserEntity, String> where = query.where();

        try {
            where.eq("belongUserId", Config.getInstance().getUserId());
            where.and();
            where.eq("userid", userId);
            if (deptId != null && deptId.length() > 0) {
                where.and();
                where.eq("deptId", deptId);
            }
            if (corpId != null && corpId.length() > 0) {
                where.and();
                where.eq("corpId", corpId);
            }
            List<UserEntity> entities = query.query();
            User user = null;
            for (UserEntity entity : entities) {
                if (user == null) {
                    user = build(entity);
                    List<UserCorp> ucs = new ArrayList<UserCorp>();
                    UserCorp uc = new UserCorp();
                    uc.setCorpName(user.getCorpName());
                    uc.setCorpId(user.getCorpId());
                    uc.setEmail(user.getEmail());
                    UserDept mainDept = new UserDept();

                    mainDept.setDeptId(user.getDeptId());
                    mainDept.setCode(user.getDeptCode());
                    mainDept.setName(user.getDeptName());
                    mainDept.setDuty(user.getDuty());

                    uc.setMainDept(mainDept);
                    ucs.add(uc);
                    user.setCorpList(ucs);

                } else {
                    User tuser = build(entity);
                    UserCorp uc = new UserCorp();
                    uc.setCorpName(tuser.getCorpName());
                    uc.setCorpId(tuser.getCorpId());
                    uc.setEmail(tuser.getEmail());
                    UserDept mainDept = new UserDept();
                    mainDept.setDeptId(tuser.getDeptId());
                    mainDept.setCode(tuser.getDeptCode());
                    mainDept.setName(tuser.getDeptName());
                    mainDept.setDuty(tuser.getDuty());
                    uc.setMainDept(mainDept);
                    user.getCorpList().add(uc);
                }
            }
//            UserEntity user=query.queryForFirst();
//            return this.build(user);
            return user;
        } catch (SQLException e) {
            LogUtil.e("根据Id查询联系人出错", e);
        }
        return null;
    }

    public User findUserDetail(String userId) {
        return findUserDetail(userId, null, null);
    }


    public void createOrUpdataFriend(final List<User> users) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {

                    for (UserCorp uc : user.getCorpList()) {
                        UserEntity entity = findByUserId(user.getUserid(), uc.getCorpId());
                        if (entity != null && "1".equals(uc.getStatus())) {
                            dao.delete(entity);
                        } else if (entity == null && "1".equals(uc.getStatus())) {
                            continue;
                        } else if (entity != null && entity.getIsFriend() == 0) {
                            entity.setIsFriend(1);
                            dao.update(entity);
                        } else if (entity != null && entity.getIsFriend() == 1) {
                            entity.setCellphone(user.getCellphone());
                            entity.setCorpId(uc.getCorpId());
                            entity.setCorpName(uc.getCorpName());
                            entity.setLoginName(uc.getLoginName());
                            entity.setSpellKey(user.getSpellKey());
                            entity.setSignature(user.getSignature());
                            entity.setUrl(user.getUrl());
                            entity.setSmallUrl(user.getSmallUrl());
                            entity.setUserid(user.getUserid());
                            entity.setInitialKey(user.getInitialKey());
                            entity.setSortNo(uc.getSortNo());
                            entity.setInnerPhone(uc.getInnerPhone());
                            entity.setStatus(uc.getStatus());
                            entity.setImStatus(user.getImStatus());
                            entity.setLastModifiedTime(user.getLastModifiedTime());
                            entity.setEmail(uc.getEmail());
                            entity.setIsFriend(user.getIsFriend());
                            entity.setUserType(uc.getUserType());
                            List<UserDept> depts = uc.getDeptList();
                            StringBuilder builder = new StringBuilder();

                            for (int i = 0; i < depts.size(); i++) {
                                if (i == 0) {
                                    entity.setDuty(depts.get(i).getDuty());
                                    entity.setDeptId(depts.get(i).getDeptId());
                                    entity.setDeptName(depts.get(i).getName());
                                    entity.setDeptCode(depts.get(i).getCode());

                                } else {
                                    if (depts.get(i).getStatus().equals("1"))
                                        continue;
                                    builder.append(depts.get(i).getCode() + "_" + depts.get(i).getDeptId() + "_" + depts.get(i).getName() + "_" + depts.get(i).getDuty()).append(";");
                                }
                            }

                            entity.setPartTimeDept(builder.toString());
                            dao.createOrUpdate(entity);

                        } else {
                            if (entity == null) {
                                entity = new UserEntity();
                            }
                            entity.setIsFriend(user.getIsFriend());
                            entity.setName(user.getName());
                            entity.setBelongUserId(Config.getInstance().getUserId());
                            entity.setCellphone(user.getCellphone());
                            entity.setCorpId(uc.getCorpId());
                            entity.setCorpName(uc.getCorpName());
                            entity.setLoginName(uc.getLoginName());
                            entity.setSpellKey(user.getSpellKey());
                            entity.setSignature(user.getSignature());
                            entity.setUrl(user.getUrl());
                            entity.setSmallUrl(user.getSmallUrl());
                            entity.setUserid(user.getUserid());
                            entity.setInitialKey(user.getInitialKey());
                            entity.setSortNo(uc.getSortNo());
                            entity.setInnerPhone(uc.getInnerPhone());
                            entity.setStatus(uc.getStatus());
                            entity.setImStatus(user.getImStatus());
                            entity.setLastModifiedTime(user.getLastModifiedTime());
                            entity.setIsTemp(user.getIsTemp());
                            entity.setEmail(uc.getEmail());
                            entity.setIsFriend(user.getIsFriend());
                            entity.setIsSameCorp(0);
                            entity.setUserType(uc.getUserType());
                            List<UserDept> depts = uc.getDeptList();
                            StringBuilder builder = new StringBuilder();

                            for (int i = 0; i < depts.size(); i++) {
                                if (i == 0) {
                                    entity.setDuty(depts.get(i).getDuty());
                                    entity.setDeptId(depts.get(i).getDeptId());
                                    entity.setDeptName(depts.get(i).getName());
                                    entity.setDeptCode(depts.get(i).getCode());

                                } else {
                                    if (depts.get(i).getStatus().equals("1"))
                                        continue;
                                    builder.append(depts.get(i).getCode() + "_" + depts.get(i).getDeptId() + "_" + depts.get(i).getName() + "_" + depts.get(i).getDuty()).append(";");
                                }
                            }

                            entity.setPartTimeDept(builder.toString());
                            dao.createOrUpdate(entity);
                        }
                    }
                }
            }
        });
    }


    public void createContact(final List<User> users) {

        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {
                    user.setIsSameCorp(1);
                    for (UserCorp uc : user.getCorpList()) {
                        UserEntity entity = null;
                        if (user.getUserid().equals(Config.getInstance().getUserId())) {
                            entity = findByUserId(user.getUserid(), uc.getCorpId());
                        }
                        if (entity != null) {
                            continue;
                        }

                        if ("1".equals(uc.getStatus())) {
                            continue;
                        } else {
                            if (entity == null) {
                                entity = new UserEntity();
                            }

                            entity.setName(user.getName());
                            entity.setBelongUserId(Config.getInstance().getUserId());
                            entity.setCellphone(user.getCellphone());
                            entity.setCorpId(uc.getCorpId());
                            entity.setCorpName(uc.getCorpName());
                            entity.setLoginName(uc.getLoginName());
                            entity.setEmail(uc.getEmail());
                            entity.setSpellKey(user.getSpellKey());
                            entity.setSignature(user.getSignature());
                            entity.setUrl(user.getUrl());
                            entity.setSmallUrl(user.getSmallUrl());
                            entity.setUserid(user.getUserid());
                            entity.setInitialKey(user.getInitialKey());
                            entity.setSortNo(uc.getSortNo());
                            entity.setInnerPhone(uc.getInnerPhone());
                            entity.setStatus(uc.getStatus());
                            entity.setImStatus(user.getImStatus());
                            entity.setLastModifiedTime(user.getLastModifiedTime());
                            entity.setIsTemp(user.getIsTemp());
                            entity.setIsFriend(user.getIsFriend());
                            //entity.setIsFriend(user.getIsFriend());
                            entity.setIsSameCorp(user.getIsSameCorp());
                            entity.setUserType(uc.getUserType());
                            List<UserDept> depts = uc.getDeptList();
                            StringBuilder builder = new StringBuilder();

                            for (int i = 0; i < depts.size(); i++) {

                                if (i == 0) {
                                    entity.setDuty(depts.get(i).getDuty());
                                    entity.setDeptId(depts.get(i).getDeptId());
                                    entity.setDeptName(depts.get(i).getName());
                                    entity.setDeptCode(depts.get(i).getCode());

                                } else {
                                    if (depts.get(i).getStatus().equals("1"))
                                        continue;
                                    builder.append(depts.get(i).getCode() + "_" + depts.get(i).getDeptId() + "_" + depts.get(i).getName() + "_" + depts.get(i).getDuty()).append(";");
                                }
                            }

                            entity.setPartTimeDept(builder.toString());

                            dao.create(entity);
                        }
                    }
                }
            }
        });

    }

    public void createOrUpdataContact(final List<User> users) {

        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {
                    //user.setIsSameCorp(1);
                    for (UserCorp uc : user.getCorpList()) {
                        UserEntity entity = findByUserId(user.getUserid(), uc.getCorpId());
                        if (entity != null && "1".equals(uc.getStatus())) {
                            dao.delete(entity);
                        } else if (entity == null && "1".equals(uc.getStatus())) {
                            continue;
                        } else {

                            if (entity == null) {
                                entity = new UserEntity();
                                List<UserEntity> entities = findMutiByUserId(user.getUserid());
                                if (entities != null && entities.size() > 0) {
                                    entity.setIsFriend(entities.get(0).getIsFriend());
                                }
                            }

                            entity.setName(user.getName());
                            entity.setBelongUserId(Config.getInstance().getUserId());
                            entity.setCellphone(user.getCellphone());
                            entity.setCorpId(uc.getCorpId());
                            entity.setCorpName(uc.getCorpName());
                            entity.setLoginName(uc.getLoginName());
                            entity.setSpellKey(user.getSpellKey());
                            entity.setSignature(user.getSignature());
                            entity.setUrl(user.getUrl());
                            entity.setSmallUrl(user.getSmallUrl());
                            entity.setEmail(uc.getEmail());
                            entity.setUserid(user.getUserid());
                            entity.setInitialKey(user.getInitialKey());
                            entity.setSortNo(uc.getSortNo());
                            entity.setInnerPhone(uc.getInnerPhone());
                            entity.setStatus(uc.getStatus());
                            entity.setImStatus(user.getImStatus());
                            entity.setLastModifiedTime(user.getLastModifiedTime());
                            entity.setIsTemp(user.getIsTemp());
                            entity.setUserType(uc.getUserType());

                            //entity.setIsFriend(user.getIsFriend());
                            entity.setIsSameCorp(user.getIsSameCorp());
                            List<UserDept> depts = uc.getDeptList();
                            StringBuilder builder = new StringBuilder();

                            for (int i = 0; i < depts.size(); i++) {

                                if (i == 0) {
                                    entity.setDuty(depts.get(i).getDuty());
                                    entity.setDeptId(depts.get(i).getDeptId());
                                    entity.setDeptName(depts.get(i).getName());
                                    entity.setDeptCode(depts.get(i).getCode());

                                } else {
                                    if (depts.get(i).getStatus().equals("1"))
                                        continue;
                                    builder.append(depts.get(i).getCode() + "_" + depts.get(i).getDeptId() + "_" + depts.get(i).getName() + "_" + depts.get(i).getDuty()).append(";");
                                }
                            }

                            entity.setPartTimeDept(builder.toString());

                            dao.createOrUpdate(entity);
                        }
                    }
                }
            }
        });

    }


    public void updateContact(final User user) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {

                List<UserEntity> entities = findEntityByUserId(user.getUserid());

                for (UserEntity entity : entities) {
                    entity.setName(user.getName());
                    entity.setSignature(user.getSignature());
                    entity.setUrl(user.getUrl());
                    entity.setSmallUrl(user.getSmallUrl());
                    entity.setLastModifiedTime(user.getLastModifiedTime());

                    dao.update(entity);
                }

            }
        });

    }

    public void updateSignature(final User user) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {

                List<UserEntity> entities = findEntityByUserId(user.getUserid());

                for (UserEntity entity : entities) {
                    entity.setSignature(user.getSignature());

                    dao.update(entity);
                }

            }
        });

    }

    public void createOrUpdataContact(final User user) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {

                for (UserCorp uc : user.getCorpList()) {

                    UserEntity entity = findByUserId(user.getUserid(), uc.getCorpId());
                    if (entity != null && "1".equals(uc.getStatus())) {
                        dao.delete(entity);
                    } else {
                        if (entity == null) {
                            entity = new UserEntity();
                        }
                        entity.setIsFriend(user.getIsFriend());
                        entity.setName(user.getName());
                        entity.setBelongUserId(Config.getInstance().getUserId());
                        entity.setCellphone(user.getCellphone());
                        entity.setCorpId(uc.getCorpId());
                        entity.setCorpName(uc.getCorpName());
                        entity.setLoginName(uc.getLoginName());
                        entity.setSpellKey(user.getSpellKey());
                        entity.setSignature(user.getSignature());
                        entity.setEmail(uc.getEmail());
                        entity.setUrl(user.getUrl());
                        entity.setSmallUrl(user.getSmallUrl());
                        entity.setUserid(user.getUserid());
                        entity.setInitialKey(user.getInitialKey());
                        entity.setSortNo(uc.getSortNo());
                        entity.setInnerPhone(uc.getInnerPhone());
                        entity.setStatus(uc.getStatus());
                        entity.setImStatus(user.getImStatus());
                        entity.setLastModifiedTime(user.getLastModifiedTime());
                        entity.setIsTemp(user.getIsTemp());
                        entity.setIsFriend(user.getIsFriend());
                        entity.setIsSameCorp(user.getIsSameCorp());
                        entity.setUserType(uc.getUserType());
                        List<UserDept> depts = uc.getDeptList();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < depts.size(); i++) {
                            if (i == 0) {
                                entity.setDuty(depts.get(i).getDuty());
                                entity.setDeptId(depts.get(i).getDeptId());
                                entity.setDeptName(depts.get(i).getName());
                                entity.setDeptCode(depts.get(i).getCode());
                            } else {
                                if (depts.get(i).getStatus().equals("1"))
                                    continue;
                                builder.append(depts.get(i).getCode() + "_" + depts.get(i).getDeptId() + "_" + depts.get(i).getName() + "_" + depts.get(i).getDuty()).append(";");
                            }
                        }

                        entity.setPartTimeDept(builder.toString());
                        dao.createOrUpdate(entity);
                    }
                }
            }
        });
    }

    public void updateToFrequent(final List<User> users) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {

                    UserEntity entity = findByUserId(user.getUserid(), user.getCorpId());
                    if (entity != null) {
                        entity.setIsFreq(1);
                        dao.update(entity);
                    }
                }
            }
        });

    }

    public void cancelFrequent(final List<User> users) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {

                    UserEntity entity = findByUserId(user.getUserid(), user.getCorpId());
                    if (entity != null) {
                        entity.setIsFreq(0);
                        dao.update(entity);
                    }
                }
            }
        });

    }

    public void cancelFrequent(final User user) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {


                    UserEntity entity = findByUserId(user.getUserid(), user.getCorpId());
                    if (entity != null) {
                        entity.setIsFreq(0);
                        dao.update(entity);
                    }

            }
        });

    }

    public void cancelAllFrequent() {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                dao.executeRaw("update addrbook_user set isFreq=0 where belongUserId='"+Config.getInstance().getUserId()+"'");

            }
        });

    }

    public void updateToFriend(final List<User> users) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (User user : users) {

                    UserEntity entity = findByUserId(user.getUserid(), user.getCorpId());
                    if (entity != null) {
                        entity.setIsTemp(0);
                        entity.setIsFriend(1);
                        dao.update(entity);
                    }
                }
            }
        });

    }

    public void cancelFriend(final String userid) {

        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                List<UserEntity> entities = findMutiByUserId(userid);
                for (UserEntity entity : entities) {
                    if (entity.getIsSameCorp() == 1) {
                        entity.setIsFriend(0);
                        dao.update(entity);
                    } else {
                        dao.delete(entity);
                    }
                }

            }
        });

    }

    public void canceledFriend(final String userid) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                List<UserEntity> entities = findMutiByUserId(userid);
                for (UserEntity entity : entities) {
                    if (entity.getIsFriend() == 1) {
                        entity.setIsFriend(2);
                        dao.update(entity);
                    }
                }
            }
        });
    }

    public void delete(String contactId, String corpId) throws SQLException {
        UserEntity user = this.findByUserId(contactId, corpId);
        dao.delete(user);
    }
}
