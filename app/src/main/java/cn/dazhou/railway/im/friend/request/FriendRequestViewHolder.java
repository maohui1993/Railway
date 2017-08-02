package cn.dazhou.railway.im.friend.request;

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

import cn.dazhou.database.DataHelper;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.FriendRequestModel;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hooyee on 2017/5/26.
 */

public class FriendRequestViewHolder extends BaseViewHolder<FriendRequestModel> {
    private TextView mUsernameText;
    private ImageView mIconImage;
    private Button mAcceptBtn;
    private Button mRejectBtn;

    public FriendRequestViewHolder(ViewGroup parent) {
        super(parent, R.layout.friend_request_item);
        mUsernameText = $(R.id.tx_item_username);
        mIconImage = $(R.id.iv_item_icon);
        mAcceptBtn = $(R.id.bt_accept);
        mRejectBtn = $(R.id.bt_reject);
    }

    @Override
    public void setData(FriendRequestModel requestModel) {
        mUsernameText.setText(requestModel.getFromJid());
        mAcceptBtn.setOnClickListener(onClickListener);
        mRejectBtn.setOnClickListener(onClickListener);
    }

    boolean flag;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_accept:
                    Observable.create(new ObservableOnSubscribe() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                            Roster roster = IMLauncher.acceptFriendRequest(mUsernameText.getText().toString());
                            RosterEntry entry = roster.getEntry(JidCreate.bareFrom(StringUtil.getRealJid(mUsernameText.getText().toString(), MyApp.gServerIp)));
                            FriendModel friendModel = IMUtil.toFriendModel(entry, MyApp.gCurrentUsername);
                            flag = friendModel.exists();
                            if (!flag) {
                                friendModel.save();
                                e.onNext(friendModel);
                            }
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(@NonNull Object o) throws Exception {
//                                    MyApp.gCurrentUser.getMyFriends().add((FriendModel) o);
                                    IMUtil.sendBroadcast(getContext(), Constants.UPDATE_FROM_SERVER_BROADCAST);
                                    FriendRequestModel request = DataHelper.getFriendRequest(mUsernameText.getText().toString(), MyApp.gCurrentUsername);

                                    request.setState(FriendRequestModel.State.ACCEPT);
                                    request.update();
                                }
                            });
                    break;
                case R.id.bt_reject:
                    try {
                        IMLauncher.rejectFriendRequest(mUsernameText.getText().toString());
                        FriendRequestModel request1 = DataHelper.getFriendRequest(mUsernameText.getText().toString(), MyApp.gCurrentUsername);

                        request1.setState(FriendRequestModel.State.REJECT);
                        request1.update();
                    } catch (IMLauncher.IMException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
