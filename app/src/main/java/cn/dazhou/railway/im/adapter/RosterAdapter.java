package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.im.activity.ChatActivity;
import cn.dazhou.railway.im.adapter.holder.RosterViewHolder;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.fragment.ContactListFragment;

/**
 * Created by hooyee on 2017/5/8.
 */

public class RosterAdapter extends RecyclerArrayAdapter<FriendModel> {
    List<RosterViewHolder> viewHolders = new ArrayList<>();

    public RosterAdapter(Context context) {
        super(context);
        setOnItemClickListener(onItemClickListener);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        RosterViewHolder viewHolder = new RosterViewHolder(parent);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    // 点击item便是与指定用户进行聊天
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String jid = getItem(position).getJid();
            ChatActivity.startItself(getContext(), jid);
            viewHolders.get(position).restore();
        }
    };

    public void updateData(ContactListFragment.TipMessage tipMessage) {
        FriendModel model = new FriendModel();
        model.setJid(tipMessage.jid);
        model.setPossessor(MyApp.gCurrentUsername);
        int index = getPosition(model);
        getItem(index).getLatestChatMessage().setContent(tipMessage.info);
        RosterViewHolder result = viewHolders.get(index);
        result.updateLatestMsg(tipMessage.info);
    }

    public void updateData(FriendModel friendModel) {
        int index = getPosition(friendModel);
        RosterViewHolder result = viewHolders.get(index);
        result.updateMessageCount(1);
    }
}
