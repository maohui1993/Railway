package cn.dazhou.im.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.view.adapter.ChatAdapter;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatContentView extends LinearLayout {
    @BindView(R2.id.rv_chat_content)
    RecyclerView mChatMessagesView;
    @BindView(R2.id.edit_chat_input)
    EditText mChatInput;
    private OnSendListener mOnSendListener;

    private ChatAdapter mAdapter;

    public ChatContentView(Context context) {
        this(context, null);
    }

    public ChatContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.chat_content_view, this);
        ButterKnife.bind(this);
        mAdapter = new ChatAdapter();
        mChatMessagesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mChatMessagesView.setHasFixedSize(true);
        mChatMessagesView.setAdapter(mAdapter);
    }

    public void addMessage(String msg) {
        mAdapter.addMsg(msg);
        mAdapter.notifyDataSetChanged();
    }

    // Butterknife为lib-moudle生成的R2文件
    @OnClick(R2.id.bt_send)
    void sendMassage() {
        String info = mChatInput.getText().toString();
        mChatInput.setText("");
        if (getOnSendListener() != null) {
            getOnSendListener().onSend(info);
            addMessage(info);
        }
    }

    public OnSendListener getOnSendListener() {
        return mOnSendListener;
    }

    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }

    public interface OnSendListener {
        void onSend(String info);
    }

}
