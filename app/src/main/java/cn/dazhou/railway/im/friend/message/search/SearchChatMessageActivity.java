package cn.dazhou.railway.im.friend.message.search;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;


public class SearchChatMessageActivity extends BaseActivity {
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private ActionMenuView mActionMenuView;

    private SearchChatMessageFragment mFragment;
    private SearchChatMessagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayoutToBase(R.layout.activity_search_chat_message);

        mFragment = (SearchChatMessageFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            // Create the fragment
            mFragment = SearchChatMessageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
        mPresenter = new SearchChatMessagePresenter(this, mFragment);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mActionMenuView = (ActionMenuView) findViewById(R.id.menu_view);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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

        // 监听搜索框文字变化
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                Cursor cursor = TextUtils.isEmpty(s) ? null : queryData(s);
                Cursor cursor = TextUtils.isEmpty(s) ? null : null;
                // 不要频繁创建适配器，如果适配器已经存在，则只需要更新适配器中的cursor对象即可。
                if (mSearchView.getSuggestionsAdapter() == null) {
                    mSearchView.setSuggestionsAdapter(new SimpleCursorAdapter(SearchChatMessageActivity.this, R.layout.item_text, cursor,
                            new String[]{"content"}, new int[] {R.id.tv_chat}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
                } else {
                    mSearchView.getSuggestionsAdapter().changeCursor(cursor);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SearchChatMessageActivity.class);
        context.startActivity(intent);
    }
}
