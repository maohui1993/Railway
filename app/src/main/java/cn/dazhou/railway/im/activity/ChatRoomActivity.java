package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.R;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
    }

    public static void startItself(Context context) {
        Intent intent  = new Intent(context, ChatRoomActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.bt_invite)
    void click() {
        IMLauncher.inviteUser("room2", "1@192.168.1.39");
    }

    @OnClick(R.id.bt_create_room)
    void create() {
        IMLauncher.createChatRoom("room1", "room1", null);
    }

    @OnClick(R.id.bt_grant)
    void grant() {

    }

}
