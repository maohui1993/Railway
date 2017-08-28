package cn.dazhou.railway.im.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.railway.config.Constants;

public class IMReceiverManager extends BroadcastReceiver {
    private static List<Observer> observers = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        for (Observer o : observers) {
            switch (intent.getAction()) {
                case Constants.LOGIN_SUCCESS_BROADCAST:
                    o.whenLogined();
                    break;
                case Constants.NEW_REQUEST_BROADCAST:
                    o.updateFriendRequest();
                    break;
                case Constants.UPDATE_FROM_SERVER_BROADCAST:
                    o.updateFriendList();
                    break;
                default:
                    break;
            }
        }
    }

    public static void register(Observer callback) {
        observers.add(callback);
    }

    public static void unregister(Observer callback) {
        observers.remove(callback);
    }

    public interface Observer {
        void whenLogined();
        void updateFriendList();
        void updateFriendRequest();
    }
}
