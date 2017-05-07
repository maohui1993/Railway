package cn.dazhou.maputil.core;

import android.content.Context;
import android.view.ViewGroup;

import cn.dazhou.maputil.core.aspect.MapApi;
import cn.dazhou.maputil.core.impl.BaiduMapApiImpl;

/**
 * Created by hooyee on 2017/5/3.
 */

public class MapManager {
    public static final byte BAIDU_MAP = 0;

    private byte mapType;
    private Context context;
    private MapApi mapApi;

    public MapManager(Context context) {
        this(context, BAIDU_MAP);
    }

    public MapManager(Context context, byte mapType) {
        this.mapType = mapType;
        this.context = context;
        init();
    }

    private void init() {
        switch (mapType) {
            case BAIDU_MAP :
                mapApi = new BaiduMapApiImpl();
                mapApi.init(context);
                break;
        }
    }

    public void loadMap(ViewGroup content) {
        mapApi.loadMap(content);
    }

    public void getPosition() {
        mapApi.getCurrentPosition();
    }

}
