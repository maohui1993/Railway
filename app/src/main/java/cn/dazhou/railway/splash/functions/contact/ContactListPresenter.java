package cn.dazhou.railway.splash.functions.contact;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.chat.room.ChatRoomActivity;
import cn.dazhou.railway.im.friend.add.AddFriendActivity;

/**
 * Created by hooyee on 2017/5/31.
 */

public class ContactListPresenter implements ContactListContract.Presenter{
    private Context mContext;
    private ContactListContract.View mView;

    public ContactListPresenter(Context context, ContactListContract.View view) {
        mContext = context;
        mView = view;
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
                        ChatRoomActivity.startItself(mContext);
                    }
                }).start();
                break;
        }
    }
}
