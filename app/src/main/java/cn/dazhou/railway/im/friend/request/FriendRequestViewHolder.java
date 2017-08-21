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

import cn.dazhou.database.FriendModel;
import cn.dazhou.database.FriendRequestModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.IMUtil;
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
    private Button mAcceptedBtn;
    private Button mRejectedBtn;
    private FriendRequestModel data;

    public FriendRequestViewHolder(ViewGroup parent) {
        super(parent, R.layout.friend_request_item);
        mUsernameText = $(R.id.tx_item_username);
        mIconImage = $(R.id.iv_item_icon);
        mAcceptBtn = $(R.id.bt_accept);
        mRejectBtn = $(R.id.bt_reject);
        mAcceptedBtn = $(R.id.bt_accepted);
        mRejectedBtn = $(R.id.bt_rejected);
    }

    @Override
    public void setData(FriendRequestModel requestModel) {
        mUsernameText.setText(requestModel.getFromJid());
        data = requestModel;
        checkState(data.getState());
    }

    private void checkState(FriendRequestModel.State state) {
        switch (state) {
            case NOT_HANDLE:
                mAcceptBtn.setOnClickListener(onClickListener);
                mRejectBtn.setOnClickListener(onClickListener);
                mAcceptBtn.setVisibility(View.VISIBLE);
                mRejectBtn.setVisibility(View.VISIBLE);
                mAcceptedBtn.setVisibility(View.GONE);
                mRejectedBtn.setVisibility(View.GONE);
                break;
            case ACCEPT:
                mAcceptedBtn.setVisibility(View.VISIBLE);
                mRejectedBtn.setVisibility(View.GONE);
                mAcceptBtn.setVisibility(View.GONE);
                mRejectBtn.setVisibility(View.GONE);
                break;
            case REJECT:
                mAcceptedBtn.setVisibility(View.GONE);
                mRejectedBtn.setVisibility(View.VISIBLE);
                mAcceptBtn.setVisibility(View.GONE);
                mRejectBtn.setVisibility(View.GONE);
                break;
        }
    }

    private void updateState(FriendRequestModel.State state) {
        data.setState(state);
        data.update();
        checkState(state);
    }

    boolean flag;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_accept:
                    updateState(FriendRequestModel.State.ACCEPT);
                    Observable.create(new ObservableOnSubscribe() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                            String jid = StringUtil.getRealJid(mUsernameText.getText().toString());
                            Roster roster = IMLauncher.acceptFriendRequest(jid);
                            RosterEntry entry = roster.getEntry(JidCreate.bareFrom(StringUtil.getRealJid(mUsernameText.getText().toString(), MyApp.gServerIp)));
                            FriendModel friendModel = DataHelper.toFriendModel(entry, MyApp.gCurrentUsername);
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
                                }
                            });
                    break;
                case R.id.bt_reject:
                    updateState(FriendRequestModel.State.REJECT);
                    try {
                        IMLauncher.rejectFriendRequest(mUsernameText.getText().toString());
                    } catch (IMLauncher.IMException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
