package cn.dazhou.railway.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.R;

public class AddFriendActivity extends AppCompatActivity {
    @BindView(R.id.edit_search_user)
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_search_user)
    public void searchUser() {
        String username = mEditText.getText().toString();
        IMLauncher.searchUserFromServer(username);
    }
}
