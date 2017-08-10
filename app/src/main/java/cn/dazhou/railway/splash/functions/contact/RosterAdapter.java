package cn.dazhou.railway.splash.functions.contact;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.acpect.db.FriendDbApi;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.im.chat.ChatActivity;

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
        // 构造一个friend出来便于从数据中找到正确的friend
        String jid = StringUtil.getWrapJid(tipMessage.jid);
        model.setJid(jid);
        model.setPossessor(MyApp.gCurrentUsername);
        int index = getPosition(model);
        getItem(index).getLatestChatMessage().setContent(tipMessage.info);
        RosterViewHolder result = viewHolders.get(index);
        result.updateLatestMsg(tipMessage.info);
    }

    public void showMsgCount(FriendModel friendModel) {
        int index = getPosition(friendModel);
        RosterViewHolder result = viewHolders.get(index);
        // 增加一条消息
        result.addMessageCount(1);
    }

    public void markNewMsg(FriendModel friendModel) {
        int index = getPosition(friendModel);
        getItem(index).setHasNewMsg(true);
        sort(new Comparator<FriendModel>() {
            @Override
            public int compare(FriendModel o1, FriendModel o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
