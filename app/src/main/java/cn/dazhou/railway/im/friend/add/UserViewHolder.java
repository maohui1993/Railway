package cn.dazhou.railway.im.friend.add;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/5/24.
 */

public class UserViewHolder extends BaseViewHolder<UserBean> {
    private TextView mUsernameText;
    private ImageView mIconImage;
    public UserViewHolder(ViewGroup parent) {
        super(parent, R.layout.user_item);
        mUsernameText = $(R.id.tx_item_username);
        mIconImage = $(R.id.iv_item_icon);
    }

    @Override
    public void setData(UserBean entry){
        mUsernameText.setText(entry.getUsername());
    }
}
