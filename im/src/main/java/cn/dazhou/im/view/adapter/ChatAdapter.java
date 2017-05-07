package cn.dazhou.im.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.R;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<String> chatMsgs;

    public ChatAdapter() {
        chatMsgs = new ArrayList<String>();
    }

    public void addMsg(String message) {
        chatMsgs.add(message);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_info, parent, false);
        ViewHolder holder = new ViewHolder(root.findViewById(R.id.tx_info_item));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.msg.setText(chatMsgs.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        public ViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView;
        }
    }
}
