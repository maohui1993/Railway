package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.UserAdapter;
import cn.dazhou.railway.im.presenter.AddFriendPresenter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddFriendActivity extends AppCompatActivity {
    @BindView(R.id.edit_search_user)
    EditText mEditText;
    @BindView(R.id.easy_recycler_ver)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private UserAdapter mAdapter;
    private AddFriendPresenter mPresenter;
    private List<UserBean> datas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mPresenter = new AddFriendPresenter(this);
        mAdapter = new UserAdapter(this, datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        mEasyRecyclerView.setAdapter(mAdapter);
        mEasyRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_request_activity, menu);
        return true;
    }

    @OnClick(R.id.bt_search_user)
    public synchronized void  searchUser(View v) {
        final String username = mEditText.getText().toString();

        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                datas = IMLauncher.searchUserFromServer(username);
                e.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (datas == null) {
                            Toast.makeText(AddFriendActivity.this, "未找到该用户", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAdapter.clear();
                        mAdapter.addAll(datas);
                    }
                });



        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StringBuilder sb = new StringBuilder(datas.get(position).getUsername());
                // 拼写jid
                sb.append(Constants.JID_SEPARATOR).append(Constants.SERVER_IP);
                Log.i("TAG", "添加的用户Jid: " + sb.toString());
                IMLauncher.addFriend(sb.toString());
            }
        });
    }

    public static final void startItself(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }
}
