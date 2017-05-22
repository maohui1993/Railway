package cn.dazhou.railway.im.presenter;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.im.db.UserModel_Table;
import cn.dazhou.railway.im.listener.IOnLoginListener;
import cn.dazhou.railway.im.service.IMChatService;
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
    private Context mContext;

    public LoginPresenter(Context context, IOnLoginListener loginListener) {
        mContext = context;
        mLoginListener = loginListener;
    }

    private void connection() {
        // 新启线程连接服务器
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                connected = IMLauncher.connect(mContext, Constants.SERVER_IP);
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
                connected = IMLauncher.connect(mContext, Constants.SERVER_IP);
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
                    MyApp.gCurrentUsername = username;
                    mLoginListener.onSuccess();
                    if (logined) {
                        UserModel userModel = SQLite.select()
                                .from(UserModel.class)
                                .where(UserModel_Table.username.eq(username))
                                .querySingle();

                        if (userModel == null) {
                            userModel = new UserModel();
                            userModel.setUsername(username);
                            userModel.setPassword(password);
                            userModel.setFirstLogin(true);
                            userModel.save();
                        }
                        // 先初始化全局user
                        MyApp.gCurrentUser = userModel;
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
