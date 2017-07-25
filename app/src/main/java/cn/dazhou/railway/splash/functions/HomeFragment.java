package cn.dazhou.railway.splash.functions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        mHomeWeb.loadUrl("http://192.168.1.39:8080/test/home.html");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mHomeWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //支持js
        mHomeWeb.getSettings().setJavaScriptEnabled(true);
        mHomeWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        return root;
    }

    @Override
    public void onDestroy() {
        mHomeWeb.clearCache(true);
        super.onDestroy();
    }
}
