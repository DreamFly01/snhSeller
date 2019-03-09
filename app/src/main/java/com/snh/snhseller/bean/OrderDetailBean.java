package com.snh.snhseller.bean;

import com.snh.snhseller.bean.supplierbean.SkuBean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/9<p>
 * <p>changeTime：2019/3/9<p>
 * <p>version：1<p>
 */
public class OrderDetailBean {
    public String OrderNo;
    public String CommTenantAddress;
    public String CommTenantArea;
    public String CommTenantCity;
    public String CommTenantProvince;
    public String CommTenantLeave;
    public String CommTenantName;
    public String CommTenantPhone;
    public String SupplierName;
    public String SupplierIconUrl;
    public String CommodityName;
    public String CommodityIconUrl;
    public int PayMethod;
    public int Freight;
    public double OrderSumPrice;
    public String SuppLeave;
    public String CreateTime;
    public String PayTime;
    public String DeliverGoodsTime;
    public String MakeTime;
    public String CancelTime;
    public int DPayPastDue;

    public List<SkuBean> NormsList;
}
