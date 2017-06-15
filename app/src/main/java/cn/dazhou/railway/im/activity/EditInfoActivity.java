package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;

public class EditInfoActivity extends AppCompatActivity {
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit)
    EditText mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ExtraInfo info = getIntent().getParcelableExtra(Constants.DATA_KEY);
        mEdit.setText(info.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_info_activity, menu);
        return true;
    }

    public static void startItself(Context context, Parcelable value) {
        Intent intent  = new Intent(context, EditInfoActivity.class);
        intent.putExtra(Constants.DATA_KEY, value);
        context.startActivity(intent);
    }
}
