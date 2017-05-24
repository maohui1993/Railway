package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.im.adapter.holder.UserViewHolder;

/**
 * Created by hooyee on 2017/5/24.
 */

public class UserAdapter extends RecyclerArrayAdapter<UserBean> {
    public UserAdapter(Context context, List<UserBean> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(parent);
    }
}
