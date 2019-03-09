package com.snh.snhseller.bean.supplierbean;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class ProductBean {
    public int ShopGoodsId;
    public String [] CarouselImgUrl;
    public String ShopIconUrl;
    public int SupplierId;
    public String SupplierName;
    public String SupplierIconUrl;
    public double Price;
    public double RetailPrice;
    public String ShopGoodsName;
    public int SumInventory;
    public List<SkuBean> ShopNormsList;
}
