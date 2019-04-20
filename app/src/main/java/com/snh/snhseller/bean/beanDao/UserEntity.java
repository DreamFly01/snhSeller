package com.snh.snhseller.bean.beanDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/20<p>
 * <p>changeTime：2019/2/20<p>
 * <p>version：1<p>
 */

@Entity()
public class UserEntity {

    public int Id;
    public String Username;
    public String ShopName;
    public String BusinessActivities;
    public String Logo;
    public String Introduction;
    public String Contacts;
    public String ContactsTel;
    public String shopTypeName;

    public String Token;
    public String Accid;
    public String suppFxUrl;
    public String suppType;
    public String ContactsQQ;
    public String Address;
    @Generated(hash = 1311986357)
    public UserEntity(int Id, String Username, String ShopName,
            String BusinessActivities, String Logo, String Introduction,
            String Contacts, String ContactsTel, String shopTypeName, String Token,
            String Accid, String suppFxUrl, String suppType, String ContactsQQ,
            String Address) {
        this.Id = Id;
        this.Username = Username;
        this.ShopName = ShopName;
        this.BusinessActivities = BusinessActivities;
        this.Logo = Logo;
        this.Introduction = Introduction;
        this.Contacts = Contacts;
        this.ContactsTel = ContactsTel;
        this.shopTypeName = shopTypeName;
        this.Token = Token;
        this.Accid = Accid;
        this.suppFxUrl = suppFxUrl;
        this.suppType = suppType;
        this.ContactsQQ = ContactsQQ;
        this.Address = Address;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public int getId() {
        return this.Id;
    }
    public void setId(int Id) {
        this.Id = Id;
    }
    public String getUsername() {
        return this.Username;
    }
    public void setUsername(String Username) {
        this.Username = Username;
    }
    public String getShopName() {
        return this.ShopName;
    }
    public void setShopName(String ShopName) {
        this.ShopName = ShopName;
    }
    public String getBusinessActivities() {
        return this.BusinessActivities;
    }
    public void setBusinessActivities(String BusinessActivities) {
        this.BusinessActivities = BusinessActivities;
    }
    public String getLogo() {
        return this.Logo;
    }
    public void setLogo(String Logo) {
        this.Logo = Logo;
    }
    public String getIntroduction() {
        return this.Introduction;
    }
    public void setIntroduction(String Introduction) {
        this.Introduction = Introduction;
    }
    public String getContacts() {
        return this.Contacts;
    }
    public void setContacts(String Contacts) {
        this.Contacts = Contacts;
    }
    public String getContactsTel() {
        return this.ContactsTel;
    }
    public void setContactsTel(String ContactsTel) {
        this.ContactsTel = ContactsTel;
    }
    public String getShopTypeName() {
        return this.shopTypeName;
    }
    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }
    public String getToken() {
        return this.Token;
    }
    public void setToken(String Token) {
        this.Token = Token;
    }
    public String getAccid() {
        return this.Accid;
    }
    public void setAccid(String Accid) {
        this.Accid = Accid;
    }
    public String getSuppFxUrl() {
        return this.suppFxUrl;
    }
    public void setSuppFxUrl(String suppFxUrl) {
        this.suppFxUrl = suppFxUrl;
    }
    public String getSuppType() {
        return this.suppType;
    }
    public void setSuppType(String suppType) {
        this.suppType = suppType;
    }
    public String getContactsQQ() {
        return this.ContactsQQ;
    }
    public void setContactsQQ(String ContactsQQ) {
        this.ContactsQQ = ContactsQQ;
    }
    public String getAddress() {
        return this.Address;
    }
    public void setAddress(String Address) {
        this.Address = Address;
    }
}
