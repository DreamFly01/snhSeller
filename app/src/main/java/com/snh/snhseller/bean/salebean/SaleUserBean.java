package com.snh.snhseller.bean.salebean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
@Entity
public class SaleUserBean {
    public int SalesmanId;
    public String PhoneNumber;
    public String NickName;
    @Generated(hash = 997801547)
    public SaleUserBean(int SalesmanId, String PhoneNumber, String NickName) {
        this.SalesmanId = SalesmanId;
        this.PhoneNumber = PhoneNumber;
        this.NickName = NickName;
    }
    @Generated(hash = 1682475375)
    public SaleUserBean() {
    }
    public int getSalesmanId() {
        return this.SalesmanId;
    }
    public void setSalesmanId(int SalesmanId) {
        this.SalesmanId = SalesmanId;
    }
    public String getPhoneNumber() {
        return this.PhoneNumber;
    }
    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
    public String getNickName() {
        return this.NickName;
    }
    public void setNickName(String NickName) {
        this.NickName = NickName;
    }
}
