package cn.dazhou.railway.im.friend.add;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import cn.dazhou.im.IMLauncher;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hooyee on 2017/5/24.
 */

public class AddFriendPresenter implements AddFriendContract.Presenter {
    private Context mContext;
    private AddFriendContract.View mAddFriendView;

    public AddFriendPresenter(Context context, AddFriendContract.View view) {
        mContext = context;
        mAddFriendView = view;
        mAddFriendView.setPresenter(this);
    }

    List result;
    @Override
    public void searchUser(final String jid) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                result = IMLauncher.searchUserFromServer(jid);
                e.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (result == null) {
                            Toast.makeText(mContext, "未找到该用户", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAddFriendView.result(result);
                    }
                });

    }
}
