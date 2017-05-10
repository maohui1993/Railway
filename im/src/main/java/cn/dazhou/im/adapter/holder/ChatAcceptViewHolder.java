package cn.dazhou.im.adapter.holder;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.R;
import cn.dazhou.im.R2;
import cn.dazhou.im.modle.ChatMsgEntity;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatAcceptViewHolder extends BaseViewHolder<ChatMsgEntity> {
    @BindView(R2.id.chat_item_date)
    TextView chatItemDate;
    @BindView(R2.id.chat_item_header)
    ImageView chatItemHeader;
//    @BindView(R2.id.chat_item_content_text)
//    GifTextView chatItemContentText;
//    @BindView(R2.id.chat_item_content_image)
//    BubbleImageView chatItemContentImage;
    @BindView(R2.id.chat_item_voice)
    ImageView chatItemVoice;
    @BindView(R2.id.chat_item_image)
    ImageView chatItemImage;
//    @BindView(R.id.chat_item_layout_content)
//    BubbleLinearLayout chatItemLayoutContent;
    @BindView(R2.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    @BindView(R2.id.chat_item_text)
    TextView chatItemText;

    public ChatAcceptViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(ChatMsgEntity data) {
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "");
        if (data.getMessage() != null) {
            chatItemText.setText(data.getMessage());
        } else if (data.getMsgSoundRecord() != null) {
            chatItemVoice.setVisibility(View.VISIBLE);
        } else if (data.getMesImage() != null) {
            chatItemImage.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.chat_item_voice)
    void startSound() {

    }
}
