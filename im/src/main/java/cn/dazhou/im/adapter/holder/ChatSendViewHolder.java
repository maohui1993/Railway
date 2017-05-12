package cn.dazhou.im.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.adapter.ChatAdapter1;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.widget.SoundView;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatSendViewHolder extends BaseViewHolder<ChatMsgEntity> {
    @BindView(R2.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R2.id.chat_item_header)
    ImageView chatItemHeader;
    //    @BindView(R2.id.chat_item_content_text)
//    GifTextView chatItemContentText;
//    @BindView(R2.id.chat_item_content_image)
//    BubbleImageView chatItemContentImage;
    @BindView(R2.id.chat_item_voice)
    SoundView chatItemVoice;
    @BindView(R2.id.chat_item_image)
    ImageView chatItemImage;
    //    @BindView(R.id.chat_item_layout_content)
//    BubbleLinearLayout chatItemLayoutContent;
    @BindView(R2.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    @BindView(R2.id.chat_item_text)
    TextView chatItemText;

    private ChatAdapter1.OnItemClickListener onItemClickListener;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter1.OnItemClickListener onItemClickListener) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setData(ChatMsgEntity data) {
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "");
        Glide.with(getContext()).load( R.drawable.header_02 ).asBitmap().into(chatItemHeader);
        if (data.getMessage() != null) {
            chatItemText.setText(data.getMessage());
        } else if (data.getMsgSoundRecord() != null) {
            chatItemVoice.setVisibility(View.VISIBLE);
            chatItemVoice.setSoundByte(data.getMsgSoundRecord());
        } else if (data.getMesImage() != null) {
            Glide.with(getContext()).load(data.getMesImage()).into(chatItemImage);
            chatItemImage.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.chat_item_voice)
    void playSound(SoundView v) {
        if (onItemClickListener != null) {
            onItemClickListener.onVoiceClick(v);
        }
    }
}
