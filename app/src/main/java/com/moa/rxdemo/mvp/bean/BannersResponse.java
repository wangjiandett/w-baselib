package com.moa.rxdemo.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BannersResponse implements Parcelable {
    public int id;
    public String imgUrl;
    public String location;
    public String status;
    public long createDate;
    public int orderIndex;

    protected BannersResponse(Parcel in) {
        id = in.readInt();
        imgUrl = in.readString();
        location = in.readString();
        status = in.readString();
        createDate = in.readLong();
        orderIndex = in.readInt();
    }

    public static final Creator<BannersResponse> CREATOR = new Creator<BannersResponse>() {
        @Override
        public BannersResponse createFromParcel(Parcel in) {
            return new BannersResponse(in);
        }

        @Override
        public BannersResponse[] newArray(int size) {
            return new BannersResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imgUrl);
        dest.writeString(location);
        dest.writeString(status);
        dest.writeLong(createDate);
        dest.writeInt(orderIndex);
    }
}
