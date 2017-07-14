package cn.dazhou.railway.im.friend.request;

import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.DataHelper;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.FriendRequestModel;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.StringUtil;

/**
 * Created by hooyee on 2017/5/26.
 */

public class FriendRequestViewHolder extends BaseViewHolder<FriendRequestModel> {
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
    public void setData(FriendRequestModel requestModel){
        mUsernameText.setText(requestModel.getFromJid());
        mSubmitBtn.setOnClickListener(onClickListener);
        mRejectBtn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_submit :
                    Roster roster = IMLauncher.acceptFriendRequest(mUsernameText.getText().toString());
                    try {
                        RosterEntry entry = roster.getEntry(JidCreate.bareFrom(StringUtil.getRealJid(mUsernameText.getText().toString())));
                        FriendModel friendModel = IMUtil.toFriendModel(entry, MyApp.gCurrentUsername);
                        MyApp.gCurrentUser.getMyFriends().add(friendModel);
                        MyApp.gCurrentUser.save();
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                    IMUtil.sendBroadcast(getContext(), Constants.UPDATE_FROM_SERVER_BROADCAST);
                    FriendRequestModel request = DataHelper.getFriendRequest(mUsernameText.getText().toString(), MyApp.gCurrentUsername);

                    request.setState(FriendRequestModel.State.ACCEPT);
                    request.update();
                    break;
                case R.id.bt_reject :
                    IMLauncher.rejectFriendRequest(mUsernameText.getText().toString());
                    FriendRequestModel request1 = DataHelper.getFriendRequest(mUsernameText.getText().toString(), MyApp.gCurrentUsername);

                    request1.setState(FriendRequestModel.State.REJECT);
                    request1.update();
                    break;
            }
        }
    };
}
