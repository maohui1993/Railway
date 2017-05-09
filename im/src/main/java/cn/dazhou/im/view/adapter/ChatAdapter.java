package cn.dazhou.im.view.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.dazhou.im.modle.ChatMsgEntity;
import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.R;
import cn.dazhou.im.view.ChatMessageView;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMsgEntity> chatMsgs;

    public ChatAdapter() {
        chatMsgs = new ArrayList<ChatMsgEntity>();
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
        if (chatMsgs.get(position).getMesImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(chatMsgs.get(position).getMesImage(), 0, chatMsgs.get(position).getMesImage().length);
            holder.messageView.setImage(bmp);
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ChatMessageView messageView;
        public ViewHolder(View itemView) {
            super(itemView);
            messageView = (ChatMessageView) itemView;
        }
    }
}
