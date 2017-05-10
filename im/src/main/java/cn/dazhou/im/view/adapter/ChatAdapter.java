package cn.dazhou.im.view.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.R;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.modle.SoundRecord;
import cn.dazhou.im.view.ChatMessageView;
import cn.dazhou.im.view.SoundView;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements ChatMessageView.OnSoundViewClickListener{
    private List<ChatMsgEntity> chatMsgs;
    private SoundRecord mSoundRecord;

    public ChatAdapter() {
        chatMsgs = new ArrayList<ChatMsgEntity>();
        mSoundRecord = new SoundRecord();
    }

    public void addMsg(String message) {
        ChatMsgEntity msg = new ChatMsgEntity();
        msg.setMessage(message);
        addMsg(msg);
    }
    public void addMsg(ChatMsgEntity message) {
        chatMsgs.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_info, parent, false);
        View msgView =  root.findViewById(R.id.chat_message);
        ViewHolder holder = new ViewHolder(msgView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageView.setText(chatMsgs.get(position).getMessage());
        final ChatMsgEntity msgEntity = chatMsgs.get(position);
        if (msgEntity.getMesImage() != null &&
                msgEntity.getMesImage().length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(msgEntity.getMesImage(), 0, msgEntity.getMesImage().length);
            holder.messageView.setImage(bmp);
        }
        if (msgEntity.getMsgSoundRecord() != null
                && msgEntity.getMsgSoundRecord().length > 0) {
            // 新启线程连接服务器
            holder.messageView.hasSoundInfo(true);
            holder.messageView.setSoundInfo(msgEntity.getMsgSoundRecord());
            holder.messageView.setOnSoundViewClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    @Override
    public void onSoundViewClick(View v) {
        Log.i("TAG", "应该是2");
        if (v instanceof SoundView) {
            SoundView soundView = (SoundView)v;
            mSoundRecord.startPlaying(soundView.getSoundFile().getAbsolutePath());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ChatMessageView messageView;
        public ViewHolder(View itemView) {
            super(itemView);
            messageView = (ChatMessageView) itemView;
        }
    }
}
