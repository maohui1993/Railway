package cn.dazhou.railway.splash.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.db.FriendModel;

/**
 * Created by hooyee on 2017/5/11.
 */

public class RosterViewHolder extends BaseViewHolder<FriendModel> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_LatestMsg;
    private TextView mMessageCount;

    public RosterViewHolder(ViewGroup parent) {
        super(parent, R.layout.roster_item);
        mTv_name = $(R.id.person_name);
        mTv_LatestMsg = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
        mMessageCount = $(R.id.message_count);
    }

    @Override
    public void setData(final FriendModel entry){
        Log.i("ViewHolder","position"+getDataPosition());
        mTv_name.setText(entry.getName());
        if (entry.getLatestChatMessage() != null) {
            mTv_LatestMsg.setText(entry.getLatestChatMessage().getContent());
        }
        Glide.with(getContext())
                .load(R.drawable.header_01)
                .asBitmap()
                .into(mImg_face);
    }

    public void updateLatestMsg(String msg) {
        mTv_LatestMsg.setText(msg);
    }

    int sum = 0;

    public void updateMessageCount(int count) {
        sum += count;
        mMessageCount.setText("[" + sum + "]");
        mMessageCount.setVisibility(View.VISIBLE);
    }

    public void restore() {
        sum = 0;
        mMessageCount.setVisibility(View.GONE);
    }

}
