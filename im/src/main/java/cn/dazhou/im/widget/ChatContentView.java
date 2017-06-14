package cn.dazhou.im.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

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
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.FullImageInfo;
import cn.dazhou.im.fragment.ChatEmotionFragment;
import cn.dazhou.im.fragment.ChatFunctionFragment;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.GlobalOnItemClickManagerUtils;
import cn.dazhou.im.util.MediaManager;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 *
 * EventBus接受到的<ChatMessageEntity>事件信息都在这里集中处理
 */

public class ChatContentView extends LinearLayout implements ChatAdapter1.OnItemClickListener {
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
    private ChatEmotionFragment chatEmotionFragment;
    private CommonFragmentPagerAdapter fragmentAdapter;

    private OnSendListener mOnSendListener;
    private OnImageClickListener mOnImageClickListener;
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

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.mOnImageClickListener = onImageClickListener;
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.chat_content_view, this);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        fragmentAdapter = new CommonFragmentPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager(), fragments);
        viewpager.setAdapter(fragmentAdapter);
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

        // 将表情框的表情选取绑定到editText中显示
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(context);
        globalOnItemClickListener.attachToEditText(editText);

        mAdapter = new ChatAdapter1(context);
        mAdapter.setOnItemClickListener(this);
        chatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter.setNoMore(R.layout.view_nomore);

        mAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mAdapter.resumeMore();
            }
        });
//        chatList.setAdapter(mAdapter);
        chatList.setAdapterWithProgress(mAdapter);
        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void initChatDatas(List<ChatMessageEntity> chatDatas) {
        mAdapter.addAll(chatDatas);
        chatList.scrollToPosition(mAdapter.getCount() - 1);
    }

    public void addMessage(ChatMessageEntity msg) {
        mAdapter.add(msg);
    }

    /**
     * 负责展示消息
     * @param messageInfo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showMessage(final ChatMessageEntity messageInfo) {
        Log.i("TAG", "event-onsend" + "   ok");
        switch (messageInfo.getType()) {
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
                messageInfo.setDate(System.currentTimeMillis());
                // 发送按钮点击时，由上层决定消息是发送给谁，ChatContentView只做消息的展示，不做逻辑控制
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
                if (messageInfo.getVoiceBytes() != null) {
                    // 释放资源
                    messageInfo.setVoiceBytes(null);
                } else if(messageInfo.getImageBytes() != null) {
                    // 资源释放
                    messageInfo.setImageBytes(null);
                }
                break;
        }
        mAdapter.add(messageInfo);
        // 将光标移动到最新的消息处
        chatList.scrollToPosition(mAdapter.getCount() - 1);

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
    public void onImageClick(View view, ChatMessageEntity message) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        FullImageInfo fullImageInfo = new FullImageInfo();
        fullImageInfo.setLocationX(location[0]);
        fullImageInfo.setLocationY(location[1]);
        fullImageInfo.setWidth(view.getWidth());
        fullImageInfo.setHeight(view.getHeight());
        fullImageInfo.setImageUrl(message.getImagePath());
        EventBus.getDefault().postSticky(fullImageInfo);
        if (mOnImageClickListener != null) {
            mOnImageClickListener.onImageClick();
        }
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

    public boolean interceptBackPress() {
        return mDetector.interceptBackPress();
    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        chatList.setRefreshListener(listener);
    }

    Handler handler = new Handler();


    public void onRefresh(final List<ChatMessageEntity> msgs, final boolean moveCursor) {
        if (msgs == null || msgs.size() == 0) {
            return;
        }
        mAdapter.insertAll(msgs, 0);
        if (moveCursor) {
            chatList.scrollToPosition(mAdapter.getCount() - 1);
        }
    }

    // 当发送时，回调到ChatActivity，由其确认目前正在跟谁聊天
    public interface OnSendListener {
        void onSend(ChatMessageEntity msg);
    }

    // 当发送时，回调到ChatActivity，由其确认目前正在跟谁聊天
    public interface OnImageClickListener {
        void onImageClick();
    }
}
