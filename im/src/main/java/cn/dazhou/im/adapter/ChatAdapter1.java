package cn.dazhou.im.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.im.adapter.holder.ChatAcceptViewHolder;
import cn.dazhou.im.adapter.holder.ChatSendViewHolder;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;

/**
 * Created by hooyee on 2017/5/10.
 */

public class ChatAdapter1 extends RecyclerArrayAdapter<ChatMsgEntity> {
    private OnItemClickListener onItemClickListener;

    public ChatAdapter1(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addMsg(String msg) {
    }

    public void addMsg(ChatMsgEntity msg) {
        notifyDataSetChanged();
    }
}
