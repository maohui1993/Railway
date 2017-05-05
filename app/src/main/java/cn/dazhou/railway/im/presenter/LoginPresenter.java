package cn.dazhou.railway.im.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.im.listener.IOnLoginListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hooyee on 2017/5/5.
 */

public class LoginPresenter {
    private boolean connected;
    private boolean logined;
    private IOnLoginListener mLoginListener;

    public LoginPresenter(IOnLoginListener loginListener) {
        mLoginListener = loginListener;
    }

    private void connection() {
        // 新启线程连接服务器
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                connected = IMLauncher.connect("192.168.1.39");
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe(); // 回调为空
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(final String username, final String password) {
        // 新启线程连接服务器
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                connected = IMLauncher.connect("192.168.1.39");
                e.onNext(1);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (connected) {
                    logined = IMLauncher.login(username, password);
                    if (logined) {
                        mLoginListener.onSuccess();
                    } else {
                        mLoginListener.onFail("账号或密码错误");
                    }
                } else {
                    mLoginListener.onFail("网络连接异常");
                }
            }
        });
    }
}
