package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.R;
import cn.dazhou.railway.SplashActivity;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.FriendModel_Table;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import cn.dazhou.railway.im.presenter.ChatPresenter;

import static cn.dazhou.railway.config.Constants.DATA_KEY;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends AppCompatActivity implements OnDataUpdateListener<ChatMessageEntity> {

    @BindView(R.id.chat_content)
    ChatContentView mChatContentView;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private ChatPresenter mPresenter;
    /**
     * 正在chat的用户jid 形式为【正在聊天的用户jid+@+自身jid】
     */
    private String mJid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mJid = getIntent().getStringExtra(DATA_KEY);
        setTitle();
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.startItself(ChatActivity.this);
            }
        });
        mPresenter = new ChatPresenter(this, mJid);
        mPresenter.setOnDataUpdateListener(this);
        mPresenter.init();
        // 点击发送按钮时
        mChatContentView.setOnSendListener(mPresenter);
        mChatContentView.setOnImageClickListener(mPresenter);
        mChatContentView.setRefreshListener(mPresenter);
        EventBus.getDefault().post(mJid);
    }

    private void setTitle() {
        FriendModel friend = SQLite.select()
                .from(FriendModel.class)
                .where(FriendModel_Table.jid.eq(mJid))
                .querySingle();
        String title = mJid;
        if (friend != null) {
            title = friend.getName();
        } else {
            String name = getNameFromServer();
            if (title != null) {
                title = name;
            }
        }
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }

    private String getNameFromServer() {
        // 从服务器获取好友列表
        Roster roster = IMLauncher.getRoster();
        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if (entry.getJid().toString().contains(mJid)) {
                return entry.getName();
            }
        }
        return null;
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(DATA_KEY, data);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_activity, menu);
        return true;
    }

    @Override
    protected void onPause() {
        // 账号切换
        EventBus.getDefault().post("");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!mChatContentView.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mChatContentView.unregister();
        super.onDestroy();
    }

    @Override
    public void onUpdateData(List<ChatMessageEntity> datas, boolean moveCursor) {
        mChatContentView.onRefresh(datas, moveCursor);
    }

}
