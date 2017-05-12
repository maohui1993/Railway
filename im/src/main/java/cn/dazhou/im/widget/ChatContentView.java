package cn.dazhou.im.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R2.id.bt_send)
    Button mSendBt;
    @BindView(R2.id.function_add)
    ImageView mAddFunctionIv;
    @BindView(R2.id.more_function)
    View mAdditionalFunc;
    @BindView(R2.id.edit_text)
    EditText mChatInput;
    @BindView(R2.id.voice_bt)
    Button mVoiceBt;
    @BindView(R2.id.voice_text)
    TextView mVoiceText;
    @BindView(R2.id.emotion_voice)
    ImageView mVoiceImage;
    @BindView(R2.id.microphone)
    View mMicrophoneView;

    private OnSendListener mOnSendListener;
    private ChatAdapter1 mAdapter;
    private SoundRecord mSoundRecord;

//    //录音相关
//    int animationRes = 0;
//    int res = 0;
//    AnimationDrawable animationDrawable = null;
//    private ImageView animView;

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
        mAdapter = new ChatAdapter1(context);
        mChatMessagesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mChatMessagesView.setAdapter(mAdapter);
        mSoundRecord = new SoundRecord();

        mChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showSendBt(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVoiceBt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onVoiceBtTouch(event);
                return false;
            }
        });
    }

    private void onVoiceBtTouch(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
                mVoiceBt.setText("松开 结束");
                mSoundRecord.startRecording();
//                        mMicrophoneView.setVisibility(VISIBLE);
                break;
            case MotionEvent.ACTION_UP :
                mVoiceBt.setText("按住 录音");
                try {
                    mSoundRecord.stopRecording();
                    byte[] bytes = mSoundRecord.getSoundRecord();
                    ChatMsgEntity msg = new ChatMsgEntity();
                    msg.setMsgSoundRecord(bytes);
                    msg.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
                    // 显示自己发送的
                    addMessage(msg);
                    sendMultimediaMessage(msg);
                } catch (Exception e) {

                }
                break;
            case MotionEvent.ACTION_CANCEL :
                mVoiceBt.setText("按住 录音");
                try {
                    mSoundRecord.stopRecording();
                } catch (Exception e) {}
                break;
        }
    }

    public void addMessage(ChatMsgEntity msg) {
        mAdapter.add(msg);
    }

    void sendMultimediaMessage(ChatMsgEntity msg) {
        getOnSendListener().onSend(msg);
    }

    @OnClick(R2.id.bt_send)
    void sendMassage() {
        String info = mChatInput.getText().toString();
        mChatInput.setText("");
        ChatMsgEntity msg = new ChatMsgEntity();
        msg.setMessage(info);
        msg.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        // 显示自己的聊天信息
        addMessage(msg);
        // 将聊天信息发送给别人
        getOnSendListener().onSend(msg);
        showSendBt(false);
    }

    private void showSendBt(boolean visibility) {
        if (visibility) {
            mSendBt.setVisibility(VISIBLE);
            mAddFunctionIv.setVisibility(GONE);
        } else {
            mSendBt.setVisibility(GONE);
            mAddFunctionIv.setVisibility(VISIBLE);
        }
    }

    @OnClick(R2.id.function_add)
    void showMoreFunction() {
        mAdditionalFunc.setVisibility(VISIBLE);
    }

    @OnClick(R2.id.iv_gallery)
    void loadImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)getContext()).startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
    }

    @OnClick(R2.id.emotion_voice)
    void updateVoiceButtonVisibility() {
        int currentState = mChatInput.getVisibility();
        switch (currentState) {
            case VISIBLE:
                mChatInput.setVisibility(GONE);
                mVoiceBt.setVisibility(VISIBLE);
                break;
            default:
                mChatInput.setVisibility(VISIBLE);
                mVoiceBt.setVisibility(GONE);
        }
    }

    public OnSendListener getOnSendListener() {
        return mOnSendListener;
    }

    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }

    // 图片发送
    public interface OnSendListener {
        void onSend(ChatMsgEntity msg);
    }

}
