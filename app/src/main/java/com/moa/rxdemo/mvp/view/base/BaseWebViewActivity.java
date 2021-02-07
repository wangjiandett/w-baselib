package com.moa.rxdemo.mvp.view.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moa.baselib.base.ui.BaseActivity;
import com.moa.rxdemo.R;

import java.lang.ref.WeakReference;

/**
 * 界面中使用WebView的控件封装
 *
 * @author wangjian
 * @version 1.0.0
 * @create 2016/10/31
 */
public abstract class BaseWebViewActivity extends BaseActivity implements View.OnClickListener {

    protected WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.wv_h5);
        // 能使用JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 优先使用缓存
        // 不是用缓存（mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);）
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new TencentWebViewClient(this));
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(new TencentWebChromeClient());

        // 设置自定义interface
        // mWebView.addJavascriptInterface(new JumpJavaScriptInterface(), JAVA_INTERFACE_NAME);
    }

    protected void showHtmlData(String htmlString){
        // 拼接html目的：去除webView加载数据后的默认边框
        String data =
                "<html>" +
                        "<head><style>img{width:100% !important;}</style></head>" +
                        "<body style='margin:0;padding:0'>"
                        +htmlString+
                        "</body>" +
                        "</html>";
        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);

        // WebView加载web资源和本地资源
        // if (mUrl.startsWith("http") || mUrl.startsWith("file:///")) {
        //     mWebView.loadUrl(mUrl);
        // } else {
        //     // 加载文字资源
        //     mWebView.loadDataWithBaseURL(null, mUrl, "text/html", "UTF-8", null);
        // }
    }

    private static class TencentWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView mWebView, String title) {
            // super.onReceivedTitle(mWebView, title);
            // if (!TextUtils.isEmpty(title)) {
            //     setTitle(title);
            // }
        }
    }

    private static class TencentWebViewClient extends WebViewClient {

        WeakReference<BaseWebViewActivity> reference;

        public TencentWebViewClient(BaseWebViewActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView mWebView, String url) {
            //mWebView.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(reference.get() != null){
                reference.get().showProgress();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.postDelayed(() -> {
                if(reference.get() != null){
                    reference.get().hideProgress();
                }
                view.measure(0, 0);
                int height = view.getMeasuredHeight();
                int width = view.getMeasuredWidth();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = width;
                params.height = height;
                view.setLayoutParams(params);
                // view.invalidate();
                //super.onPageFinished(view, url);
            }, 500);
        }
    }

    /**
     * 覆盖titlebar返回按钮返回WebView的上一页面
     */
    @Override
    public void onClick(View v) {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onClick(v);
    }

    /**
     * 覆盖手机返回按钮，返回WebView的上一页面
     */
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}