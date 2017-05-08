package cn.dazhou.im.view.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.R;
import cn.dazhou.im.core.modle.ChatMsgEntity;

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
        msg.setDate(message);
        addMsg(msg);
    }
    public void addMsg(ChatMsgEntity message) {
        chatMsgs.add(message);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_info, null, false);
        ViewHolder holder = new ViewHolder(root.findViewById(R.id.tx_info_item));
        holder.image = (ImageView) root.findViewById(R.id.image_info_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.msg.setText(chatMsgs.get(position).getDate());
        if (chatMsgs.get(position).getMesImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(chatMsgs.get(position).getMesImage(), 0, chatMsgs.get(position).getMesImage().length);
            holder.image.setImageDrawable(new BitmapDrawable(bmp));
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView;
        }
    }
}
