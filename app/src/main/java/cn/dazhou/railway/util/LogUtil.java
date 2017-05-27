package cn.dazhou.railway.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * Created by hooyee on 2017/5/22.
 */

public class LogUtil {
    private static final String LOG_PATH = Environment.getExternalStorageDirectory() + "/railway/log/";
    private static final String LOG_NAME = "railway.log";
    private static PrintWriter print = null;

    public static void init() {
        File path = new File(LOG_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            File file = new File(path, LOG_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            print = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
        } catch (FileNotFoundException e) {
            Log.i("TAG", "LogUtil error1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("TAG", "LogUtil error2");
            e.printStackTrace();
        }
    }

    public static void write(String data) {
        if (print == null) {
            return;
        }
        print.write(data);
        print.flush();
    }

    public static void write(Throwable ex) {
        if (print == null) {
            return;
        }
        // 导出发生异常的时间
        print.println(getCurrentTime());
        print.println();
        // 导出异常的调用栈信息
        ex.printStackTrace(print);
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(System.currentTimeMillis());
    }

    public static void destroy() {
        if (print != null) {
            print.close();
        }
    }
}
