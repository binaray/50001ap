package com.five_o_one.ap1d;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Choco〜bourbon on 29-Nov-17.
 */

public class LocationData implements Parcelable {

    private String name="name";
    private String details="placeholder";
    private int imageUrl=0;

    public LocationData(){}

    public LocationData(String name, String details, int imageUrl){
        this.name=name;
        this.details=details;
        this.imageUrl=imageUrl;
    }

    public LocationData(Parcel in) {
        name=in.readString();
        details=in.readString();
        imageUrl=in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeInt(imageUrl);
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
