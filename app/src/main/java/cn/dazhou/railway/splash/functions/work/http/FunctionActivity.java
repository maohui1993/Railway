package cn.dazhou.railway.splash.functions.work.http;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import cn.dazhou.database.FunctionItemModel;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;

public class FunctionActivity extends AppCompatActivity {
    private static final String EXTRA_DATA = "function";
    private Toolbar mToolbar;
    private WebView mHomeWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        mHomeWeb = (WebView) findViewById(R.id.wv_home);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        FunctionItemModel model = getIntent().getParcelableExtra(EXTRA_DATA);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (model != null) {
            getSupportActionBar().setTitle(model.getFunctionname());
        } else {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHomeWeb.canGoBack()) {
                    mHomeWeb.goBack();
                } else {
                    onBackPressed();
                }
            }
        });

        mHomeWeb.loadUrl("http://192.168.1.252:8089/railway_test/" + model.getUrl());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mHomeWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Log.i("TAG", url);
                if (url.startsWith("tel")) {
                    ActivityUtils.callPhone(FunctionActivity.this, url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });

        //支持js
        mHomeWeb.getSettings().setJavaScriptEnabled(true);
        mHomeWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        mHomeWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Intent intent = new Intent(Intent.ACTION_PICK);//选择图片文件
                intent.setType("image/*");//相片类型
                value = filePathCallback;
                startActivityForResult(Intent.createChooser(intent, "Image Browser"), 200);
                return true;
            }
        });
    }

    ValueCallback value ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 200 || data == null) {
            return;
        }
        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String picturePath = cursor.getString(columnIndex);
        value.onReceiveValue(new Uri[]{Uri.fromFile(new File(picturePath))});
        value = null;
    }

    public static void startItself(Context context, Parcelable param) {
        Intent intent = new Intent(context, FunctionActivity.class);
        intent.putExtra(EXTRA_DATA, param);
        context.startActivity(intent);
    }
}
