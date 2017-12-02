package com.five_o_one.ap1d;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Choco〜bourbon on 29-Nov-17.
 */

public class LocationData implements Parcelable {
    private int id=0;
    private String name="name";
    private String details="placeholder";
    private int imageUrl=0;
    private int selected;
    private Path[] paths=new Path[10];

    public LocationData(){}

    public LocationData(String name, String details, int imageUrl){
        this.name=name;
        this.details=details;
        this.imageUrl=imageUrl;
    }

    public LocationData(String name, String details, int imageUrl,Path[] paths, int selected){
        this.name=name;
        this.details=details;
        this.imageUrl=imageUrl;
        this.paths=paths;
        this.selected=selected;
    }

    public LocationData(Parcel in) {
        id=in.readInt();
        name=in.readString();
        details=in.readString();
        imageUrl=in.readInt();
        selected=in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int isSelected() {
        return selected;    //1-true 0-false
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setPaths(Path[] paths) {
        this.paths = paths;
    }

    public Path[] getPaths() {
        return paths;
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
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeInt(imageUrl);
        parcel.writeInt(selected);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ",name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isSelected='" + selected + '\'' +
                '}';
    }
}
