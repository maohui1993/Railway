package cn.dazhou.im.core.modle;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by hooyee on 2017/5/5.
 */

public class User implements Comparable<User>{
    private String id;
    private String nickname;
    private String username;
    private String password;
    private String telephone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int compareTo(@NonNull User o) {
        return nickname.compareTo(o.getNickname());
    }
}
