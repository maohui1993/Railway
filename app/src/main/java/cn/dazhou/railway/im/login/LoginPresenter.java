package cn.dazhou.railway.im.login;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.im.db.UserModel_Table;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.StringUtil;
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

public class LoginPresenter implements LoginContract.Presenter{
    private boolean connected;
    private boolean logined;
    private Context mContext;
    private LoginContract.View mLoginView;

    public LoginPresenter(Context context,  LoginContract.View view) {
        mContext = context;
        mLoginView = view;
        mLoginView.setPresenter(this);
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public boolean login(final String username, final String password) {
        // 新启线程连接服务器
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                try {
                    connected = IMLauncher.connect(mContext, MyApp.gServerIp, MyApp.gServerPort, MyApp.gServerTimeout);
                } catch (Exception ex) {
                    connected = false;
                    LogUtil.write(ex);
                }
                e.onNext(1);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (connected) {
                    try {
                        logined = IMLauncher.login(username, password);
                    } catch (Exception e) {
                        logined = false;
                        LogUtil.write(e);
                    }
                    MyApp.gCurrentUsername = username;
                    if (logined) {
                        UserModel userModel = SQLite.select()
                                .from(UserModel.class)
                                .where(UserModel_Table.username.eq(username))
                                .querySingle();

                        if (userModel == null) {
                            ExtraInfo info = IMLauncher.getVCard(StringUtil.getRealJid(username));
                            userModel = new UserModel();
                            userModel.setUsername(username);
                            userModel.setPassword(password);
                            userModel.setFirstLogin(true);
                            userModel.setNickName(info.getName());
                            userModel.setTel(info.getTel());
                            userModel.save();
                            IMUtil.updateFriendFromServer(userModel);
                        }
                        // 先初始化全局user
                        MyApp.gCurrentUser = userModel;
                        mLoginView.success();
                    } else {
                        mLoginView.fail("账号或密码错误");
                    }
                } else {
                    mLoginView.fail("网络连接异常");
                }
            }
        });
        return logined;
    }
}
