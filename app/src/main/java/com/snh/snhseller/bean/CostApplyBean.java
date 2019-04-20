package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/14<p>
 * <p>changeTime：2019/3/14<p>
 * <p>version：1<p>
 */
public class CostApplyBean implements Parcelable {
    public int SalesmanId;
    public String SalesmanName;
    public int CostId;
    public String CostName;
    public int CostType;
    public String CostTypeName;
    public int CostStates;
    public double Budget;
    public String CostCreateTime;
    public String ApprovalNo;
    public String OccurDate;
    public String [] ReMark;
    public String [] SuppRemark;
    public String ExpenseVoucher;
    public int SupplierId;
    public String SupplierName;
    public String SupplierIconUrl;
    public String SalesmanLogo;

    protected CostApplyBean(Parcel in) {
        SalesmanId = in.readInt();
        SalesmanName = in.readString();
        CostId = in.readInt();
        CostName = in.readString();
        CostType = in.readInt();
        CostTypeName = in.readString();
        CostStates = in.readInt();
        Budget = in.readDouble();
        CostCreateTime = in.readString();
        ApprovalNo = in.readString();
        OccurDate = in.readString();
        ReMark = in.createStringArray();
        SuppRemark = in.createStringArray();
        ExpenseVoucher = in.readString();
        SupplierId = in.readInt();
        SupplierName = in.readString();
        SupplierIconUrl = in.readString();
        SalesmanLogo = in.readString();
    }

    public static final Creator<CostApplyBean> CREATOR = new Creator<CostApplyBean>() {
        @Override
        public CostApplyBean createFromParcel(Parcel in) {
            return new CostApplyBean(in);
        }

        @Override
        public CostApplyBean[] newArray(int size) {
            return new CostApplyBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(SalesmanId);
        dest.writeString(SalesmanName);
        dest.writeInt(CostId);
        dest.writeString(CostName);
        dest.writeInt(CostType);
        dest.writeString(CostTypeName);
        dest.writeInt(CostStates);
        dest.writeDouble(Budget);
        dest.writeString(CostCreateTime);
        dest.writeString(ApprovalNo);
        dest.writeString(OccurDate);
        dest.writeStringArray(ReMark);
        dest.writeStringArray(SuppRemark);
        dest.writeString(ExpenseVoucher);
        dest.writeInt(SupplierId);
        dest.writeString(SupplierName);
        dest.writeString(SupplierIconUrl);
        dest.writeString(SalesmanLogo);
    }
}
