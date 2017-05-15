package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.core.function.INewMessageListener;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.adapter.ChatPagerAdapter;
import cn.dazhou.railway.im.presenter.ChatPresenter;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends AppCompatActivity implements INewMessageListener {
    private static final String DATA_KEY = "jid";

    @BindView(R.id.chat_content)
    ChatContentView mChatContentView;
    private ChatPresenter mPresenter;
    /**
     * 正在chat的用户jid
     */
    private String mJid;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mJid = getIntent().getStringExtra(DATA_KEY);
        mPresenter = new ChatPresenter(this, mJid);

        // 点击发送按钮时
        mChatContentView.setOnSendListener(mPresenter);
        // 当有新的消息时显示出来
        IMLauncher.setNewMessageListener(this);
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(DATA_KEY, data);
        context.startActivity(intent);
    }

    public void addMessage(ChatMsgEntity msg) {
        mChatContentView.addMessage(msg);
    }

    @Override
    public void showNewMessage(final Message msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatMsgEntity msgEntity = (ChatMsgEntity) Tool.parseJSON(msg.getBody(), ChatMsgEntity.class);
                msgEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                mChatContentView.addMessage(msgEntity);
            }
        });
    }

}
