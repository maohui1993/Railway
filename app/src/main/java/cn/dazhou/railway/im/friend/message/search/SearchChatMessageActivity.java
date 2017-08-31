package cn.dazhou.railway.im.friend.message.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;

import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.R;

import static android.widget.LinearLayout.SHOW_DIVIDER_BEGINNING;

public class SearchChatMessageActivity extends BaseActivity {
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private ActionMenuView mActionMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayoutToBase(R.layout.activity_search_chat_message);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mActionMenuView = (ActionMenuView) findViewById(R.id.menu_view);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_search , mActionMenuView.getMenu());
        menu = mActionMenuView.getMenu();
        MenuItem searchItem = menu.findItem(R.id.menu_search_view);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

//        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
//        mSearchView.setIconified(true);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();
        mSearchView.setQueryHint("搜索");
        mSearchView.setShowDividers(LinearLayoutCompat.SHOW_DIVIDER_MIDDLE);
//        //设置是否显示搜索框展开时的提交按钮
//        mSearchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SearchChatMessageActivity.class);
        context.startActivity(intent);
    }
}
