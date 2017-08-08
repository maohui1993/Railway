package cn.dazhou.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by hooyee on 2017/8/8.
 */

@Table(database = RailwayDatabase.class)
public class FunctionItemModel extends BaseModel {
    @PrimaryKey
    String shortName;  // 简称
    @PrimaryKey
    String jid;        // 用户Id
    @Column
    String iconUrl;    // 图标url
    @Column
    String name;       // 功能名称
    @Column
    String href;       // 功能对应的超链接
    @Column
    int state;         // 状态：0停用，1正常
    @Column
    int position;      // 记录功能的排序位置：-1代表未参加排序

    public FunctionItemModel(){}

    private FunctionItemModel(String jid, String iconUrl, String name, String shortName, String href, int state, int position) {
        this.jid = jid;
        this.iconUrl = iconUrl;
        this.name = name;
        this.shortName = shortName;
        this.href = href;
        this.state = state;
        this.position = position;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class Builder {
        String jid;
        String iconUrl;
        String name;
        String shortName;
        String href;
        int state;
        int position = -1;

        public Builder jid(String jid) {
            this.jid = jid;
            return this;
        }

        public Builder iconUrl(String url) {
            this.iconUrl = url;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder href(String href) {
            this.href = href;
            return this;
        }

        public Builder state(int state) {
            this.state = state;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public FunctionItemModel build() {
            return new FunctionItemModel(jid, iconUrl, name, shortName, href, state, position);
        }
    }
}
