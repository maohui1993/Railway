package cn.dazhou.railway.im.friend.message.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dazhou.railway.R;

public class SearchChatMessageFragment extends Fragment implements SearchChatMessageContract.View{
    private SearchChatMessageContract.Presenter mPresenter;

    public static SearchChatMessageFragment newInstance() {
        SearchChatMessageFragment fragment = new SearchChatMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_chat_message, container, false);
    }

    @Override
    public void setPresenter(SearchChatMessageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
