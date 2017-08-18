package cn.dazhou.railway.splash.functions.contact;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;

/**
 * Created by hooyee on 2017/5/11.
 */

public class RosterViewHolder extends BaseViewHolder<FriendModel> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private ImageView mCallIv;
    private TextView mTv_LatestMsg;
    private TextView mMessageCount;

    public RosterViewHolder(ViewGroup parent) {
        super(parent, R.layout.roster_item);
        mTv_name = $(R.id.person_name);
//        mTv_LatestMsg = $(R.id.person_sign);
        mImg_face = $(R.id.person_face);
//        mMessageCount = $(R.id.message_count);
        mCallIv = $(R.id.iv_call);

    }

    @Override
    public void setData(final FriendModel entry){
        Log.i("ViewHolder","position"+getDataPosition());
        mTv_name.setText(entry.getName());
        mCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.callPhone((Activity) getContext(), entry.getTel());
            }
        });
        Glide.with(getContext())
                .load(R.drawable.header_01)
                .asBitmap()
                .into(mImg_face);
    }

}
