package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderGoodsBean implements Parcelable {
    public int OrderGoodsId;
    public String OrderGoodsName;
    public String OrderGoodsIcon;
    public String NormName;
    public int Number;
    public double Price;
    public String shopLogo;
    public String shopName;
    public String state;
    public double Freight;
    public int OrderId;


    protected OrderGoodsBean(Parcel in) {
        OrderGoodsId = in.readInt();
        OrderGoodsName = in.readString();
        OrderGoodsIcon = in.readString();
        NormName = in.readString();
        Number = in.readInt();
        Price = in.readDouble();
        shopLogo = in.readString();
        shopName = in.readString();
        state = in.readString();
        Freight = in.readDouble();
        OrderId = in.readInt();
    }

    public static final Creator<OrderGoodsBean> CREATOR = new Creator<OrderGoodsBean>() {
        @Override
        public OrderGoodsBean createFromParcel(Parcel in) {
            return new OrderGoodsBean(in);
        }

        @Override
        public OrderGoodsBean[] newArray(int size) {
            return new OrderGoodsBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OrderGoodsId);
        dest.writeString(OrderGoodsName);
        dest.writeString(OrderGoodsIcon);
        dest.writeString(NormName);
        dest.writeInt(Number);
        dest.writeDouble(Price);
        dest.writeString(shopLogo);
        dest.writeString(shopName);
        dest.writeString(state);
        dest.writeDouble(Freight);
        dest.writeInt(OrderId);
    }
}
