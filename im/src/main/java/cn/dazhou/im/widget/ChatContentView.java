package cn.dazhou.im.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.adapter.CommonFragmentPagerAdapter;
import cn.dazhou.im.fragment.ChatFunctionFragment;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.modle.SoundRecord;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.MediaManager;
import cn.dazhou.im.util.Utils;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatContentView extends LinearLayout implements ChatAdapter1.OnItemClickListener{
    @BindView(R2.id.chat_list)
    EasyRecyclerView chatList;
    @BindView(R2.id.emotion_voice)
    ImageView emotionVoice;
    @BindView(R2.id.edit_text)
    EditText editText;
    @BindView(R2.id.voice_text)
    TextView voiceText;
    @BindView(R2.id.emotion_button)
    ImageView emotionButton;
    @BindView(R2.id.emotion_add)
    ImageView emotionAdd;
    @BindView(R2.id.emotion_send)
    Button emotionSend;
    @BindView(R2.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R2.id.emotion_layout)
    RelativeLayout emotionLayout;

    private EmotionInputDetector mDetector;

    private ArrayList<Fragment> fragments;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter fragementAdapter;

    private OnSendListener mOnSendListener;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.chat_content_view, this);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        fragementAdapter = new CommonFragmentPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager(), fragments);
        viewpager.setAdapter(fragementAdapter);
        viewpager.setCurrentItem(0);
        EventBus.getDefault().register(this);

        mDetector = EmotionInputDetector.with((Activity) context)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        mAdapter = new ChatAdapter1(context);
        mAdapter.setOnItemClickListener(this);
        chatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chatList.setAdapter(mAdapter);
        mSoundRecord = new SoundRecord();
    }


    public void addMessage(ChatMsgEntity msg) {
        mAdapter.add(msg);
    }

    void sendMultimediaMessage(ChatMsgEntity msg) {
        getOnSendListener().onSend(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final ChatMsgEntity messageInfo) {
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfo.setDate(Utils.getCurrentTime());
        mAdapter.add(messageInfo);
        chatList.scrollToPosition(mAdapter.getCount() - 1);
        if (mOnSendListener != null) {
            mOnSendListener.onSend(messageInfo);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            }
        }, 2000);
    }

    public OnSendListener getOnSendListener() {
        return mOnSendListener;
    }

    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }

    @Override
    public void onHeaderClick(int position) {

    }

    @Override
    public void onImageClick(View view) {

    }

    @Override
    public void onVoiceClick(SoundView soundView) {
        if (animView != null) {
            animView.setImageResource(res);
            animView = null;
        }
        switch (soundView.getType()) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                animationRes = R.drawable.voice_left;
                res = R.mipmap.icon_voice_left3;
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                animationRes = R.drawable.voice_right;
                res = R.mipmap.icon_voice_right3;
                break;
        }
        animView = soundView;
        animView.setImageResource(animationRes);
        animationDrawable = (AnimationDrawable) animView.getDrawable();
        animationDrawable.start();
        MediaManager.playSound(soundView.getSoundFile().getPath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                animView.setImageResource(res);
            }
        });
    }

    // 图片发送
    public interface OnSendListener {
        void onSend(ChatMsgEntity msg);
    }

}
