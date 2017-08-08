package cn.dazhou.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ManyToMany;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by hooyee on 2017/5/18.
 */

@Table(database = RailwayDatabase.class)
//@ManyToMany(referencedTable = FunctionItemModel.class)
public class UserModel extends BaseModel{
    @PrimaryKey
    private String username;   //用户jid
    @Column
    private String password;   //用户密码
    @Column
    private boolean firstLogin = false; // 记录用户是否第一次在当前机器登录, 默认为非第一次
    @Column
    private String nickName="";
    @Column
    private String email="";
    @Column
    private String tel="";

    List<FriendModel> friends;
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "friends")
    public List<FriendModel> getMyFriends() {
        if (friends == null || friends.isEmpty()) {
            friends = SQLite.select()
                    .from(FriendModel.class)
                    .where(FriendModel_Table.possessor.eq(username))
                    .queryList();
        }
        return friends;
    }

    public static UserModel getUser(String username) {
        return SQLite.select()
                .from(UserModel.class)
                .where(UserModel_Table.username.eq(username))
                .querySingle();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
