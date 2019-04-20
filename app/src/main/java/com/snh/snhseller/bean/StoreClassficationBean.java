package com.snh.snhseller.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/25<p>
 * <p>changeTime：2019/3/25<p>
 * <p>version：1<p>
 */
public class StoreClassficationBean implements Parcelable {
    public int Id;
    public String Name; // 类目名称
    public String EHaiTao; //企业海淘 价格
    public String ENoHaiTao; //企业非海淘 价格
    public String PHaiTao;//个人海淘 价格
    public String PNoHaiTao;// 个人非海淘 价格

    protected StoreClassficationBean(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        EHaiTao = in.readString();
        ENoHaiTao = in.readString();
        PHaiTao = in.readString();
        PNoHaiTao = in.readString();
    }

    public static final Creator<StoreClassficationBean> CREATOR = new Creator<StoreClassficationBean>() {
        @Override
        public StoreClassficationBean createFromParcel(Parcel in) {
            return new StoreClassficationBean(in);
        }

        @Override
        public StoreClassficationBean[] newArray(int size) {
            return new StoreClassficationBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(EHaiTao);
        dest.writeString(ENoHaiTao);
        dest.writeString(PHaiTao);
        dest.writeString(PNoHaiTao);
    }
}
