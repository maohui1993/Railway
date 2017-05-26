package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.im.adapter.holder.FriendRequestViewHolder;

/**
 * Created by hooyee on 2017/5/26.
 */

public class FriendRequestAdapter  extends RecyclerArrayAdapter<UserBean> {
    public FriendRequestAdapter(Context context, List<UserBean> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendRequestViewHolder(parent);
    }
}
