package cn.dazhou.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Comparator;

/**
 * Created by hooyee on 2017/8/8.
 */

@Table(database = RailwayDatabase.class)
public class FunctionItemModel extends BaseModel implements Comparable<FunctionItemModel>, Parcelable{
    @PrimaryKey
    String functionname;       // 功能名称
    @PrimaryKey
    String jid;        // 用户Id
    @Column
    String iconUrl;    // 图标url
    @Column
    String url;       // 功能对应的超链接
    @Column
    int state;         // 状态：0停用，1正常
    @Column
    int sort;      // 记录功能的排序位置：-1代表未参加排序
    @Column
    int functiontype;

    public FunctionItemModel(){}

    private FunctionItemModel(String jid, String iconUrl, String functionname, String url, int state, int sort, int functiontype) {
        this.jid = jid;
        this.iconUrl = iconUrl;
        this.functionname = functionname;
        this.url = url;
        this.state = state;
        this.sort = sort;
        this.functiontype = functiontype;
    }

    protected FunctionItemModel(Parcel in) {
        functionname = in.readString();
        jid = in.readString();
        iconUrl = in.readString();
        url = in.readString();
        state = in.readInt();
        sort = in.readInt();
        functiontype = in.readInt();
    }

    public static final Creator<FunctionItemModel> CREATOR = new Creator<FunctionItemModel>() {
        @Override
        public FunctionItemModel createFromParcel(Parcel in) {
            return new FunctionItemModel(in);
        }

        @Override
        public FunctionItemModel[] newArray(int size) {
            return new FunctionItemModel[size];
        }
    };

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getFunctionname() {
        return functionname;
    }

    public void setFunctionname(String functionname) {
        this.functionname = functionname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getFunctiontype() {
        return functiontype;
    }

    public void setFunctiontype(int functiontype) {
        this.functiontype = functiontype;
    }

    @Override
    public int compareTo(@NonNull FunctionItemModel o) {
        return sort - o.getSort();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(functionname);
        dest.writeString(jid);
        dest.writeString(iconUrl);
        dest.writeString(url);
        dest.writeInt(state);
        dest.writeInt(sort);
        dest.writeInt(functiontype);
    }


    public static class Builder {
        String jid;
        String iconUrl;
        String functionname;
        String url;
        int state;
        int sort = -1;
        int functiontype;

        public Builder jid(String jid) {
            this.jid = jid;
            return this;
        }

        public Builder iconUrl(String url) {
            this.iconUrl = url;
            return this;
        }

        public Builder functionname(String functionname) {
            this.functionname = functionname;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder state(int state) {
            this.state = state;
            return this;
        }

        public Builder sort(int sort) {
            this.sort = sort;
            return this;
        }

        public Builder functiontype(int functiontype) {
            this.functiontype = functiontype;
            return this;
        }

        public FunctionItemModel build() {
            return new FunctionItemModel(jid, iconUrl, functionname, url, state, sort, functiontype);
        }
    }
}
