package com.snh.moudle_coupons.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/30<p>
 * <p>changeTime：2019/5/30<p>
 * <p>version：1<p>
 */
public class CouponsBean implements Parcelable {
    public int CouponId;
    public String CouponName;
    public  double ConditionValue;
    public double CouponValue;
    public int CouponWay;
    public String BeginDate;
    public String EndDate;
    public int TotalNum;
    public int StockNum;
    public int ReceiveNum;
    public int UsedNum;
    public int MaxReceiveNum;
    public int IsDisplay;
    public int IsPutaway;
    public String SupplierName;
    public String SupplierLogo;
    public int IsAdd;

    protected CouponsBean(Parcel in) {
        CouponId = in.readInt();
        CouponName = in.readString();
        ConditionValue = in.readDouble();
        CouponValue = in.readDouble();
        CouponWay = in.readInt();
        BeginDate = in.readString();
        EndDate = in.readString();
        TotalNum = in.readInt();
        StockNum = in.readInt();
        ReceiveNum = in.readInt();
        UsedNum = in.readInt();
        MaxReceiveNum = in.readInt();
        IsDisplay = in.readInt();
        IsPutaway = in.readInt();
        SupplierName = in.readString();
        SupplierLogo = in.readString();
    }

    public static final Creator<CouponsBean> CREATOR = new Creator<CouponsBean>() {
        @Override
        public CouponsBean createFromParcel(Parcel in) {
            return new CouponsBean(in);
        }

        @Override
        public CouponsBean[] newArray(int size) {
            return new CouponsBean[size];
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
        dest.writeDouble(ConditionValue);
        dest.writeDouble(CouponValue);
        dest.writeInt(CouponWay);
        dest.writeString(BeginDate);
        dest.writeString(EndDate);
        dest.writeInt(TotalNum);
        dest.writeInt(StockNum);
        dest.writeInt(ReceiveNum);
        dest.writeInt(UsedNum);
        dest.writeInt(MaxReceiveNum);
        dest.writeInt(IsDisplay);
        dest.writeInt(IsPutaway);
        dest.writeString(SupplierName);
        dest.writeString(SupplierLogo);
    }
}
