package cn.dazhou.im.core.function;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public interface IChat {
    void chatWith(String jid, String info) throws Exception;

    void showRoster();
}
