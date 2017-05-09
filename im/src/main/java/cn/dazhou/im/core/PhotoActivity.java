package cn.dazhou.im.core;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.dazhou.im.util.Tool;

public class PhotoActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Tool.gPicPath = picturePath;
            finish();
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, PhotoActivity.class);
        context.startActivity(intent);
    }

}
