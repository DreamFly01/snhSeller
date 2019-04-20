package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SkuBean implements Parcelable {
    public int NormId;
    public int ShopGoodsId;
    public String CommodityName;
    public String CommodityIconUrl;
    public String NormName;
    public String NormValue;
    public double Price;
    public double RetailPrice;
    public int Inventory;
    public int Weight;
    public String MeasureUnit;
    public String CreateTime;
    public String UpdateTime;

    protected SkuBean(Parcel in) {
        NormId = in.readInt();
        ShopGoodsId = in.readInt();
        CommodityName = in.readString();
        CommodityIconUrl = in.readString();
        NormName = in.readString();
        NormValue = in.readString();
        Price = in.readDouble();
        RetailPrice = in.readDouble();
        Inventory = in.readInt();
        Weight = in.readInt();
        MeasureUnit = in.readString();
        CreateTime = in.readString();
        UpdateTime = in.readString();
    }

    public static final Creator<SkuBean> CREATOR = new Creator<SkuBean>() {
        @Override
        public SkuBean createFromParcel(Parcel in) {
            return new SkuBean(in);
        }

        @Override
        public SkuBean[] newArray(int size) {
            return new SkuBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(NormId);
        dest.writeInt(ShopGoodsId);
        dest.writeString(CommodityName);
        dest.writeString(CommodityIconUrl);
        dest.writeString(NormName);
        dest.writeString(NormValue);
        dest.writeDouble(Price);
        dest.writeDouble(RetailPrice);
        dest.writeInt(Inventory);
        dest.writeInt(Weight);
        dest.writeString(MeasureUnit);
        dest.writeString(CreateTime);
        dest.writeString(UpdateTime);
    }
}
