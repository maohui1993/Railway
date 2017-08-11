package cn.dazhou.railway.im.friend.message.list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.im.acpect.db.FriendDbApi;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;

public class MessageListFragment extends Fragment implements MessageListContract.View{
    private EasyRecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;
    private View mRootView;

    public MessageListFragment() {
        // Required empty public constructor
    }

    public static MessageListFragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MessageListAdapter(getContext());
        mAdapter.addAll(DataHelper.getMessageList(MyApp.gCurrentUser));
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_message_list, container, false);
            mRecyclerView = (EasyRecyclerView) mRootView.findViewById(R.id.message_content);
            mRecyclerView.setAdapter(mAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(), 0.5f), Util.dip2px(getContext(), 72), 0);
            itemDecoration.setDrawLastItem(false);
            mRecyclerView.addItemDecoration(itemDecoration);
        } else {
            ViewGroup group = (ViewGroup) mRootView.getParent();
            group.removeView(mRootView);
        }
        return mRootView;
    }

    /**
     * 更新当前好友未读消息量
     *
     * @param friendModel
     * @see cn.dazhou.railway.im.service.IMChatService#incomingChatMessageListener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(FriendDbApi friendModel) {
        mAdapter.toTop((FriendModel) friendModel);
    }

    @Override
    public void setPresenter(MessageListContract.Presenter presenter) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
