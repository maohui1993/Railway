package cn.dazhou.railway.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.R;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_searchfriend)
    public void searchUser() {
        IMLauncher.searchUserFromServer("mao");
    }
}
