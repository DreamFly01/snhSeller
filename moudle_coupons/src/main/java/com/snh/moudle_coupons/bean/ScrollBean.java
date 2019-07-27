package com.snh.moudle_coupons.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/25<p>
 * <p>changeTime：2019/1/25<p>
 * <p>version：1<p>
 */

public class ScrollBean extends SectionEntity<ScrollBean.ScrollItemBean> {
    public  long SupplierId;
    public ScrollBean(boolean isHeader, String header) {
        super(isHeader, header);
    }
    public ScrollItemBean itemBean;

    public ScrollBean(ScrollBean.ScrollItemBean bean) {
        super(bean);
    }

    public static class ScrollItemBean implements Parcelable {
        public int ShopgoodsId;
        public String CommodityName;
        public String CommodityIconUrl;
        public long Inventory;
        public String type;
        public int Status;
        public boolean isSelect;

        public ScrollItemBean() {
        }

        protected ScrollItemBean(Parcel in) {
            ShopgoodsId = in.readInt();
            CommodityName = in.readString();
            CommodityIconUrl = in.readString();
            Inventory = in.readLong();
            type = in.readString();
            isSelect = in.readByte() != 0;
        }

        public static final Creator<ScrollItemBean> CREATOR = new Creator<ScrollItemBean>() {
            @Override
            public ScrollItemBean createFromParcel(Parcel in) {
                return new ScrollItemBean(in);
            }

            @Override
            public ScrollItemBean[] newArray(int size) {
                return new ScrollItemBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ShopgoodsId);
            dest.writeString(CommodityName);
            dest.writeString(CommodityIconUrl);
            dest.writeLong(Inventory);
            dest.writeString(type);
            dest.writeByte((byte) (isSelect ? 1 : 0));
        }
    }

}
