package com.snh.snhseller.bean;

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
        public int CommTenantId;
        public String CommTenantName;
        public String CommTenantIcon;
        public int OnThePin;
        public double Price;
        public String UnitsTitle;
        public int status;
        public String CategoryName;
        public double MarketPrice;

        public long Inventory;
        public int total;
        public String type;

        public ScrollItemBean(String test, String type) {
            this.type = type;
        }

        public ScrollItemBean() {
        }

        protected ScrollItemBean(Parcel in) {
            CommTenantId = in.readInt();
            CommTenantName = in.readString();
            CommTenantIcon = in.readString();
            OnThePin = in.readInt();
            Price = in.readDouble();
            UnitsTitle = in.readString();
            status = in.readInt();
            Inventory = in.readLong();
            total = in.readInt();
            type = in.readString();
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
            dest.writeInt(CommTenantId);
            dest.writeString(CommTenantName);
            dest.writeString(CommTenantIcon);
            dest.writeInt(OnThePin);
            dest.writeDouble(Price);
            dest.writeString(UnitsTitle);
            dest.writeInt(status);
            dest.writeLong(Inventory);
            dest.writeInt(total);
            dest.writeString(type);
        }
    }

}
