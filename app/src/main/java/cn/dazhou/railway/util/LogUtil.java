package cn.dazhou.railway.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hooyee on 2017/5/22.
 */

public class LogUtil {
    private static final String LOG_PATH = Environment.getExternalStorageDirectory() + "/railway/log/";
    private static final String LOG_NAME = "railway.log1";
    private static FileOutputStream out;
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
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.i("TAG", "LogUtil error1");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("TAG", "LogUtil error2");
            e.printStackTrace();
        }
    }

    public static void write(byte[] bytes) {
        if (out == null) {
            return;
        }
        try {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void destroy() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
