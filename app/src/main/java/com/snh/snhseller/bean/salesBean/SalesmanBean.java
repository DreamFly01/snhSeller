package com.snh.snhseller.bean.salesBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class SalesmanBean implements Parcelable {
    public int SalesmanId;
    public String SalesmanName;
    public String SalesmanRealName;
    public String Sex;
    public int Age;
    public String PhoneNumber;
    public String Address;
    public String CreateTime;
    public String UpdateTime;
    public boolean States;
    public String SalesmanLogo;
    public List<SalesSupplierBean> ManagerSuppList;

    protected SalesmanBean(Parcel in) {
        SalesmanId = in.readInt();
        SalesmanName = in.readString();
        SalesmanRealName = in.readString();
        Sex = in.readString();
        Age = in.readInt();
        PhoneNumber = in.readString();
        Address = in.readString();
        CreateTime = in.readString();
        UpdateTime = in.readString();
        States = in.readByte() != 0;
        SalesmanLogo = in.readString();
        ManagerSuppList = in.createTypedArrayList(SalesSupplierBean.CREATOR);
    }

    public static final Creator<SalesmanBean> CREATOR = new Creator<SalesmanBean>() {
        @Override
        public SalesmanBean createFromParcel(Parcel in) {
            return new SalesmanBean(in);
        }

        @Override
        public SalesmanBean[] newArray(int size) {
            return new SalesmanBean[size];
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
        dest.writeString(SalesmanRealName);
        dest.writeString(Sex);
        dest.writeInt(Age);
        dest.writeString(PhoneNumber);
        dest.writeString(Address);
        dest.writeString(CreateTime);
        dest.writeString(UpdateTime);
        dest.writeByte((byte) (States ? 1 : 0));
        dest.writeString(SalesmanLogo);
        dest.writeTypedList(ManagerSuppList);
    }
}
