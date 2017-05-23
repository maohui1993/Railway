package cn.dazhou.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.im.adapter.holder.ChatAcceptViewHolder;
import cn.dazhou.im.adapter.holder.ChatSendViewHolder;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.widget.SoundView;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatAdapter1 extends RecyclerArrayAdapter<ChatMessageEntity> {

    private OnItemClickListener onItemClickListener;

    public ChatAdapter1(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, ChatMessageEntity message);

        void onVoiceClick(SoundView soundView);
    }
}
