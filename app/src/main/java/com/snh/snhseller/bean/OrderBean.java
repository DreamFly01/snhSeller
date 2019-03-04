package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderBean implements Parcelable {
    public int OrderId;
    public int SupplierId;
    public int UserId;
    public String UserName;
    public String UserIcon;
    public double OrderPrice;
    public int Freight;
    public int OrderStates;
    public int PayStates;
    public long OrderCreateTime;
    public String ReceivingName;
    public String ReceivingPhone;
    public String ReceivingAddress;
    public List<OrderGoodsBean> OrderGoodsList;

    protected OrderBean(Parcel in) {
        OrderId = in.readInt();
        SupplierId = in.readInt();
        UserId = in.readInt();
        UserName = in.readString();
        UserIcon = in.readString();
        OrderPrice = in.readDouble();
        Freight = in.readInt();
        OrderStates = in.readInt();
        PayStates = in.readInt();
        OrderCreateTime = in.readLong();
        ReceivingName = in.readString();
        ReceivingPhone = in.readString();
        ReceivingAddress = in.readString();
        OrderGoodsList = in.createTypedArrayList(OrderGoodsBean.CREATOR);
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel in) {
            return new OrderBean(in);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OrderId);
        dest.writeInt(SupplierId);
        dest.writeInt(UserId);
        dest.writeString(UserName);
        dest.writeString(UserIcon);
        dest.writeDouble(OrderPrice);
        dest.writeInt(Freight);
        dest.writeInt(OrderStates);
        dest.writeInt(PayStates);
        dest.writeLong(OrderCreateTime);
        dest.writeString(ReceivingName);
        dest.writeString(ReceivingPhone);
        dest.writeString(ReceivingAddress);
        dest.writeTypedList(OrderGoodsList);
    }
}
