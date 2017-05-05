package cn.dazhou.maputil.core.aspect;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by hooyee on 2017/5/3.
 */

public interface MapApi {
    void init(Context context);
    void loadMap(ViewGroup content);
    void getCurrentPosition();
}
