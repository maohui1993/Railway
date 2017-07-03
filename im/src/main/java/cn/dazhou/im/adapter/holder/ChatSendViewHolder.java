package cn.dazhou.im.adapter.holder;

import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Utils;
import cn.dazhou.im.widget.BubbleImageView;
import cn.dazhou.im.widget.BubbleLinearLayout;
import cn.dazhou.im.widget.GifTextView;
import cn.dazhou.im.widget.SoundView;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatSendViewHolder extends BaseViewHolder<ChatMessageEntity> {
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
    @BindView(R2.id.chat_item_fail)
    ImageView chatItemFail;
    @BindView(R2.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    @BindView(R2.id.chat_item_progress)
    ProgressBar chatItemProgress;
    @BindView(R2.id.file_format)
    View fileContainer;
    private RelativeLayout.LayoutParams layoutParams;

    private ChatAdapter1.OnItemClickListener onItemClickListener;
    private Handler handler;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter1.OnItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    @Override
    public void setData(final ChatMessageEntity data) {
        chatItemDate.setText(Utils.getFormatTime(data.getDate()));
        chatItemDate.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(R.drawable.header_02).asBitmap().into(chatItemHeader);
        if (data.getContent() != null) {
            chatItemContentText.setSpanText(handler, data.getContent(), true);
            chatItemVoice.setVisibility(View.GONE);
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
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            chatItemLayoutContent.setLayoutParams(layoutParams);
        } else if (data.getVoicePath() != null) {
            chatItemVoice.setVisibility(View.VISIBLE);
            chatItemVoice.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
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
        } else if (data.getImagePath() != null) {
            chatItemVoice.setVisibility(View.GONE);
            chatItemLayoutContent.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(data.getImagePath()).into(chatItemContentImage);
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
            fileContainer.setVisibility(View.VISIBLE);
            File file = new File(data.getFilePath());
            String name = file.getName();
            String size = convertSpaceUnit(file.length());
            ((TextView)fileContainer.findViewById(R.id.file_name)).setText(name);
            ((TextView)fileContainer.findViewById(R.id.file_size)).setText(size);
        }

        switch (data.getSendState()) {
            case Constants.CHAT_ITEM_SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }


    private String convertSpaceUnit(long size) {
        float kb = size / 1024.0f;
        float mb = kb / 1024.0f;
        DecimalFormat decimalFormat= new DecimalFormat(".00");
        if (mb < 1) {
            return decimalFormat.format(kb) + " KB";
        } else {
            return decimalFormat.format(mb) + " MB";
        }
    }
}
