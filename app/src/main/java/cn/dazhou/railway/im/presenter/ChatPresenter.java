package cn.dazhou.railway.im.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.im.activity.ChatActivity;

/**
 * Created by hooyee on 2017/5/8.
 */

public class ChatPresenter implements ChatContentView.OnSendListener {
    private Context mContext;
    private String mJid;

    public ChatPresenter(Context mContext, String jid) {
        this.mContext = mContext;
        mJid = jid;
    }

    public void loadImageInfo(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Cursor cursor = mContext.getContentResolver().query(data.getData(), null, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = Tool.createBitmapByPath(picturePath, 600, 800);

            // 图片发送有问题 需要 修改
            ChatMsgEntity msg = new ChatMsgEntity();
            byte[] bytes = Tool.compressImage(bmp, 200, 100);
            msg.setMesImage(bytes);
            msg.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
            ((ChatActivity)mContext).addMessage(msg);
            onSend(msg);
        }
    }

    @Override
    public void onSend(ChatMsgEntity msg) {
        IMLauncher.chatWith(mJid, msg);
    }
}
