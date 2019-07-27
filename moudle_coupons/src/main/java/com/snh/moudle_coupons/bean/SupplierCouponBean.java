package com.snh.moudle_coupons.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/5<p>
 * <p>changeTime：2019/6/5<p>
 * <p>version：1<p>
 */
public class SupplierCouponBean implements Parcelable {
    public int CouponId;
    public String CouponName;
    public double TotalAmount;
    public int SupplierId;
    public int MerchantsId;
    public String GoodsIds;
    public String CouponCodeIds;

    protected SupplierCouponBean(Parcel in) {
        CouponId = in.readInt();
        CouponName = in.readString();
        TotalAmount = in.readDouble();
        SupplierId = in.readInt();
        MerchantsId = in.readInt();
        GoodsIds = in.readString();
        CouponCodeIds = in.readString();
    }

    public static final Creator<SupplierCouponBean> CREATOR = new Creator<SupplierCouponBean>() {
        @Override
        public SupplierCouponBean createFromParcel(Parcel in) {
            return new SupplierCouponBean(in);
        }

        @Override
        public SupplierCouponBean[] newArray(int size) {
            return new SupplierCouponBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CouponId);
        dest.writeString(CouponName);
        dest.writeDouble(TotalAmount);
        dest.writeInt(SupplierId);
        dest.writeInt(MerchantsId);
        dest.writeString(GoodsIds);
        dest.writeString(CouponCodeIds);
    }
}
