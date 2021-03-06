package com.rxutils.jason.ui.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rxutils.jason.MainActivity;
import com.rxutils.jason.R;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.utils.ActivityStackUtil;
import com.rxutils.jason.widget.X5WebView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.util.ArrayList;

public class BrowserAty extends AppCompatActivity {

    private X5WebView webView;
    private ProgressBar progressBar;
    String webUrl;

    public static void launchBrowser(Context context, String url) {
//        Intent intent = new Intent(context, BrowserAty.class);
//        intent.putExtra("url", url);
//        context.startActivity(intent);
        launchBrowser2(context, url);
    }

    public static void launchBrowser2(Context context, String url) {
        Intent intent = new Intent(context, BrowserAty.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("url", url);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_aty);
        init();
    }

    private void init() {
        ActivityStackUtil.getScreenManager().pushActivity(this);
        webUrl = getIntent().getStringExtra("url");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setProgressDrawable(new ClipDrawable(new ColorDrawable(UIhelper.getColor(R.color.colorPrimary)), Gravity.START, ClipDrawable.HORIZONTAL));

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                while (webView.canGoBack()) {
//                    webView.goBack();
//                }
                if (webUrl.equals(SetConfig.URL_GREE_VR_HOME)) {
                    webView.loadUrl(WebJsUtils.createJSHomeVr_closeSide());
                    webView.loadUrl(WebJsUtils.createJSHomeVr_closeProduct());
                } else if (webUrl.equals(SetConfig.URL_GREE_VR_PRODUCT)) {
                    webView.loadUrl(WebJsUtils.createJSGree_closeAward());
                    webView.loadUrl(WebJsUtils.creatJSGree_closeAir());
//                    webView.loadUrl(WebJsUtils.createJSGreeVr());
                    webView.loadUrl(WebJsUtils.randomJSGreeSence());
//                    webView.loadUrl(WebJsUtils.alert());
                } else if (webUrl.equals(SetConfig.URL_GREE_MALL_PHOTO)) {
                    webView.loadUrl(WebJsUtils.createJSphoto());

                } else if (webUrl.equals(SetConfig.URL_GREE_GAME)) {

                } else if (webUrl.equals(SetConfig.URL_GREE_MALL)) {

                }
                Intent intent = new Intent(BrowserAty.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        this.webView = findViewById(R.id.webview);
        //该界面打开更多链接
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                //重定向链接
                GlobalCode.printLog("direct>>" + s);
                GlobalCode.printLog("dir-" + super.shouldOverrideUrlLoading(webView, s));
                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                GlobalCode.printLog("web_finish=" + s);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                GlobalCode.printLog("web_start=" + s);
            }
        });

        this.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(webView, newProgress); //监听网页的加载速度
            }
        });

        this.webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                new AlertDialog.Builder(BrowserAty.this)
                        .setTitle("")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(BrowserAty.this, "fake msg: i'll download", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(BrowserAty.this, "fake msg: refuse download", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Toast.makeText(BrowserAty.this, "refuse download", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setSupportZoom(true);    //支持缩放
//        webSetting.setBuiltInZoomControls(true);//设置内置的缩放控件，false不可缩放
        webSetting.setUseWideViewPort(true);    //
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
//         webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//         webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        String debugUrl = "http://debugtbs.qq.com";
        GlobalCode.printLog("load_url-" + getIntent().getStringExtra("url"));
        if (QbSdk.canLoadX5(this) && QbSdk.isTbsCoreInited()) {
            webView.loadUrl(getIntent().getStringExtra("url"));
        } else {
            webView.loadUrl(debugUrl);
        }
        GlobalCode.printLog("x5suc>" + webView.getX5WebViewExtension());
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        //去除qq浏览器
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ArrayList<View> outView = new ArrayList<View>();
                getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                int size = outView.size();
                if (outView != null && outView.size() > 0) {
                    outView.get(0).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            direct2Home();
            return true;
        } else {
            return false;
        }
    }

    private void direct2Home() {
        if (null != webView && webView.canGoBack()) {
            WebBackForwardList webBackForwardList = webView.copyBackForwardList();
            if (webBackForwardList.getCurrentIndex() > 0) {
                String historyUrl = webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();
                GlobalCode.printLog("history=" + historyUrl);
                if (!historyUrl.equals("url")) {
                    webView.goBack();
                }
            }
        }
    }

    private boolean back() {
        if (null != webView && webView.canGoBack()) {
            WebBackForwardList webBackForwardList = webView.copyBackForwardList();
            GlobalCode.printLog("web=" + webBackForwardList.getCurrentIndex() + " \n" +
                    webBackForwardList.getCurrentItem().getUrl());
            if (webBackForwardList.getCurrentIndex() == 4) {
                webView.loadUrl(webBackForwardList.getItemAtIndex(0).getUrl());
                return true;
            }
            webView.goBack();
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        back();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalCode.printLog("aty_onResume>>" + this);
//        GlobalCode.printLog("stack>>"+ActivityStackUtil.getScreenManager().getCurAty());
        webView.onResume();
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出界面暂停 webView的活跃，并且关闭 JS 支持
        webView.onPause();
        webView.getSettings().setLightTouchEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        ActivityStackUtil.getScreenManager().popActivity(this);
        super.onDestroy();
    }
}
