package cn.dazhou.railway.im.friend.message.list;

import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/5/11.
 */

public class MessageViewHolder extends BaseViewHolder<FriendModel> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_LatestMsg;
    private TextView mMessageCount;

    public MessageViewHolder(ViewGroup parent) {
        super(parent, R.layout.message_item);
        mTv_name = $(R.id.person_name);
        mTv_LatestMsg = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
        mMessageCount = $(R.id.message_count);
    }

    @Override
    public void setData(final FriendModel entry){
        mTv_name.setText(entry.getName());
        if (entry.getLatestChatMessage() != null) {
            mTv_LatestMsg.setText(entry.getLatestChatMessage().getContent());
        }
        if (entry.getNotReadCount() != 0) {
            mMessageCount.setVisibility(View.VISIBLE);
            mMessageCount.setText("[" + entry.getNotReadCount() + "]");
        } else {
            mMessageCount.setVisibility(View.GONE);
        }
        Glide.with(getContext())
                .load(R.drawable.header_01)
                .asBitmap()
                .into(mImg_face);
    }

}
