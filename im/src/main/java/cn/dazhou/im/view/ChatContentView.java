package cn.dazhou.im.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.core.PhotoActivity;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.modle.SoundRecord;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatContentView extends LinearLayout{
    @BindView(R2.id.rv_chat_content)
    EasyRecyclerView mChatMessagesView;
//    @BindView(R2.id.edit_chat_input)
//    EditText mChatInput;
    @BindView(R2.id.bt_send)
    Button mSendBt;
    @BindView(R2.id.edit_text)
    EditText mChatInput;
    @BindView(R2.id.voice_text)
    TextView voiceText;
    @BindView(R2.id.emotion_voice)
    ImageView mVoiceImage;

    private OnSendListener mOnSendListener;
//    private ChatAdapter mAdapter;
    private ChatAdapter1 mAdapter;
    private SoundRecord mSoundRecord;

    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

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
//        mAdapter = new ChatAdapter();
        mAdapter = new ChatAdapter1(context);
        mChatMessagesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        mChatMessagesView.setHasFixedSize(true);
        mChatMessagesView.setAdapter(mAdapter);
        mSoundRecord = new SoundRecord();

        mVoiceImage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN :
//                        mVoiceImage.setText("松开 结束");
                        mSoundRecord.startRecording();
                        break;
                    case MotionEvent.ACTION_UP :
//                        mVoiceImage.setText("按住 录音");
                        try {
                            mSoundRecord.stopRecording();
                            byte[] bytes = mSoundRecord.getSoundRecord();
                            ChatMsgEntity msg = new ChatMsgEntity();
                            msg.setMsgSoundRecord(bytes);
                            msg.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                            addMessage(msg);
                            sendMultimediaMessage(msg);
                        } catch (Exception e) {

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL :
//                        mVoiceImage.setText("按住 录音");
                        try {
                            mSoundRecord.stopRecording();
                        } catch (Exception e) {}
                        break;
                }
                return false;
            }
        });
    }

    public void addMessage(String msg) {
        mAdapter.addMsg(msg);
    }

    public void addMessage(ChatMsgEntity msg) {
        mAdapter.add(msg);
        mAdapter.addMsg(msg);
    }

    void sendMultimediaMessage(ChatMsgEntity msg) {
        getOnSendListener().onSend(msg);
    }

    // Butterknife为lib-moudle生成的R2文件
    @OnClick(R2.id.bt_send)
    void sendMassage() {
        String info = mChatInput.getText().toString();
        byte[] bytes = Tool.createBitmapByPath(Tool.gPicPath);
        restore();
        if (getOnSendListener() != null) {
            ChatMsgEntity msg = new ChatMsgEntity();
            msg.setMessage(info);
            msg.setMesImage(bytes);
            msg.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
            getOnSendListener().onSend(msg);
            addMessage(msg);
        }
    }

//    @OnClick(R2.id.bt_photo)
    void selectPicture() {
        PhotoActivity.startItself(getContext());
    }

    private void restore() {
        Tool.gPicPath = null;
        mChatInput.setText("");
    }

    public OnSendListener getOnSendListener() {
        return mOnSendListener;
    }

    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }

    public interface OnSendListener {
        void onSend(ChatMsgEntity msg);
    }

}
