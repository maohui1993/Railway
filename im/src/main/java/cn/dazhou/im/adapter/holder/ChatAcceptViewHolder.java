package cn.dazhou.im.adapter.holder;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.MediaPlayerUtils;
import cn.dazhou.im.util.Utils;
import cn.dazhou.im.widget.BubbleImageView;
import cn.dazhou.im.widget.BubbleLinearLayout;
import cn.dazhou.im.widget.GifTextView;
import cn.dazhou.im.widget.SoundView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatAcceptViewHolder extends BaseViewHolder<ChatMessageEntity> {
    @BindView(R2.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R2.id.chat_item_header)
    ImageView chatItemHeader;
    @BindView(R2.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @BindView(R2.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;
    @BindView(R2.id.chat_item_voice)
    SoundView chatItemVoice;
    @BindView(R2.id.chat_item_layout_content)
    BubbleLinearLayout chatItemLayoutContent;
    @BindView(R2.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    @BindView(R2.id.file_format)
    View fileContainer;
    @BindView(R2.id.video)
    View videoContent;
    @BindView(R2.id.file_video)
    ImageView video;
//    @BindView(R2.id.video_suspend)
//    ImageView suspend;
    @BindView(R2.id.thumbnail)
    ImageView thumbnail;

    private RelativeLayout.LayoutParams layoutParams;

    private ChatAdapter1.OnItemClickListener onItemClickListener;
    private Handler handler;

    public ChatAcceptViewHolder(ViewGroup parent, ChatAdapter1.OnItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_accept);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    @Override
    public void setData(final ChatMessageEntity data) {
        if (data.isShowTimestamp()) {
            chatItemDate.setText(Utils.getFormatTime(data.getDate()));
            chatItemDate.setVisibility(View.VISIBLE);
        } else {
            chatItemDate.setVisibility(View.GONE);
        }
        Glide.with(getContext()).load(R.drawable.header_01).asBitmap().into(chatItemHeader);
        if (data.getDataType() == ChatMessageEntity.Type.text) {
            chatItemContentText.setSpanText(handler, data.getContent(), true);
            chatItemVoice.setVisibility(View.GONE);
            videoContent.setVisibility(GONE);
            chatItemContentText.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.GONE);
            TextPaint paint = chatItemContentText.getPaint();
            // 计算textview在屏幕上占多宽
            int len = (int) paint.measureText(chatItemContentText.getText().toString().trim());
            if (len < Utils.dp2px(getContext(), 200)) {
                layoutParams.width = len + Utils.dp2px(getContext(), 30);
                layoutParams.height = Utils.dp2px(getContext(), 48);
            } else {
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            chatItemLayoutContent.setLayoutParams(layoutParams);
        } else if (data.getDataType() == ChatMessageEntity.Type.voice) {
            chatItemVoice.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            chatItemVoice.setVisibility(View.VISIBLE);
            videoContent.setVisibility(GONE);
            chatItemVoice.setVoicePath(data.getVoicePath());
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.VISIBLE);
            chatItemContentImage.setVisibility(View.GONE);
            chatItemVoiceTime.setText(Utils.formatTime(data.getVoiceTime()));
            chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVoiceClick(chatItemVoice);
                }
            });
            layoutParams.width = Utils.dp2px(getContext(), 120);
            layoutParams.height = Utils.dp2px(getContext(), 48);
            chatItemLayoutContent.setLayoutParams(layoutParams);
        } else if (data.getDataType() == ChatMessageEntity.Type.picture) {
            chatItemVoice.setVisibility(View.GONE);
            chatItemLayoutContent.setVisibility(View.GONE);
            videoContent.setVisibility(GONE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(data.getImagePath())
//                    .placeholder(R.mipmap.ic_launcher)
                    .into(chatItemContentImage);
            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(chatItemContentImage, data);
                }
            });
            layoutParams.width = Utils.dp2px(getContext(), 120);
            layoutParams.height = Utils.dp2px(getContext(), 48);
            chatItemLayoutContent.setLayoutParams(layoutParams);
        } else if (data.getDataType() == ChatMessageEntity.Type.file) {
            chatItemVoice.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            videoContent.setVisibility(GONE);
            chatItemContentImage.setVisibility(View.GONE);
            fileContainer.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            File file = new File(data.getFilePath());
            String name = file.getName();
            String size = Utils.convertSpaceUnit(file.length());
            ((TextView) fileContainer.findViewById(R.id.file_name)).setText(name);
            ((TextView) fileContainer.findViewById(R.id.file_size)).setText(size);
            ((ProgressBar) fileContainer.findViewById(R.id.progress)).setProgress(data.getFileProcess());
            fileContainer.findViewById(R.id.scroll).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    fileContainer.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            layoutParams.width = Utils.dp2px(getContext(), 250);
            layoutParams.height = Utils.dp2px(getContext(), 75);
            chatItemLayoutContent.setLayoutParams(layoutParams);
        } else if (data.getDataType() == ChatMessageEntity.Type.video) {
            chatItemVoice.setVisibility(GONE);
            chatItemVoiceTime.setVisibility(GONE);
            chatItemContentText.setVisibility(GONE);
            chatItemContentImage.setVisibility(GONE);
            videoContent.setVisibility(VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            fileContainer.setVisibility(GONE);
            video.setVisibility(View.VISIBLE);

            Bitmap bmp = MediaPlayerUtils.getVideoThumbnail(data.getFilePath());
            thumbnail.setImageBitmap(bmp);

            // 点击之后改变状态并处理
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVideoClick(v, data);
                }
            });

            layoutParams.width = Utils.dp2px(getContext(), 200);
            layoutParams.height = Utils.dp2px(getContext(), 150);
            chatItemLayoutContent.setLayoutParams(layoutParams);
        }
    }

}
