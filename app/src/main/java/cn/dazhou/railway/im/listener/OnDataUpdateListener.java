package cn.dazhou.railway.im.listener;

import java.util.List;

/**
 * Created by Hooyee on 2017/5/21.
 * mail: hooyee_moly@foxmail.com
 */

public interface OnDataUpdateListener<T> {
    void onUpdateData(List<T> datas);
}
