package cn.dazhou.railway.im.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.addfriend.AddFriendActivity;
import cn.dazhou.railway.im.activity.ChatRoomActivity;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;

/**
 * Created by hooyee on 2017/5/31.
 */

public class ContactListPresenter implements View.OnClickListener{
    private Context mContext;
    private OnDataUpdateListener mOnDataUpdateListener;
    private WeakReference<List<FriendModel>> cache;

    public ContactListPresenter(Context context) {
        mContext = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener mOnDataUpdateListener) {
        this.mOnDataUpdateListener = mOnDataUpdateListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_friend:
                Toast.makeText(mContext, "new Friend", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, AddFriendActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.chat_group:
                Toast.makeText(mContext, "Group Chat", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
////                        IMLauncher.createChatRoom("test2", "liaotian", "123");
//                        IMLauncher.joinChatRoom("test2", MyApp.gCurrentUsername, "123");
                        ChatRoomActivity.startItself(mContext);
                    }
                }).start();
                break;
        }
    }
}
