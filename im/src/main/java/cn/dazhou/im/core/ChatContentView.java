package cn.dazhou.im.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.adapter.CommonFragmentPagerAdapter;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.im.entity.FullImageInfo;
import cn.dazhou.im.entity.ProcessEvent;
import cn.dazhou.im.fragment.ChatEmotionFragment;
import cn.dazhou.im.fragment.ChatFunctionFragment;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.GlobalOnItemClickManagerUtils;
import cn.dazhou.im.util.MediaManager;
import cn.dazhou.im.util.MediaPlayerUtils;
import cn.dazhou.im.util.Utils;
import cn.dazhou.im.widget.EmotionInputDetector;
import cn.dazhou.im.widget.NoScrollViewPager;
import cn.dazhou.im.widget.SoundView;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 * <p>
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

    private MediaPlayerUtils mMediaPlayer;


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

    public void destroy() {
        EventBus.getDefault().unregister(this);
        mMediaPlayer.release();
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.chat_content_view, this);
        ButterKnife.bind(this);
        mMediaPlayer = new MediaPlayerUtils(context);

        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        fragmentAdapter = new CommonFragmentPagerAdapter(((AppCompatActivity) context).getSupportFragmentManager(), fragments);
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

        chatList.setAdapterWithProgress(mAdapter);
//        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE:
//                        mAdapter.notifyDataSetChanged();
//                        break;
//                    case RecyclerView.SCROLL_STATE_DRAGGING:
//                        mDetector.hideEmotionLayout(false);
//                        mDetector.hideSoftInput();
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });



        chatList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    hideInput();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public boolean hideInput() {
        mDetector.hideSoftInput();
        mDetector.hideEmotionLayout(false);
        return true;
    }

    /**
     * 负责展示消息
     *
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
                    mOnSendListener.onSend(messageInfo, true);
                }
                break;
            case Constants.CHAT_ITEM_TYPE_LEFT:
                if (messageInfo.getVoiceBytes() != null) {
                    // 释放资源
                    messageInfo.setVoiceBytes(null);
                } else if (messageInfo.getImageBytes() != null) {
                    // 资源释放
                    messageInfo.setImageBytes(null);
                }
                break;
        }
        mAdapter.add(messageInfo);
        // 将光标移动到最新的消息处
        chatList.scrollToPosition(mAdapter.getCount() - 1);

    }

    /**
     * 更新文件下载进度
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessage(ProcessEvent event) {
        ChatMessageEntity message = new ChatMessageEntity();
        message.setFilePath(event.getFilePath());
        message.setFileProcess(event.getProcess());
        message.setType(event.getType());
        message.setDate(event.getDate());
        message.setDataType(event.getDataType());

        mAdapter.update(message);
    }



    public OnSendListener getOnSendListener() {
        return mOnSendListener;
    }

    public void setOnSendListener(OnSendListener mOnSendListener) {
        this.mOnSendListener = mOnSendListener;
    }

    @Override
    public void onHeaderClick(int position) {
        Toast.makeText(getContext(), "header", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onVideoClick(String fileUri, SurfaceView surfaceView) {
        Uri uri = Uri.fromFile(new File(fileUri));
        try {
            mMediaPlayer.startPlay(uri, surfaceView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailTipClick(final ChatMessageEntity entity) {
        new AlertDialog.Builder(getContext())
                .setMessage("是否重发？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entity.setSendState(Constants.CHAT_ITEM_SENDING);
                        mOnSendListener.onSend(entity, false);
                        mAdapter.updateSendState(entity);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create()
                .show();

    }

    @Override
    public byte onSuspendOrRestart() {
        return mMediaPlayer.suspendOrRestart();
    }

    public boolean interceptBackPress() {
        return mDetector.interceptBackPress();
    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        chatList.setRefreshListener(listener);
    }

    public void setLoadingTip(final String tip) {
        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                TextView tv = new TextView(getContext());
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(getContext(), 20)));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tv.setText(tip);
                return tv;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }


    public void onRefresh(final List<ChatMessageEntity> msgs, final boolean moveCursor) {
        if (msgs == null || msgs.size() == 0) {
            return;
        }
        mAdapter.removeAllHeader();
        mAdapter.insertAll(msgs, 0);
        if (moveCursor) {
            chatList.scrollToPosition(mAdapter.getCount() - 1);
        }
    }

    public void onRefresh(final List<ChatMessageEntity> msgs) {
        onRefresh(msgs, false);
    }

    // 当发送时，回调到ChatActivity，由其确认目前正在跟谁聊天
    public interface OnSendListener {
        void onSend(ChatMessageEntity msg, boolean saveMessage);
    }

    // 当发送时，回调到ChatActivity，由其确认目前正在跟谁聊天
    public interface OnImageClickListener {
        void onImageClick();
    }

}
