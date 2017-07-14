package cn.dazhou.railway.im.friend.request;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.im.db.FriendRequestModel;

/**
 * Created by hooyee on 2017/5/26.
 */

public class FriendRequestAdapter  extends RecyclerArrayAdapter<FriendRequestModel> {
    public FriendRequestAdapter(Context context, List<FriendRequestModel> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendRequestViewHolder(parent);
    }
}
