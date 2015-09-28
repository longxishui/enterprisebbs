package com.aspirecn.corpsocial.bundle.addrbook.repository;

import android.text.TextUtils;

import com.aspirecn.corpsocial.bundle.addrbook.domain.Dept;
import com.aspirecn.corpsocial.bundle.addrbook.repository.entity.DepartEntity;
import com.aspirecn.corpsocial.common.config.Config;
import com.aspirecn.corpsocial.common.eventbus.EventBusAnnotation;
import com.aspirecn.corpsocial.common.eventbus.SqliteDao;
import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Amos on 15-6-19.
 */
@EventBusAnnotation.Dao(name = "departDao")
public class DepartDao extends SqliteDao<DepartEntity, String> {


    public static final String DEPT_SEPERATOR = ".";

    /**
     * 删除已包含的下级部门，例如list中包括：001, 001.001两个部门，则后者会被删除。
     *
     * @param deptList
     */
    public static void removeChildDeptItems(List<String> deptList) {
        Collections.sort(deptList);

        int i = 0;
        String prior = "";

        while (i < deptList.size()) {
            if (prior.length() > 0) {
                String curr = deptList.get(i);
                if (curr.startsWith(prior + DEPT_SEPERATOR)) {
                    deptList.remove(i);

                    continue;
                }
            }

            prior = deptList.get(i);
            i++;
        }
    }

    public List<Dept> find() throws SQLException {
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        where.eq("belongUserId", Config.getInstance().getUserId());
        List<DepartEntity> entities = queryBuilder.query();
        List<Dept> depts = new ArrayList<Dept>();
        for (DepartEntity entity : entities) {
            Dept dept = new Dept();
            dept.setName(entity.getName());
            dept.setDeptId(entity.getDeptId());
            dept.setCode(entity.getCode());
            dept.setCorpId(entity.getCorpId());
            dept.setLastModifiedTime(entity.getLastModifiedTime());
            dept.setLeaf(entity.getLeaf());
            dept.setPcode(entity.getPcode());
            dept.setCorpType(entity.getCorpType());
            dept.setStatus(entity.getStatus());
            depts.add(dept);
        }
        return depts;
    }

    /**
     * 查找子部门
     *
     * @param deptCode
     * @return
     */
    public List<Dept> findChildren(String deptCode, String corpId) {
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        List<Dept> depts = new ArrayList<Dept>();
        try {
            if (deptCode == null) {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", "0");

            } else {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", deptCode);
            }
            if (corpId != null) {
                where.and().eq("corpId", corpId);
            }
            queryBuilder.orderByRaw("corpId ,sortNo");

            List<DepartEntity> entities = queryBuilder.query();
            for (DepartEntity entity : entities) {
                Dept dept = new Dept();
                dept.setName(entity.getName());
                dept.setDeptId(entity.getDeptId());
                dept.setCode(entity.getCode());
                dept.setCorpId(entity.getCorpId());
                dept.setLastModifiedTime(entity.getLastModifiedTime());
                dept.setLeaf(entity.getLeaf());
                dept.setPcode(entity.getPcode());
                dept.setCorpType(entity.getCorpType());
                dept.setStatus(entity.getStatus());
                depts.add(dept);
            }
        } catch (SQLException e) {
            LogUtil.e("查询下级部门失败", e);
//			e.printStackTrace();
        }

        return depts;
    }

    public List<Dept> findChildren(String pcode) {
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        List<Dept> depts = new ArrayList<Dept>();
        try {

            if (pcode != null) {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", pcode);
            } else {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", "0");
            }
            queryBuilder.orderByRaw("corpId ,sortNo");

            List<DepartEntity> entities = queryBuilder.query();
            for (DepartEntity entity : entities) {
                Dept dept = new Dept();
                dept.setName(entity.getName());
                dept.setDeptId(entity.getDeptId());
                dept.setCode(entity.getCode());
                dept.setCorpId(entity.getCorpId());
                dept.setLastModifiedTime(entity.getLastModifiedTime());
                dept.setLeaf(entity.getLeaf());
                dept.setPcode(entity.getPcode());
                dept.setCorpType(entity.getCorpType());
                dept.setStatus(entity.getStatus());
                depts.add(dept);
            }
        } catch (SQLException e) {
            LogUtil.e("查询下级部门失败", e);
//			e.printStackTrace();
        }

        return depts;
    }

    public List<Dept> findChildren(String pcode, final List<String> coprIds) {
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        List<Dept> depts = new ArrayList<Dept>();
        try {

            if (pcode != null) {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", pcode);
            } else {
                where.eq("belongUserId", Config.getInstance().getUserId());
                where.and();
                where.eq("pcode", "0");
            }
            if (coprIds.size() > 0) {
                where.and().in("corpId", new Iterable<String>() {
                    @Override
                    public Iterator<String> iterator() {
                        return new Iterator<String>() {
                            private int index = 0;

                            public boolean hasNext() {
                                return index < coprIds.size();
                            }

                            public String next() {
                                return coprIds.get(index++);
                            }

                            public void remove() {

                            }
                        };
                    }
                });
            }
            queryBuilder.orderByRaw("corpId ,sortNo");
            LogUtil.i("aspire----" + where.getStatement());

            List<DepartEntity> entities = queryBuilder.query();
            for (DepartEntity entity : entities) {
                Dept dept = new Dept();
                dept.setName(entity.getName());
                dept.setDeptId(entity.getDeptId());
                dept.setCode(entity.getCode());
                dept.setCorpId(entity.getCorpId());
                dept.setLastModifiedTime(entity.getLastModifiedTime());
                dept.setLeaf(entity.getLeaf());
                dept.setPcode(entity.getPcode());
                dept.setCorpType(entity.getCorpType());
                dept.setStatus(entity.getStatus());
                depts.add(dept);
            }
        } catch (SQLException e) {
            LogUtil.e("查询下级部门失败", e);
//			e.printStackTrace();
        }

        return depts;
    }

    /**
     * 根据deptid 获取code
     *
     * @param deptId
     * @return
     */
    public String getCodeByDeptId(String belongUserId, String deptId) {
        if (TextUtils.isEmpty(deptId)) {

            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("belongUserId", belongUserId);
        map.put("deptId", deptId);
//		List<DeptEntity> list = this.findAllByWhere("serverDeptId", serverDeptId, "");
        List<DepartEntity> list = this.findAllByWhere(map, "");
        if (list.size() == 1) {
            return list.get(0).getCode();
        } else {
            return null;
        }
    }

//    public void setupBelongUserId() {
//        List<DepartEntity> entities = findAll();
//        for (DepartEntity entity : entities) {
//            entity.setBelongUserId(Config.getInstance().getUserId());
//            update(entity);
//        }
//    }

    /**
     * 查询部门信息
     */

    public DepartEntity findDeptById(String deptId) {
        if (deptId == null) return null;
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        DepartEntity dept = null;
        try {
            where.eq("belongUserId", Config.getInstance().getUserId());
            where.and();
            where.eq("deptId", deptId);
            dept = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            LogUtil.e("保存部门信息失败", e);
        }
        return dept;
    }

    public DepartEntity findDeptByCode(String code, String corpId) {
        QueryBuilder<DepartEntity, String> queryBuilder = dao.queryBuilder();
        Where<DepartEntity, String> where = queryBuilder.where();
        DepartEntity dept = null;
        try {
            where.eq("belongUserId", Config.getInstance().getUserId());
            where.and();
            where.eq("code", code);
            dept = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            LogUtil.e("查找部门信息失败", e);
        }
        return dept;
    }

    /**
     * 保存部门信息
     */
    public void createOrUpdataDept(Dept item) {

        try {

            DepartEntity dept = findDeptById(item.getDeptId());

            if (dept != null && "1".equals(item.getStatus())) {
                dao.delete(dept);
            } else {
                if (dept == null) {
                    dept = new DepartEntity();
                }
                dept.setPcode(item.getPcode());
                dept.setDeptId(item.getDeptId());
                dept.setCode(item.getCode());
                dept.setName(item.getName());
                dept.setBelongUserId(Config.getInstance().getUserId());
                dept.setSortNo(item.getSortNo());
                dept.setCorpId(item.getCorpId());
                dept.setFullName(item.getFullName());
                if ("0".equals(item.getLeaf()) || "1".equals(item.getLeaf())) {
                    dept.setLeaf(item.getLeaf());
                } else {
                    if ("false".equals(item.getLeaf())) {
                        dept.setLeaf("0");
                    }
                    if ("true".equals(item.getLeaf())) {
                        dept.setLeaf("1");
                    }
                }
                dept.setStatus(item.getStatus());
                dept.setLastModifiedTime(item.getLastModifiedTime());

                dao.createOrUpdate(dept);
            }

        } catch (SQLException e) {
            LogUtil.e("保存部门出错", e);
        }

    }

//    public void deleteCorpDept(final String corpId){
//        executeTransaction(new SqliteDao.TransactionCallback() {
//            @Override
//            public void execute() throws SQLException {
//                DeleteBuilder<DepartEntity, String> builder=dao.deleteBuilder();
//                builder.where().eq("corpId",corpId);
//                builder.delete();
//            }
//        });
//    }

    public void deleteCorpDept(String corpId) throws SQLException {
        DeleteBuilder<DepartEntity, String> builder = dao.deleteBuilder();
        builder.where().eq("corpId", corpId);
        builder.delete();
    }

    public void createOrUpdataDept(final List<Dept> items) {
        executeTransaction(new SqliteDao.TransactionCallback() {
            @Override
            public void execute() throws SQLException {
                for (Dept item : items) {
                    DepartEntity dept = findDeptById(item.getDeptId());

                    if (dept != null && "1".equals(item.getStatus())) {
                        dao.delete(dept);
                    } else {
                        if (dept == null) {
                            dept = new DepartEntity();
                        }
                        dept.setPcode(item.getPcode());
                        dept.setDeptId(item.getDeptId());
                        dept.setCode(item.getCode());
                        dept.setName(item.getName());
                        dept.setBelongUserId(Config.getInstance().getUserId());
                        dept.setSortNo(item.getSortNo());
                        dept.setCorpId(item.getCorpId());
                        dept.setFullName(item.getFullName());
                        if ("0".equals(item.getLeaf()) || "1".equals(item.getLeaf())) {
                            dept.setLeaf(item.getLeaf());
                        } else {
                            if ("false".equals(item.getLeaf())) {
                                dept.setLeaf("0");
                            }
                            if ("true".equals(item.getLeaf())) {
                                dept.setLeaf("1");
                            }
                        }
                        dept.setStatus(item.getStatus());
                        dept.setLastModifiedTime(item.getLastModifiedTime());

                        dao.createOrUpdate(dept);
                    }
                }
            }
        });

    }
}
