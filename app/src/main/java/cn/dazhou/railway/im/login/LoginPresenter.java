package cn.dazhou.railway.im.login;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import cn.dazhou.database.UserModel;
import cn.dazhou.database.UserModel_Table;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.im.util.Config;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;
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
    private static final String OFFSET_Y = "offset_y";

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
                    Config.gCurrentUsername = MyApp.gCurrentUsername;
                    if (logined) {
                        UserModel userModel = SQLite.select()
                                .from(UserModel.class)
                                .where(UserModel_Table.username.eq(username))
                                .querySingle();

                        if (userModel == null) {
                            ExtraInfo info = IMLauncher.getVCard(StringUtil.getRealJid(username, MyApp.gServerIp));
                            userModel = new UserModel();
                            userModel.setUsername(username);
                            userModel.setPassword(password);
                            userModel.setFirstLogin(true);
                            userModel.setNickName(info.getName());
                            userModel.setTel(info.getTel());
                            userModel.save();
                            DataHelper.updateFriendFromServer(userModel);
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

    @Override
    public float calculateOffset(Activity activity, View v) {
        int softInputHeight = ActivityUtils.getSupportSoftInputHeight(activity);
        int[] position = new int[2];
        int actionBarH = 0;
        v.getLocationOnScreen(position);
        int vBottom = v.getHeight() + position[1];
        int screenHeight = ActivityUtils.getScreenHeight(activity);
        if (((AppCompatActivity)activity).getSupportActionBar() != null) {
            actionBarH = ((AppCompatActivity) activity).getSupportActionBar().getHeight();
        }
        float visibleY = screenHeight - actionBarH - softInputHeight - vBottom;
        if (visibleY < 0) {
            return visibleY;
        }
        return 0;
    }

    @Override
    public float getOffset() {
        return SharedPreferenceUtil.getFloat(mContext, OFFSET_Y, 0f);
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.password_warp:
            case R.id.edit_password:
            case R.id.username_wrap:
            case R.id.edit_username:
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//                    updateUI(v);
                    mLoginView.updateInputState(mLoginView.INPUTTING);
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void updateUI(final View v) {
        final float offset = SharedPreferenceUtil.getFloat(v.getContext(), OFFSET_Y, 0f);
        if (offset != 0f) {
            mLoginView.setOffset(offset);
        } else {
            v.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    float offset = calculateOffset(mLoginView.getActivity(), mLoginView.getReLayoutView());
                    SharedPreferenceUtil.putFloat(v.getContext(), OFFSET_Y, offset);
                    mLoginView.setOffset(offset);
                }
            }, 500L);
        }
    }

    @Override
    public void listenerInputState(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 软键盘高度为0，则表示state=NOT_INPUT
                    if ( ActivityUtils.getSupportSoftInputHeight(mLoginView.getActivity()) == 0) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    }
                }
            }
        }).start();
    }
}
