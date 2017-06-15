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
    private String title;

    public ExtraInfo(){}

    protected ExtraInfo(Parcel in) {
        tel = in.readString();
        addr = in.readString();
        name = in.readString();
        title = in.readString();
    }

    public String getTelKey() {
        return "VOICE";
    }

    public String getAddrKey() {
        return "addr";
    }

    public String getNameKey() {
        return "name";
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        dest.writeString(title);
    }
}
