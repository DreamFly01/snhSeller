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
    public String RealName;
    public String SalesmanLogo;
    @Generated(hash = 1696948586)
    public SaleUserBean(int SalesmanId, String PhoneNumber, String NickName,
            String RealName, String SalesmanLogo) {
        this.SalesmanId = SalesmanId;
        this.PhoneNumber = PhoneNumber;
        this.NickName = NickName;
        this.RealName = RealName;
        this.SalesmanLogo = SalesmanLogo;
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
    public String getRealName() {
        return this.RealName;
    }
    public void setRealName(String RealName) {
        this.RealName = RealName;
    }
    public String getSalesmanLogo() {
        return this.SalesmanLogo;
    }
    public void setSalesmanLogo(String SalesmanLogo) {
        this.SalesmanLogo = SalesmanLogo;
    }
}
