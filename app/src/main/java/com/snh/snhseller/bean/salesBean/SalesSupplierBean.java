package com.snh.snhseller.bean.salesBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SalesSupplierBean implements Parcelable {
    public int SupplierId;
    public String ShopName;
    public String SupplierIconUrl;
    public String Describe;

    protected SalesSupplierBean(Parcel in) {
        SupplierId = in.readInt();
        ShopName = in.readString();
        SupplierIconUrl = in.readString();
        Describe = in.readString();
    }

    public static final Creator<SalesSupplierBean> CREATOR = new Creator<SalesSupplierBean>() {
        @Override
        public SalesSupplierBean createFromParcel(Parcel in) {
            return new SalesSupplierBean(in);
        }

        @Override
        public SalesSupplierBean[] newArray(int size) {
            return new SalesSupplierBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SupplierId);
        dest.writeString(ShopName);
        dest.writeString(SupplierIconUrl);
        dest.writeString(Describe);
    }
}
