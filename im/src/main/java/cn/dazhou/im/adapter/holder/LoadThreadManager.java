package cn.dazhou.im.adapter.holder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hooyee on 2017/8/23.
 */

public class LoadThreadManager {
    private static ExecutorService executor =  Executors.newFixedThreadPool(5);
    public static ExecutorService getThreadPool() {
        return executor;
    }
}
