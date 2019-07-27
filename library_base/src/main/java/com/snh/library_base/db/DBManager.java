package com.snh.library_base.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.snh.library_base.utils.Contans;
import com.snh.library_base.utils.SPUtils;
import com.snh.snhseller.base.greendao.DaoMaster;
import com.snh.snhseller.base.greendao.DaoSession;
import com.snh.snhseller.base.greendao.UserEntityDao;

import java.util.List;


/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class DBManager {
    private final static String dbName = "shn_seller_base_db";
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


    public void logingSuccess(AllUserBean userBean, final Activity activity, String psw, String phone) {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        UserEntity userEntity = new UserEntity();
        userEntity.Id = userBean.supp.Id;
        userEntity.BusinessActivities = userBean.supp.BusinessActivities;
        userEntity.Contacts = userBean.supp.Contacts ;
        userEntity.ContactsTel = phone;
        userEntity.Introduction = userBean.supp.Introduction;
        userEntity.Logo = userBean.supp.Logo;
        userEntity.ShopName = userBean.supp.ShopName;
        userEntity.suppFxUrl = userBean.suppFxUrl;
        userEntity.Username = userBean.supp.Username;
        userEntity.suppType = userBean.suppType;
        userEntity.Province = userBean.supp.Province;
        userEntity.City = userBean.supp.City;
        userEntity.Area = userBean.supp.Area;
        userEntity.Address = userBean.supp.Address;
        userEntity.ContactsQQ = userBean.supp.ContactsQQ;
        userEntity.shopTypeName = userBean.shopTypeName;

        if (null != userBean.nimResult) {
            userEntity.Accid = userBean.nimResult.Accid;
            userEntity.Token = userBean.nimResult.Token;
        }
        userEntityDao.insert(userEntity);
        SPUtils.getInstance(activity).savaBoolean(Contans.IS_HDFK, userBean.IsHdfk).commit();

        SPUtils.getInstance(context).saveData(Contans.PSW, psw);
        SPUtils.getInstance(context).saveData(Contans.PHONE,phone);
        SPUtils.getInstance(context).savaBoolean(Contans.IS_REGIST, true).commit();

    }



    public int getUseId() {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        List<UserEntity> list = userEntityDao.queryBuilder().list();
        return list.size() > 0 ? list.get(0).Id : 0;
    }

    public UserEntity getUserInfo() {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        List<UserEntity> list = userEntityDao.queryBuilder().list();
        return list.size() > 0 ? list.get(0) : null;
    }



    public void cleanUser() {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        userEntityDao.deleteAll();
    }

}
