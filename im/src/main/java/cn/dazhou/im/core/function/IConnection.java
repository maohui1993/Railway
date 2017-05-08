package cn.dazhou.im.core.function;

/**
 * Created by hooyee on 2017/5/5.
 */

public interface IConnection {
    IConnection connect(String ip) throws Exception;

    void login(String username, String password) throws Exception;
}
