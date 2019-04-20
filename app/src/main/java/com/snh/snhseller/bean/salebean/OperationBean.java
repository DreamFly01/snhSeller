package com.snh.snhseller.bean.salebean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class OperationBean {
    public int SupplierId;
    public String SupplierName;
    public String SupplierIconUrl;
    public String SupplierBuinourName;
    public String SupplierBuinourPhoneNumber;
    public String SupplierAddress;

    public List<CommTenantBean> CommTenantList;
    public List<CommTenantBean> MyCommTenantList;
}
