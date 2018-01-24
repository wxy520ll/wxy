package cn.net.xinyi.xmjt.activity.Collection.SafetyManage.OneCamera;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseActivity2;

public class MachRoomStudyAty extends BaseActivity2 {

    private ProgressBar progressBar;
    private TextView tv_pro;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_mach_room_study);
        webview = (WebView) findViewById(R.id.web_study);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_pro = (TextView) findViewById(R.id.tv_pro);
        String url = "http://xinyi-tech.h5release.com/h5/safetymanage-study.html";
        //String url = "http://xinyi-tech.h5-legend.com/h5/safetymanage-study.html";
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSetting = webview.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSetting.setJavaScriptEnabled(true);
        //自适应屏幕
        // webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        // 设置可以支持缩放
        webSetting.setSupportZoom(true);
        // 设置出现缩放工具
        webSetting.setBuiltInZoomControls(true);
        //无限缩放
        webSetting.setUseWideViewPort(true);
        webSetting.setDomStorageEnabled(true);
        // 加载需要显示的网页
        webview.loadUrl(url);
        // 设置Web视图
        webview.setWebViewClient(new MyWebViewClient());

        //设置当前进度监听，onProgressChanged方法的newProgress参数就是当前网页的加载进度
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE); //加载完成后隐藏progressBar
                    tv_pro.setVisibility(View.GONE); //加载完成后隐藏TextView
                }else{
                    //如果有新网页打开判断progressbar是否在隐藏，如果是则显示
                    if(View.GONE == progressBar.getVisibility()){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.clearCache(true);
    }

    @Override
    public String getAtyTitle () {
        return getString(R.string.safety_study);
    }

    @Override
    public boolean enableBackUpBtn () {
        return true;
    }
}
