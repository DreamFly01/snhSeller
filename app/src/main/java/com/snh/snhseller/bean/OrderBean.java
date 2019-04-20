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
//    public int OrderStates;
    public int PayStates;
    public String OrderCreateTime;
    public String ReceivingName;
    public String ReceivingPhone;
    public String ReceivingAddress;

//    public int OrderId;
    public String OrderNo;
    public String CommTenantName;
    public String CommTenantIconUrl;
    public int SumCommodity;
    public String CommodityName;
    public String CommodityIconUrl;
//    public double OrderPrice;
    public int OrderStates;//0.全部 1.待付款 2.待发货 3.已发货 4.已完成 5.退款中 6.退款成功 7.退款失败
    public String Delivery;
    public String PayMethod;//1.微信支付 2.支付宝支付 3.银联支付 4.货到付款  5.其他
    public String LeaveWord;
//    public String OrderCreateTime;
    public List<OrderGoodsBean> OrderGoodsList;
    public String ShopType;
    public boolean IsSupportCode;
    public int IsChangePrice;

    protected OrderBean(Parcel in) {
        OrderId = in.readInt();
        SupplierId = in.readInt();
        UserId = in.readInt();
        UserName = in.readString();
        UserIcon = in.readString();
        OrderPrice = in.readDouble();
        Freight = in.readInt();
        PayStates = in.readInt();
        OrderCreateTime = in.readString();
        ReceivingName = in.readString();
        ReceivingPhone = in.readString();
        ReceivingAddress = in.readString();
        OrderNo = in.readString();
        CommTenantName = in.readString();
        CommTenantIconUrl = in.readString();
        SumCommodity = in.readInt();
        CommodityName = in.readString();
        CommodityIconUrl = in.readString();
        OrderStates = in.readInt();
        Delivery = in.readString();
        PayMethod = in.readString();
        LeaveWord = in.readString();
        OrderGoodsList = in.createTypedArrayList(OrderGoodsBean.CREATOR);
        ShopType = in.readString();
        IsSupportCode = in.readByte() != 0;
        IsChangePrice = in.readInt();
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
        dest.writeInt(PayStates);
        dest.writeString(OrderCreateTime);
        dest.writeString(ReceivingName);
        dest.writeString(ReceivingPhone);
        dest.writeString(ReceivingAddress);
        dest.writeString(OrderNo);
        dest.writeString(CommTenantName);
        dest.writeString(CommTenantIconUrl);
        dest.writeInt(SumCommodity);
        dest.writeString(CommodityName);
        dest.writeString(CommodityIconUrl);
        dest.writeInt(OrderStates);
        dest.writeString(Delivery);
        dest.writeString(PayMethod);
        dest.writeString(LeaveWord);
        dest.writeTypedList(OrderGoodsList);
        dest.writeString(ShopType);
        dest.writeByte((byte) (IsSupportCode ? 1 : 0));
        dest.writeInt(IsChangePrice);
    }
}
