package com.snh.snhseller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.snh.snhseller.bean.salebean.SaleUserBean;
import com.snh.snhseller.greendao.DaoMaster;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.greendao.SaleUserBeanDao;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class DBManager {
    private final static String dbName = "shn_seller_db";
    private static DBManager mInstance;
    private static DaoMaster.DevOpenHelper openHelper;
    private static Context context;
    private String tabName;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public DBManager(Context context, String tabName) {
        this.context = context;
        this.tabName = tabName;
        openHelper = new DaoMaster.DevOpenHelper(context, tabName, null);
        getDaoMaster();
        getDaoMaster();
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context, dbName);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    public static SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            getInstance(context);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    public static SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 获取DaoMaster
     * <p>
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @return
     */
    public static DaoMaster getDaoMaster() {
        if (null == daoMaster) {
            synchronized (DBManager.class) {
                if (null == daoMaster) {
                    MyOpenHelper helper = new MyOpenHelper(context, dbName, null);
                    daoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return daoMaster;
    }

    /**
     * 获取DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession() {
        if (null == daoSession) {
            synchronized (DBManager.class) {
                daoSession = getDaoMaster().newSession();
            }
        }

        return daoSession;
    }

    public void saveSaleUser(SaleUserBean bean) {
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        userBeanDao.insert(bean);
    }

    public SaleUserBean getSaleInfo() {
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        List<SaleUserBean> list = userBeanDao.queryBuilder().list();
        return list.size() > 0 ? list.get(0) : null;
    }


    public void cleanSale() {
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        userBeanDao.deleteAll();
    }
}
