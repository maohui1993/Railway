package cn.dazhou.im.fragment;

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
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.FileUtil;
import cn.dazhou.im.util.Tool;

/**
 * 作者：Rance on 2016/12/13 16:01
 * 邮箱：rance935@163.com
 */
public class ChatFunctionFragment extends BaseFragment {
    private View rootView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int REQUEST_CODE_PICK_FILE = 4;
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

    @OnClick(R2.id.chat_function_file)
    void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择要发送的文件"), REQUEST_CODE_PICK_FILE);
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case CROP_PHOTO:
                if (res == Activity.RESULT_OK) {
                    try {
                        if (imageUri == null) {
                            return;
                        }
                        Bitmap bmp = Tool.createBitmapByPath(imageUri.getPath(), 300, 400);
                        byte[] bytes = Tool.compressImage(bmp, 1024 * 2, 100);
                        ChatMessageEntity messageInfo = new ChatMessageEntity.Builder()
                                .imageBytes(bytes)
                                .imagePath(imageUri.getPath())
                                .dataType(ChatMessageEntity.Type.voice)
                                // 标记为自己发送的消息，显示在右边
                                .type(Constants.CHAT_ITEM_TYPE_RIGHT)
                                .build();
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                    }
                } else {
                    Log.d(Constants.TAG, "失败");
                }

                break;
            case REQUEST_CODE_PICK_IMAGE:
                if (res == Activity.RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    try {
                        Cursor cursor = getContext().getContentResolver().query(data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap bmp = Tool.createBitmapByPath(picturePath, 300, 400);
                        byte[] bytes = Tool.compressImage(bmp, 1024*2, 100);
                        ChatMessageEntity messageInfo = new ChatMessageEntity.Builder()
                                .imageBytes(bytes)
                                .imagePath(picturePath)
                                // 标记为自己发送的消息，显示在右边
                                .type(Constants.CHAT_ITEM_TYPE_RIGHT)
                                .dataType(ChatMessageEntity.Type.picture)
                                .build();
                        EventBus.getDefault().post(messageInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(Constants.TAG, e.getMessage());
                    }
                } else {
                    Log.d(Constants.TAG, "失败");
                }

                break;

            case REQUEST_CODE_PICK_FILE:
                // Get the Uri of the selected file
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String filePath = FileUtil.getPathByUri4kitkat(getContext(), uri);
                ChatMessageEntity messageInfo = new ChatMessageEntity.Builder()
                        .filePath(filePath)
                        // 标记为自己发送的消息，显示在右边
                        .type(Constants.CHAT_ITEM_TYPE_RIGHT)
                        .dataType(ChatMessageEntity.Type.file)
                        .build();
                EventBus.getDefault().post(messageInfo);
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
