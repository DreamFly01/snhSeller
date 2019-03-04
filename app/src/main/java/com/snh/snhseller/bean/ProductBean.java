package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class ProductBean implements Parcelable {
    public int CommTenantId;
    public String CommTenantName;
    public String CommTenantIcon;
    public int CategoryId;
    public String CategoryName;
    public int IsAuditing;
    public String Reason;
    public double Price;
    public long Inventory;
    public long SalesVolume;
    public int state;
    public String UnitsTitle;

    protected ProductBean(Parcel in) {
        CommTenantId = in.readInt();
        CommTenantName = in.readString();
        CommTenantIcon = in.readString();
        CategoryId = in.readInt();
        CategoryName = in.readString();
        IsAuditing = in.readInt();
        Reason = in.readString();
        Price = in.readDouble();
        Inventory = in.readLong();
        SalesVolume = in.readLong();
        state = in.readInt();
        UnitsTitle = in.readString();
    }

    public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
        @Override
        public ProductBean createFromParcel(Parcel in) {
            return new ProductBean(in);
        }

        @Override
        public ProductBean[] newArray(int size) {
            return new ProductBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CommTenantId);
        dest.writeString(CommTenantName);
        dest.writeString(CommTenantIcon);
        dest.writeInt(CategoryId);
        dest.writeString(CategoryName);
        dest.writeInt(IsAuditing);
        dest.writeString(Reason);
        dest.writeDouble(Price);
        dest.writeLong(Inventory);
        dest.writeLong(SalesVolume);
        dest.writeInt(state);
        dest.writeString(UnitsTitle);
    }
}
