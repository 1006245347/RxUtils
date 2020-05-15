package com.rxutils.jason.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rxutils.jason.R;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;

/**
 * Created by jason on 17/10/9.
 * 支持App内部显示资源、支持JavaScript、支持显示进度条的WebView
 */

public class TinyWebView extends WebView {
    private ProgressBar progressbar;

    public TinyWebView(Context context) {
        super(context);
    }

    public TinyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TinyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
        openJavaScript();
        setWebViewClient(new WebClient());
        setWebChromeClient(new WebChromeClient());
    }

    private void initProgressBar(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 3), 0, 0));
        //改变progressbar默认进度条的颜色（深红色）为Color.GREEN
        progressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(UIhelper.getColor(R.color.colorPrimary)), Gravity.START, ClipDrawable.HORIZONTAL));
        addView(progressbar);
    }

    /**
     * 方法描述：启用支持javascript
     */
    private void openJavaScript() {

        WebSettings webSettings = getSettings();

        //解决图文混排html 数据
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(false);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 类描述：显示WebView加载的进度情况
     */
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);

                progressbar.setProgress(newProgress);
                GlobalCode.printLog("web_progress=" + newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
            this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //handler.cancel();证书不合理，默认的处理方式，webview变成空白页，
//            super.onReceivedSslError(view, handler, error);
            // 改为接收证书
            handler.proceed();
            //其他
        }
    }

    public void release() {
        this.clearHistory();
        this.loadUrl("about:blank");
        this.stopLoading();
        this.setWebViewClient(null);
        this.setWebChromeClient(null);
        this.destroy();
    }

}
