package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.railway.im.activity.ChatActivity;
import cn.dazhou.railway.im.adapter.holder.RosterViewHolder;
import cn.dazhou.railway.im.db.FriendModel;

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
        return new RosterViewHolder(parent);
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
