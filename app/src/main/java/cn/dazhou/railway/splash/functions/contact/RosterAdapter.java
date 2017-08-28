package cn.dazhou.railway.splash.functions.contact;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.im.chat.ChatActivity;

/**
 * Created by hooyee on 2017/5/8.
 */

public class RosterAdapter extends RecyclerArrayAdapter<FriendModel> {

    public RosterAdapter(Context context) {
        super(context);
        setOnItemClickListener(onItemClickListener);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        RosterViewHolder viewHolder = new RosterViewHolder(parent);
        return viewHolder;
    }

    // 点击item便是与指定用户进行聊天
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String jid = getItem(position).getJid();
            ChatActivity.startItself(getContext(), jid);
        }
    };

}
