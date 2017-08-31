package cn.dazhou.railway.im.friend.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;

public class EditInfoActivity extends BaseActivity {
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
        addLayoutToBase(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        info = getIntent().getParcelableExtra(Constants.DATA_KEY);
        if (info.getTitle().equals(TITLE_TEL)) {
            mEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            mEdit.setMaxLines(1);
            mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        } else if (info.getTitle().equals(TITLE_NAME)) {
            mEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            mEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        mEdit.setText(info.getName());
        mToolbar.setTitle(info.getTitle());
        setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (info.getTitle()) {
                    case TITLE_NAME :
                        info.setName(mEdit.getText().toString());
                        MyApp.gCurrentUser.setNickName(info.getName());
                        break;
                    case TITLE_ADDR :
                        info.setAddr(mEdit.getText().toString());
                        break;
                    case TITLE_TEL :
                        info.setTel(mEdit.getText().toString());
                        MyApp.gCurrentUser.setTel(info.getTel());
                        break;
                    default:
                        break;
                }
                try {
                    IMLauncher.saveVCard(info);
                    MyApp.gCurrentUser.save();
                    finish();
                } catch (Exception e) {
                    Log.i("TAG", "保存个人信息失败");
                    Log.i("TAG", "异常信息：" + e.getMessage());
                    Toast.makeText(EditInfoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
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
