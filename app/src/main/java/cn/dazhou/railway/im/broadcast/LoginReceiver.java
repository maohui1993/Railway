package cn.dazhou.railway.im.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class LoginReceiver extends BroadcastReceiver {
    private static List<Callback> callbacks = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        for (Callback c : callbacks) {
            c.callback();
        }
    }

    public static void subscibe(Callback callback) {
        callbacks.add(callback);
    }

    public static void unsubscribe(Callback callback) {
        callbacks.remove(callback);
    }

    public interface Callback {
        void callback();
    }
}
