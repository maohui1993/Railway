package cn.dazhou.im.adapter.holder;

import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.modle.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Utils;
import cn.dazhou.im.widget.BubbleImageView;
import cn.dazhou.im.widget.BubbleLinearLayout;
import cn.dazhou.im.widget.GifTextView;
import cn.dazhou.im.widget.SoundView;

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

    private RelativeLayout.LayoutParams layoutParams;

    private ChatAdapter1.OnItemClickListener onItemClickListener;

    public ChatAcceptViewHolder(ViewGroup parent, ChatAdapter1.OnItemClickListener onItemClickListener) {
        super(parent, R.layout.item_chat_accept);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    @Override
    public void setData(ChatMessageEntity data) {
        chatItemDate.setText(data.getDate() != null ? data.getDate() : "");
        Glide.with(getContext()).load(R.drawable.header_01).asBitmap().into(chatItemHeader);
        if (data.getContent() != null) {
            chatItemContentText.setSpanText(null, data.getContent(), true);
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
            chatItemVoice.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            chatItemVoice.setVisibility(View.VISIBLE);
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
//            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
//                }
//            });
            layoutParams.width = Utils.dp2px(getContext(), 120);
            layoutParams.height = Utils.dp2px(getContext(), 48);
            chatItemLayoutContent.setLayoutParams(layoutParams);
        }
    }

}
