/**
 * @(#) BaseDao.java Created on 2012-11-11
 * <p/>
 * Copyright (c) 2012 Aspire. All Rights Reserved
 */
package com.aspirecn.corpsocial.common.eventbus;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.aspirecn.corpsocial.common.util.LogUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <T>
 * @param <ID>
 * @author lizhuo_a
 */
public class SqliteDao<T, ID> {

    protected Dao<T, ID> dao;

    private OrmLiteSqliteOpenHelper databaseHelper;

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    public SqliteDao() {

        Type mySuperClass = this.getClass().getGenericSuperclass();
        Type entityType = ((ParameterizedType) mySuperClass)
                .getActualTypeArguments()[0];
        clazz = (Class<T>) entityType;

        databaseHelper = DatabaseHelper.getInstance();
        try {
            dao = databaseHelper.getDao(clazz);
        } catch (SQLException e) {
            LogUtil.e("创建数据类异常", e);
        }
    }

    public List<T> findAll() {
        List<T> result = new ArrayList<T>();

        try {
            result = dao.queryForAll();
        } catch (SQLException e) {
            LogUtil.e("查询全部数据异常", e);
        }
        return result;

    }

    /**
     * @param orderBy
     * @return
     */
    public List<T> findAll(String orderBy) {
        List<T> result = new ArrayList<T>();

        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
        queryBuilder.orderByRaw(orderBy);
        try {
            result = queryBuilder.query();
            return result;
        } catch (SQLException e) {
            LogUtil.e("排序查询全部数据异常", e);
        }
        return result;
    }

    /**
     * 单条件查询列表
     *
     * @param columnName
     * @param value
     * @return
     */
    public List<T> findAllByWhere(String columnName, String value,
                                  String orderBy) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
        try {
            queryBuilder.where().eq(columnName, value);
            if (!TextUtils.isEmpty(orderBy)) {

                queryBuilder.orderByRaw(orderBy);
            }

            return queryBuilder.query();
        } catch (SQLException e) {
            LogUtil.e("单条件排序查询全部数据异常", e);
        }
        return null;
    }

    public List<T> findAllByWhere(Map<String, Object> columns) {
        return findAllByWhere(columns, "");
    }

    /**
     * 多条件查询列表
     *
     * @param columns
     * @return
     */
    public List<T> findAllByWhere(Map<String, Object> columns, String orderBy) {
        List<T> result = new ArrayList<T>();
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        Set<String> keys = columns.keySet();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String columnName = it.next();
                Object value = columns.get(columnName);
                if (!inited) {
                    where.eq(columnName, value);
                    inited = true;
                } else {
                    where.and().eq(columnName, value);
                }
            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            result = queryBuilder.query();
            return result;
        } catch (SQLException e) {
            LogUtil.e("多条件排序查询全部数据异常", e);
        }

        return result;
    }

    /**
     * 多条件分页查询列表
     *
     * @param columns
     * @param index
     * @param count
     * @param orderBy
     * @return
     */
    @SuppressWarnings("deprecation")
    public List<T> findAllByWhereAndIndex(Map<String, Object> columns,
                                          int index, int count, String orderBy) {
        List<T> result = new ArrayList<T>();
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        Set<String> keys = columns.keySet();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String columnName = it.next();
                Object value = columns.get(columnName);
                if (!inited) {
                    where.eq(columnName, value);
                    inited = true;
                } else {
                    where.and().eq(columnName, value);
                }

            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            queryBuilder.offset(index);
            queryBuilder.limit(count);

            result = queryBuilder.query();
            return result;
        } catch (SQLException e) {
            LogUtil.e("多条件排序分页查询全部数据异常", e);
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public List<T> findAllByWhereAndIndex(Map<String, Object> eqcolumns,
                                          Map<String, Object> notEqColumns, Map<String, Object[]> inColumns,
                                          Map<String, Object[]> notInColumns, int index, int count,
                                          String orderBy) {
        List<T> result = new ArrayList<T>();
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;

            if (eqcolumns != null) {
                Set<String> eqKeys = eqcolumns.keySet();
                for (Iterator<String> it = eqKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object value = eqcolumns.get(columnName);
                    if (!inited) {
                        where.eq(columnName, value);
                        inited = true;
                    } else {
                        where.and().eq(columnName, value);
                    }

                }
            }

            if (notEqColumns != null) {
                Set<String> notEqKeys = notEqColumns.keySet();
                for (Iterator<String> it = notEqKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object value = notEqColumns.get(columnName);
                    if (!inited) {
                        where.ne(columnName, value);
                        inited = true;
                    } else {
                        where.and().ne(columnName, value);
                    }
                }
            }

            if (inColumns != null) {
                Set<String> inKeys = inColumns.keySet();
                for (Iterator<String> it = inKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object[] value = inColumns.get(columnName);
                    if (!inited) {
                        where.in(columnName, value);
                        inited = true;
                    } else {
                        where.and().in(columnName, value);
                    }
                }
            }

            if (notInColumns != null) {
                Set<String> notInKeys = notInColumns.keySet();
                for (Iterator<String> it = notInKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object[] value = notInColumns.get(columnName);
                    if (!inited) {
                        where.notIn(columnName, value);
                        inited = true;
                    } else {
                        where.and().notIn(columnName, value);
                    }
                }
            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            queryBuilder.offset(index);
            queryBuilder.limit(count);

            result = queryBuilder.query();
            return result;
        } catch (SQLException e) {
            LogUtil.e("多条件多组合排序分页查询全部数据异常", e);
        }

        return result;
    }

    /**
     * 按ID查询对象
     *
     * @param id
     * @return
     */
    public T findById(ID id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            LogUtil.e("按ID查询单条数据异常", e);
        }
        return null;
    }

    /**
     * 单条件查询对象
     *
     * @param columnName
     * @param value
     * @return
     */
    public T findByWhere(String columnName, String value) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
        try {
            queryBuilder.where().eq(columnName, value);

            return queryBuilder.queryForFirst();
        } catch (SQLException e) {
            LogUtil.e("单条件查询单条数据异常", e);
        }

        return null;

    }

    /**
     * 多条件查询对象
     *
     * @param columns
     * @return
     */
    public T findByWhere(Map<String, Object> columns) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        Set<String> keys = columns.keySet();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String columnName = it.next();
                Object value = columns.get(columnName);
                if (!inited) {
                    where.eq(columnName, value);
                    inited = true;
                } else {
                    where.and().eq(columnName, value);
                }

            }

            T queryForFirst = queryBuilder.queryForFirst();
            return queryForFirst;
        } catch (SQLException e) {
            LogUtil.e("多条件查询单条数据异常", e);
        }

        return null;
    }

    public int getCount(Map<String, Object> columns) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        Set<String> keys = columns.keySet();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String columnName = it.next();
                Object value = columns.get(columnName);
                if (!inited) {
                    where.eq(columnName, value);
                    inited = true;
                } else {
                    where.and().eq(columnName, value);
                }

            }
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            List<T> query = dao.query(preparedQuery);
            return query.size();
        } catch (SQLException e) {
            LogUtil.e("多条件查询条数异常", e);
        }

        return 0;
    }

    public int getCount(Map<String, Object> eqcolumns, Map<String, Object> notEqColumns,
                        Map<String, Object[]> inColumns, Map<String, Object[]> notInColumns) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;

            if (eqcolumns != null) {
                Set<String> eqKeys = eqcolumns.keySet();
                for (Iterator<String> it = eqKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object value = eqcolumns.get(columnName);
                    if (!inited) {
                        where.eq(columnName, value);
                        inited = true;
                    } else {
                        where.and().eq(columnName, value);
                    }

                }
            }

            if (notEqColumns != null) {
                Set<String> notEqKeys = notEqColumns.keySet();
                for (Iterator<String> it = notEqKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object value = notEqColumns.get(columnName);
                    if (!inited) {
                        where.ne(columnName, value);
                        inited = true;
                    } else {
                        where.and().ne(columnName, value);
                    }
                }
            }

            if (inColumns != null) {
                Set<String> inKeys = inColumns.keySet();
                for (Iterator<String> it = inKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object[] value = inColumns.get(columnName);
                    if (!inited) {
                        where.in(columnName, value);
                        inited = true;
                    } else {
                        where.and().in(columnName, value);
                    }
                }
            }

            if (notInColumns != null) {
                Set<String> notInKeys = notInColumns.keySet();
                for (Iterator<String> it = notInKeys.iterator(); it.hasNext(); ) {
                    String columnName = it.next();
                    Object[] value = notInColumns.get(columnName);
                    if (!inited) {
                        where.notIn(columnName, value);
                        inited = true;
                    } else {
                        where.and().notIn(columnName, value);
                    }
                }
            }
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            List<T> query = dao.query(preparedQuery);
            return query.size();
        } catch (SQLException e) {
            LogUtil.e("多条件查询条数异常", e);
        }

        return 0;
    }

    public T findByWhere(Map<String, Object> columns, String orderBy) {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder();

        Set<String> keys = columns.keySet();
        try {
            Where<T, ID> where = queryBuilder.where();
            boolean inited = false;
            for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
                String columnName = it.next();
                Object value = columns.get(columnName);
                if (!inited) {
                    where.eq(columnName, value);
                    inited = true;
                } else {
                    where.and().eq(columnName, value);
                }

            }

            if (!TextUtils.isEmpty(orderBy)) {
                queryBuilder.orderByRaw(orderBy);
            }

            T queryForFirst = queryBuilder.queryForFirst();
            return queryForFirst;
        } catch (SQLException e) {
            LogUtil.e("多条件排序查询单条数据异常", e);
        }

        return null;
    }

    /**
     * 通过id删除记录
     *
     * @param id
     */
    public void deteleById(ID id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            LogUtil.e("按ID删除单条数据异常", e);
        }
    }

    /**
     * 删除某一条记录
     */
    public void detele(T entity) {
        try {
            Log.e("SqliteDao","数据库删除操作");
            dao.delete(entity);
        } catch (SQLException e) {
            LogUtil.e("删除单条数据异常", e);
        }
    }

    /**
     * 删除某多条记录
     */
    public void detele(List<T> entitys) {
        try {
            dao.delete(entitys);
        } catch (SQLException e) {
            LogUtil.e("删除多条数据异常", e);
        }
    }

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    public T insert(T entity) {
        try {
            int i = dao.create(entity);
            // 插入成功
            if (i != -1) {
                return entity;
            }
        } catch (SQLException e) {
            LogUtil.e("插入单条数据异常", e);
        }
        return null;
    }

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    public T update(T entity) {
        try {
            int i = dao.update(entity);
            if (i != -1) {
                return entity;
            }
        } catch (SQLException e) {
            LogUtil.e("更新单条数据异常", e);
        }
        return null;
    }

    /**
     *
     */
    public void executeSql(String sql, String... s) {
        try {
            dao.executeRaw(sql, s);
        } catch (SQLException e) {
            LogUtil.e("执行原生SQL语句异常", e);
        }
    }

    /**
     * 事务调用模板方法
     *
     * @param transactionCallback
     */
    public void executeTransaction(TransactionCallback transactionCallback) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();

        writableDatabase.beginTransaction();
        try {

            transactionCallback.execute();
            writableDatabase.setTransactionSuccessful();
        } catch (Exception exception) {
            LogUtil.e("事务出错", exception);
            LogUtil.e(LogUtil.getStackMsg(exception));
        }

        writableDatabase.endTransaction();
    }

    /**
     * 事务回调类
     *
     * @author lizhuo_a
     */
    public interface TransactionCallback {

        void execute() throws SQLException;
    }

}
