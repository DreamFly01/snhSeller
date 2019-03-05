package com.snh.snhseller.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.snh.snhseller.MainActivity;
import com.snh.snhseller.bean.UserBean;
import com.snh.snhseller.bean.beanDao.UserEntity;
import com.snh.snhseller.bean.salebean.SaleUserBean;
import com.snh.snhseller.greendao.DaoMaster;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.greendao.SaleUserBeanDao;
import com.snh.snhseller.greendao.UserEntityDao;

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
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private String tabName;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public DBManager(Context context, String tabName) {
        this.context = context;
        this.tabName = tabName;
        openHelper = new DaoMaster.DevOpenHelper(context, tabName, null);

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
    public SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, tabName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    public SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, tabName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public DaoSession getDaoSession() {
        daoMaster = new DaoMaster(getWritableDatabase());
        daoSession = daoMaster.newSession();
        return daoSession;
    }

    public void logingSuccess(UserBean userBean, final Activity activity) {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        UserEntity userEntity = new UserEntity();
        userEntity.Id = userBean.supp.Id;
        userEntity.BusinessActivities = userBean.supp.BusinessActivities;
        userEntity.Contacts = userBean.supp.Contacts;
        userEntity.ContactsTel = userBean.supp.ContactsTel;
        userEntity.Introduction = userBean.supp.Introduction;
        userEntity.Logo = userBean.supp.Logo;
        userEntity.ShopName = userBean.supp.ShopName;
        userEntity.suppFxUrl = userBean.suppFxUrl;
        userEntity.Username = userBean.supp.Username;
        userEntity.Accid = userBean.nimResult.Accid;
        userEntity.Token = userBean.nimResult.Token;
        userEntity.suppType = userBean.suppType;
        userEntity.Address = userBean.supp.Address;
        userEntity.ContactsQQ = userBean.supp.ContactsQQ;
        userEntityDao.insert(userEntity);
        JumpUtils.simpJump(activity,MainActivity.class,true);
    }

    public void saveSaleUser(SaleUserBean bean){
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        userBeanDao.insert(bean);
    }
    public int getUseId() {
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        List<UserEntity> list = userEntityDao.queryBuilder().list();
        return list.size()>0? list.get(0).Id:0;
    }
    public UserEntity getUserInfo(){
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        List<UserEntity> list = userEntityDao.queryBuilder().list();
        return list.size()>0?list.get(0):null;
    }
    public SaleUserBean getSaleInfo(){
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        List<SaleUserBean> list = userBeanDao.queryBuilder().list();
        return list.size()>0?list.get(0):null;
    }
    public void cleanUser(){
        UserEntityDao userEntityDao = getDaoSession().getUserEntityDao();
        userEntityDao.deleteAll();
    }
    public void cleanSale(){
        SaleUserBeanDao userBeanDao = getDaoSession().getSaleUserBeanDao();
        userBeanDao.deleteAll();
    }

}
