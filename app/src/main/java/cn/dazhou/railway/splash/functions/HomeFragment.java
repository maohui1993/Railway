package cn.dazhou.railway.splash.functions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import cn.dazhou.railway.R;

public class HomeFragment extends BaseFragment {
    private WebView mHomeWeb;

    public static HomeFragment newInstance(boolean param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeWeb = (WebView) root.findViewById(R.id.wv_home);
//        mHomeWeb.loadUrl("http://192.168.1.185:8088/html/index.html");
        mHomeWeb.loadUrl("http://192.168.1.4:8088");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开

        mHomeWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("tel")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{
                                        Manifest.permission.CALL_PHONE,},
                                1);
                    } else {
                        startActivity(intent);
                    }
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
        return root;
    }

    ValueCallback value ;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 200 || data == null) {
            return;
        }
        Cursor cursor = getContext().getContentResolver().query(data.getData(), null, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String picturePath = cursor.getString(columnIndex);
        value.onReceiveValue(new Uri[]{Uri.fromFile(new File(picturePath))});
        value = null;
    }

    @Override
    public void onDestroy() {
        mHomeWeb.clearCache(true);
        super.onDestroy();
    }
}
