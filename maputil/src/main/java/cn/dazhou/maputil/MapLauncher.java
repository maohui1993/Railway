package cn.dazhou.maputil;

import android.content.Context;
import android.view.ViewGroup;

import cn.dazhou.maputil.core.MapManager;

/**
 * Created by hooyee on 2017/5/3.
 */

public class MapLauncher {
    static MapManager mapManager;
    public static void init(Context context) {
        mapManager = new MapManager(context);
    }

    public static void loadMap(ViewGroup content) {
        mapManager.loadMap(content);
    }

    public static void getPosition() {
        mapManager.getPosition();
    }
}
