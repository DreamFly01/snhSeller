package com.snh.snhseller.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/17<p>
 * <p>changeTime：2019/4/17<p>
 * <p>version：1<p>
 */
public class AllUserBean {
    public int type;
    public String shopTypeName;
    public Supp supp;
    public NimResult nimResult;
    public String suppFxUrl;
    public String suppType;
    public boolean IsHdfk;
    public int SalesmanId;
    public String PhoneNumber;
    public String NickName;
    public String RealName;
    public String SalesmanLogo;
    public int isFull;//资料是否完整 1完整 0不完整
    public class Supp {
        public int Id;
        public String Username;
        public String ShopName;
        public String BusinessActivities;
        public String Logo;
        public String Introduction;
        public String Contacts;
        public String ContactsTel;
        public String ContactsQQ;
        public String Province;
        public String City;
        public String Area;
        public String Address;
    }
    public class NimResult{
        public String Token;
        public String Accid;
    }
}
