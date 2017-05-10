package cn.dazhou.im.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;

/**
 * Created by hooyee on 2017/5/8.
 */

public class Tool {
    public static String gPicPath;
    private static Gson gson;

    public static Object parseJSON(String jsonString, Class classType) {
        if (gson == null) {
            gson = new Gson();
        }
        Object obj = null;
        try {
            obj = gson.fromJson(jsonString, classType);
        } catch (JsonSyntaxException e) {
            Log.e("TAG", "load json is fail");
        }
        return obj;
    }

    public static String toJSON(Object obj) {
        if (gson == null) {
            gson = new Gson();
        }
        String json = null;
        try {
            json = gson.toJson(obj);
            Log.i("TAG", "json-info = " + json);
        } catch (JsonSyntaxException e) {
            Log.e("TAG", "load json is fail");
        }
        return json;
    }

    public static void checkPermission(Context context, String permission) {
        if( Build.VERSION.SDK_INT>=23){
            //android 6.0权限问题
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission)) {
                    Toast.makeText(context, "由于您拒绝授予权限，应用可能无法正常运行", Toast.LENGTH_LONG).show();
                } else {
                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{permission},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }

    public static byte[] createBitmapByPath(String path) {
        if (path == null || "".equals(path)) {
            return null;
        }

        Bitmap bmp = decodeSampledBitmapFromFile(path, 600, 600);
//        Bitmap bmp = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filename,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }
}
