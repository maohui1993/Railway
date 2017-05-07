package cn.dazhou.railway.im.listener;

/**
 * Created by hooyee on 2017/5/5.
 */

public interface IOnLoginListener {
    /**
     * 登录成功时回调
     */
    void onSuccess();

    /**
     * 登录失败时回调
     * @param info
     */
    void onFail(String info);
}
