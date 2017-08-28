package cn.dazhou.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hooyee on 2017/8/15.
 */

public class VideoInfo implements Parcelable{
    private int locationX;
    private int locationY;
    private int width;
    private int height;
    private String videoUrl;

    public VideoInfo() {}

    protected VideoInfo(Parcel in) {
        locationX = in.readInt();
        locationY = in.readInt();
        width = in.readInt();
        height = in.readInt();
        videoUrl = in.readString();
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(locationX);
        dest.writeInt(locationY);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(videoUrl);
    }
}
