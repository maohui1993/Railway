package cn.dazhou.railway.im.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Date;
import java.util.Set;

import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.friend.info.FriendInfoActivity;
import cn.dazhou.railway.splash.SplashActivity;
import cn.dazhou.railway.util.ActivityUtils;

import static cn.dazhou.railway.config.Constants.DATA_KEY;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends BaseActivity {
    private static final String EXTRA_DATA = "_date";

    Toolbar mToolbar;
    private ChatPresenter mPresenter;
    private ChatFragment mChatFragment;
    /**
     * 正在chat的用户jid 形式为【正在聊天的用户jid+@+自身jid】
     */
    private String mJid;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayoutToBase(R.layout.activity_chat);
        mJid = getIntent().getStringExtra(DATA_KEY);
        mDate = (Date) getIntent().getSerializableExtra(EXTRA_DATA);

        mChatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mChatFragment == null) {
            // Create the fragment
            mChatFragment = ChatFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mChatFragment, R.id.contentFrame);
        }

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setTitle();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.startItself(ChatActivity.this);
            }
        });
        mPresenter = new ChatPresenter(this, mChatFragment, mJid, mDate);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info:
                        FriendInfoActivity.startItself(ChatActivity.this, StringUtil.getRealJid(mJid, MyApp.gServerIp));
                        break;
                }
                return false;
            }
        });

        // IMChatService#messageEventBus 处理该事件
        EventBus.getDefault().post(StringUtil.getUsername(mJid));
    }

    private void setTitle() {
        FriendModel friend = DataHelper.getFriend(mJid);
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
    }

    private String getNameFromServer() {
        // 从服务器获取好友列表
        Roster roster = null;
        try {
            roster = IMLauncher.getRoster();
            Set<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                if (entry.getJid().toString().contains(mJid)) {
                    return entry.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void startItself(Context context, String data) {
        startItself(context, data, null);
    }

    public static void startItself(Context context, String jid, Date date) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(DATA_KEY, jid);
        intent.putExtra(EXTRA_DATA, date);
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
        if (mChatFragment.back()) {
            super.onBackPressed();
        }
    }

}
