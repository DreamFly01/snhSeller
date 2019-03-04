package com.snh.snhseller.bean;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */
public class UserBean {
    public Supp supp;
    public NimResult nimResult;
    public String suppFxUrl;
    public String suppType;
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
        public String Address;
    }
    public class NimResult{
        public String Token;
        public String Accid;
    }
}
