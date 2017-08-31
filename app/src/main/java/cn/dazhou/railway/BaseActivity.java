package cn.dazhou.railway;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by lenovo on 2017/8/31.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar2);
        setContentView(R.layout.activity_base);
    }

    protected void addLayoutToBase(int layoutResID){

        LinearLayout llContent = (LinearLayout) findViewById(R.id.base_content); //v_content是在基类布局文件中预定义的layout区域
        //通过LayoutInflater填充基类的layout区域
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = LayoutInflater.from(this).inflate(layoutResID, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llContent.addView(v, params);
    }
}
