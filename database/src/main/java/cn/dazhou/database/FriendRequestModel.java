package cn.dazhou.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by hooyee on 2017/7/13.
 */

@Table(database = RailwayDatabase.class)
public class FriendRequestModel extends BaseModel implements Parcelable{
    @PrimaryKey(autoincrement = true)
    int id;
    @Column
    private String toJid;
    @Column
    private String fromJid;
    @Column
    private State state = State.NOT_HANDLE;

    protected FriendRequestModel(Parcel in) {
        id = in.readInt();
        toJid = in.readString();
        fromJid = in.readString();
        state = in.readParcelable(State.class.getClassLoader());
    }

    public FriendRequestModel() {}

    public static final Creator<FriendRequestModel> CREATOR = new Creator<FriendRequestModel>() {
        @Override
        public FriendRequestModel createFromParcel(Parcel in) {
            return new FriendRequestModel(in);
        }

        @Override
        public FriendRequestModel[] newArray(int size) {
            return new FriendRequestModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToJid() {
        return toJid;
    }

    public void setToJid(String toJid) {
        this.toJid = toJid;
    }

    public String getFromJid() {
        return fromJid;
    }

    public void setFromJid(String fromJid) {
        this.fromJid = fromJid;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        FriendRequestModel model = (FriendRequestModel)obj;
        return toJid.equals(model.toJid)
                && fromJid.equals(model.fromJid);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(toJid);
        dest.writeString(fromJid);
        dest.writeParcelable(state, flags);
    }

    public enum State implements Parcelable{
        ACCEPT(0),
        REJECT(1),
        NOT_HANDLE(2);

        private int value;

        State(int v) {
            value = v;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(value);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return State.values()[in.readInt()];
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };
    }
}
