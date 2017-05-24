package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.FriendModel_Table;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import cn.dazhou.railway.im.presenter.ChatPresenter;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends AppCompatActivity implements OnDataUpdateListener<ChatMessageEntity> {

    public static final String DATA_KEY = "jid";

    @BindView(R.id.chat_content)
    ChatContentView mChatContentView;
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
        mPresenter = new ChatPresenter(this, mJid);
        mPresenter.setOnDataUpdateListener(this);
        mPresenter.init();
        // 点击发送按钮时
        mChatContentView.setOnSendListener(mPresenter);
        mChatContentView.setOnImageClickListener(mPresenter);
        EventBus.getDefault().post(mJid);

    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(DATA_KEY, data);
        context.startActivity(intent);
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
    public void onUpdateData(List<ChatMessageEntity> datas) {
        mChatContentView.initChatDatas(datas);
    }

}
