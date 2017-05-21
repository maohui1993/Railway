package cn.dazhou.im.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.adapter.CommonFragmentPagerAdapter;
import cn.dazhou.im.fragment.ChatFunctionFragment;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.MediaManager;
import cn.dazhou.im.util.Tool;
import cn.dazhou.im.util.Utils;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 *
 * EventBus接受到的<ChatMessageEntity>事件信息都在这里集中处理
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
    }

    public void initChatDatas(List<ChatMessageEntity> chatDatas) {
        mAdapter.addAll(chatDatas);
    }

    public void addMessage(ChatMessageEntity msg) {
        mAdapter.add(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final ChatMessageEntity messageInfo) {
        switch (messageInfo.getType()) {
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
                messageInfo.setDate(Utils.getCurrentTime());
                mAdapter.add(messageInfo);
                // 将光标移动到最新的消息处
                chatList.scrollToPosition(mAdapter.getCount() - 1);
                if (mOnSendListener != null) {
                    mOnSendListener.onSend(messageInfo);
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                    }
                }, 2000);
                break;
            case Constants.CHAT_ITEM_TYPE_LEFT:
                if (messageInfo.getVoiceBtyes() != null) {
                    // 需要异步加载声音文件
                    String voicePath = Tool.saveByteToLocalFile(messageInfo.getVoiceBtyes(), +System.currentTimeMillis()+".aar");
                    messageInfo.setVoicePath(voicePath);
                    messageInfo.setVoiceTime(messageInfo.getVoiceTime());
                    messageInfo.setVoiceBtyes(null);
                } else if(messageInfo.getImageBytes() != null) {
                    String imagePath = Tool.saveByteToLocalFile(messageInfo.getImageBytes(), System.currentTimeMillis()+".png");
                    messageInfo.setImagePath(imagePath);
                    messageInfo.setImageBytes(null);
                }
                mAdapter.add(messageInfo);
                break;
        }
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
        MediaManager.playSound(soundView.getVoicePath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                animView.setImageResource(res);
            }
        });
    }

    // 当发送时，回调到ChatActivity，由其确认目前正在跟谁聊天
    public interface OnSendListener {
        void onSend(ChatMessageEntity msg);
    }

}
