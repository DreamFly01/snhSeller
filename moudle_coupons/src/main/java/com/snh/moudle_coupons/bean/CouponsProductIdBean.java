package com.snh.moudle_coupons.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/4<p>
 * <p>changeTime：2019/6/4<p>
 * <p>version：1<p>
 */
public class CouponsProductIdBean implements Parcelable {
    public int GoodsId;
    public String GoodsImg;
    public String GoodsName;

    public CouponsProductIdBean() {
    }


    protected CouponsProductIdBean(Parcel in) {
        GoodsId = in.readInt();
        GoodsImg = in.readString();
        GoodsName = in.readString();
    }

    public static final Creator<CouponsProductIdBean> CREATOR = new Creator<CouponsProductIdBean>() {
        @Override
        public CouponsProductIdBean createFromParcel(Parcel in) {
            return new CouponsProductIdBean(in);
        }

        @Override
        public CouponsProductIdBean[] newArray(int size) {
            return new CouponsProductIdBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(GoodsId);
        dest.writeString(GoodsImg);
        dest.writeString(GoodsName);
    }
}
