package cn.dazhou.railway.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.im.db.UserModel_Table;
import cn.dazhou.railway.im.login.LoginActivity;
import cn.dazhou.railway.im.service.IMChatService;
import cn.dazhou.railway.im.service.IMFriendRequestService;
import cn.dazhou.railway.splash.SplashActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hooyee on 2017/6/19.
 */

public class IMUtil {
    public static List<FriendModel> updateFriendFromServer(UserModel user) {
        // 从服务器获取好友列表
        Roster roster = IMLauncher.getRoster();

        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            FriendModel friend = new FriendModel();
            // raw-jid形式为  username@hostname
            String rawJid = entry.getJid().toString();
            Log.i("TAG", "friend type = " + entry.getType().name());
            String possessor = user.getUsername();
            // 存储的jid形式为  username@possessor
            friend.setJid(rawJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + possessor);
            friend.setRelation(FriendModel.typeToInt(entry.getType().name()));
            friend.setRawJid(rawJid);
            friend.setName(entry.getName());
            friend.setPossessor(possessor);
            ExtraInfo info = IMLauncher.getVCard(rawJid);
            friend.setNickName(info.getName());
            friend.setTel(info.getTel());
            user.getMyFriends().add(friend);
            user.setFirstLogin(false);
            user.save();
        }
        return user.getMyFriends();
    }

    public static void startServiceWhenLogin(Context context) {
        Log.i("TAG", "start service");
        IMChatService.startItself(context);
        IMFriendRequestService.startItself(context);
    }

    public static void stopServiceWhenLogout(Context context) {
        IMChatService.stopItself(context);
        IMFriendRequestService.stopItself(context);
    }

    static boolean connected;

    public static void checkUser(final Context context) {
        String lastLogin = SharedPreferenceUtil.getString(context, Constants.LATEST_LOGIN_JID, "");

        MyApp.gServerIp = SharedPreferenceUtil.getString(context, Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT);
        MyApp.gServerPort = SharedPreferenceUtil.getInt(context, Constants.SERVER_PORT, Constants.SERVER_PORT_DEFAULT);
        MyApp.gServerTimeout = SharedPreferenceUtil.getInt(context, Constants.SERVER_CONNECT_TIMEOUT, Constants.SERVER_CONNECT_TIMEOUT_DEFAULT);

        if (!"".equals(lastLogin)) {
            MyApp.gCurrentUsername = lastLogin;
            MyApp.gCurrentUser = UserModel.getUser(lastLogin);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    IMUtil.login(context);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    IMUtil.startServiceWhenLogin(context);
                }
            }).start();
        } else {
            LoginActivity.startItself(context);
        }
    }

    public static void logout(Context context) {
        IMLauncher.logout();
        MyApp.gCurrentUser = null;
        MyApp.gCurrentUsername = "";
        SharedPreferenceUtil.putString(context, Constants.LATEST_LOGIN_JID, MyApp.gCurrentUsername);
        IMUtil.stopServiceWhenLogout(context);
        IMLauncher.disconnect();
    }

    public static void login(final Context context) {
        try {
            IMLauncher.connect(context, MyApp.gServerIp, MyApp.gServerPort, MyApp.gServerTimeout);
            boolean login = IMLauncher.login(MyApp.gCurrentUsername, MyApp.gCurrentUser.getPassword());
            Log.i("TAG", "hasLog = " + login);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw =  new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            Log.i("TAG", "login fail : " + sw.toString());
        }
//        Observable.create(new ObservableOnSubscribe() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
//                try {
//                    connected = IMLauncher.connect(context, MyApp.gServerIp, MyApp.gServerPort, MyApp.gServerTimeout);
//                } catch (Exception ex) {
//                    connected = false;
//                    LogUtil.write(ex);
//                    StringWriter sw = new StringWriter();
//                    PrintWriter pw =  new PrintWriter(sw);
//                    //将出错的栈信息输出到printWriter中
//                    ex.printStackTrace(pw);
//                    pw.flush();
//                    sw.flush();
//                    Log.i("TAG", "login fail : " + sw.toString());
//                }
//                e.onNext(1);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        try {
//                            IMLauncher.login(MyApp.gCurrentUsername, MyApp.gCurrentUser.getPassword());
//                        } catch (Exception ex) {
//                            StringWriter sw = new StringWriter();
//                            PrintWriter pw =  new PrintWriter(sw);
//                            //将出错的栈信息输出到printWriter中
//                            ex.printStackTrace(pw);
//                            pw.flush();
//                            sw.flush();
//                            Log.i("TAG", "login fail : " + sw.toString());
//                        }
//                    }
//                });

    }

    public static void sendBroadcast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }
}
