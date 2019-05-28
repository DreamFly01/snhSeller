package com.snh.snhseller.bean.supplierbean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/7<p>
 * <p>changeTime：2019/3/7<p>
 * <p>version：1<p>
 */
public class SkuBean implements Parcelable {
    public int NormId;
    public String NormName;
    public String NormValue;
    public double Price;
    public double MarketPrice;
    public int Inventory;
    public String Unit;
    public int total;

    public double NormPrice;
    public double NormSumPrice;
    public int NormNumber;

    protected SkuBean(Parcel in) {
        NormId = in.readInt();
        NormName = in.readString();
        NormValue = in.readString();
        Price = in.readDouble();
        MarketPrice = in.readDouble();
        Inventory = in.readInt();
        Unit = in.readString();
        total = in.readInt();
        NormPrice = in.readDouble();
        NormSumPrice = in.readDouble();
        NormNumber = in.readInt();
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
        dest.writeString(NormName);
        dest.writeString(NormValue);
        dest.writeDouble(Price);
        dest.writeDouble(MarketPrice);
        dest.writeInt(Inventory);
        dest.writeString(Unit);
        dest.writeInt(total);
        dest.writeDouble(NormPrice);
        dest.writeDouble(NormSumPrice);
        dest.writeInt(NormNumber);
    }
}
