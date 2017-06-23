package cn.dazhou.railway.im.editinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;

public class EditInfoActivity extends AppCompatActivity {
    public static final String TITLE_NAME = "修改名称";
    public static final String TITLE_TEL = "修改手机";
    public static final String TITLE_ADDR = "修改地址";
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit)
    EditText mEdit;

    private ExtraInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        info = getIntent().getParcelableExtra(Constants.DATA_KEY);
        mEdit.setText(info.getName());
        mToolbar.setTitle(info.getTitle());
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (info.getTitle()) {
                    case TITLE_NAME :
                        info.setName(mEdit.getText().toString());
                        break;
                    case TITLE_ADDR :
                        info.setAddr(mEdit.getText().toString());
                        break;
                    case TITLE_TEL :
                        info.setTel(mEdit.getText().toString());
                        break;
                    default:
                        break;
                }
                try {
                    IMLauncher.saveVCard(info);
                } catch (Exception e) {
                    Log.i("TAG", "保存个人信息失败");
                    Log.i("TAG", "异常信息：" + e.getMessage());
                    e.printStackTrace();
                }
                return false;
            }
        });
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
