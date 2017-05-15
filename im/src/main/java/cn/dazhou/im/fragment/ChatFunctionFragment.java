package cn.dazhou.im.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;

/**
 * 作者：Rance on 2016/12/13 16:01
 * 邮箱：rance935@163.com
 */
public class ChatFunctionFragment extends BaseFragment {
    private View rootView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private File output;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat_function, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    /**
     * 拍照
     */
    @OnClick(R2.id.chat_function_photograph)
    void takePhoto() {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output = new File(file, System.currentTimeMillis() + ".jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);

    }

    /**
     * 从相册选取图片
     */
    @OnClick(R2.id.chat_function_photo)
    void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case CROP_PHOTO:
                if (res == Activity.RESULT_OK) {
                    try {
                        ChatMsgEntity messageInfo = new ChatMsgEntity();
                        Bitmap bmp = Tool.createBitmapByPath(imageUri.getPath(), 300, 400);
                        byte[] bytes = Tool.compressImage(bmp, 200, 100);
                        messageInfo.setMesImage(bytes);
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                    }
                } else {
                    Log.d(Constants.TAG, "失败");
                }

                break;
            case REQUEST_CODE_PICK_IMAGE:
                if (res == Activity.RESULT_OK) {
                    try {
                        Cursor cursor = getContext().getContentResolver().query(data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        ChatMsgEntity messageInfo = new ChatMsgEntity();
                        Bitmap bmp = Tool.createBitmapByPath(picturePath, 300, 400);
                        byte[] bytes = Tool.compressImage(bmp, 200, 100);
                        messageInfo.setMesImage(bytes);
                        // 标记为自己发送的消息，显示在右边
                        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(Constants.TAG, e.getMessage());
                    }
                } else {
                    Log.d(Constants.TAG, "失败");
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
