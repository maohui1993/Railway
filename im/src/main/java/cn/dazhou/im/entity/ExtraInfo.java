package cn.dazhou.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hooyee on 2017/6/15.
 */

public class ExtraInfo implements Parcelable{
    private String tel;
    private String addr;
    private String name;

    public ExtraInfo(){}

    protected ExtraInfo(Parcel in) {
        tel = in.readString();
        addr = in.readString();
        name = in.readString();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<ExtraInfo> CREATOR = new Creator<ExtraInfo>() {
        @Override
        public ExtraInfo createFromParcel(Parcel in) {
            return new ExtraInfo(in);
        }

        @Override
        public ExtraInfo[] newArray(int size) {
            return new ExtraInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tel);
        dest.writeString(addr);
        dest.writeString(name);
    }
}
