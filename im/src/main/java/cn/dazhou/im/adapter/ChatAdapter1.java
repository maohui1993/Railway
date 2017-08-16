package cn.dazhou.im.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

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
    private Handler handler;

    public ChatAdapter1(Context context) {
        super(context);
        handler = new Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler);
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

    public void update(ChatMessageEntity object) {
        int pos = getPosition(object);
        if (pos == -1) {
//            pos = 0;
            return;
        }
        Log.i("FILE", "position = " + pos);
        ChatMessageEntity goal = getItem(pos);
        goal.setFileProcess(object.getFileProcess());
        notifyItemChanged(pos);

    }

    public void updateSendState(ChatMessageEntity entity) {
        int pos = getPosition(entity);
        if (pos == -1) {
            pos = 0;
        }
        Log.i("FILE", "position = " + pos);
        ChatMessageEntity goal = getItem(pos);
        goal.setSendState(entity.getSendState());
        update(goal, pos);
    }

    public interface OnItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, ChatMessageEntity message);

        void onVoiceClick(SoundView soundView);

        void onVideoClick(View v, ChatMessageEntity entity);

        void onFailTipClick(ChatMessageEntity data);
    }
}
