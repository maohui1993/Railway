package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.im.activity.ChatActivity;
import cn.dazhou.railway.im.adapter.holder.RosterViewHolder;

/**
 * Created by hooyee on 2017/5/8.
 */

public class RosterAdapter extends RecyclerArrayAdapter<RosterEntry> {

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
            EntityBareJid jid = (EntityBareJid) getItem(position).getJid();
            ChatActivity.startItself(getContext(), jid.toString());
        }
    };
}
