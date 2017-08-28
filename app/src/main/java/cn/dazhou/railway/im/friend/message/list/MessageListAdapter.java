package cn.dazhou.railway.im.friend.message.list;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.im.chat.ChatActivity;

/**
 * Created by hooyee on 2017/5/8.
 */

public class MessageListAdapter extends RecyclerArrayAdapter<FriendModel> {
    private int lastClickPosition = -1;

    public MessageListAdapter(Context context) {
        super(context);
        setOnItemClickListener(onItemClickListener);
        setOnItemLongClickListener(onItemLongClickListener);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        MessageViewHolder viewHolder = new MessageViewHolder(parent);
        return viewHolder;
    }

    // 点击item便是与指定用户进行聊天
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            // 点击了便代表读了
            getItem(position).setNotReadCount(0);
            notifyItemChanged(position);

            String jid = getItem(position).getJid();
            ChatActivity.startItself(getContext(), jid);
        }
    };

    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final int position) {
//            Toast.makeText(getContext(), "sdf", Toast.LENGTH_SHORT).show();
            lastClickPosition = position;
            return false;
        }
    };

    public void toTop(FriendModel object) {
        FriendModel item = getItemByObj(object);
        if (item != null) {
            remove(item);
        }
        insert(object, 0);
    }

    private FriendModel getItemByObj(FriendModel rawObj) {
        int position = getPosition(rawObj);
        if (position == -1) {
            return null;
        }
        return getItem(position);
    }

    public int getLastClickPosition() {
        return lastClickPosition;
    }

    public void delete(int position) {
        FriendModel item = getItem(position);
        remove(position);
        item.setInMessageList(false);
        item.update();
    }
}
