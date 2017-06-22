package cn.dazhou.railway.im.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.IMUtil;

/**
 * Created by hooyee on 2017/5/26.
 */

public class FriendRequestViewHolder extends BaseViewHolder<UserBean> {
    private TextView mUsernameText;
    private ImageView mIconImage;
    private Button mSubmitBtn;
    private Button mRejectBtn;

    public FriendRequestViewHolder(ViewGroup parent) {
        super(parent, R.layout.friend_request_item);
        mUsernameText = $(R.id.tx_item_username);
        mIconImage = $(R.id.iv_item_icon);
        mSubmitBtn = $(R.id.bt_submit);
        mRejectBtn = $(R.id.bt_reject);
    }

    @Override
    public void setData(UserBean entry){
        mUsernameText.setText(entry.getUsername());
        mSubmitBtn.setOnClickListener(onClickListener);
        mRejectBtn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_submit :
                    IMLauncher.acceptFriendRequest(mUsernameText.getText().toString());
                    IMUtil.sendBroadcast(getContext(), Constants.UPDATE_FROM_SERVER_BROADCAST);
                    break;
                case R.id.bt_reject :
                    IMLauncher.rejectFriendRequest(mUsernameText.getText().toString());
                    break;
            }
        }
    };
}
