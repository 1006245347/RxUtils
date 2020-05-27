package com.rxutils.jason.ui.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rxutils.jason.R;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.ui.launcher.BrowserBean;
import com.rxutils.jason.ui.launcher.LauncherAty2;
import com.rxutils.jason.utils.ActivityStackUtil;
import com.rxutils.jason.widget.X5WebView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BrowserAty2 extends AppCompatActivity {

    private RelativeLayout rl_root;
    private ProgressBar progressBar;
    private X5WebView curWebView;

    private Set<BrowserBean> browserSet = new HashSet<>();
    private HashMap<Integer, X5WebView> viewMap = new HashMap<>();

    public static void launcherBrowser(Context context, int index, String url) {
        Intent intent = new Intent(context, BrowserAty2.class);
        intent.putExtra("index", index);
        intent.putExtra("" + index, url);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_aty2);
        browserSet.clear();
        viewMap.clear();
        ActivityStackUtil.getScreenManager().pushActivity(this);
        final int index = getIntent().getIntExtra("index", -1);
        String url = getIntent().getStringExtra(index + "");
        initView(index, url);
    }

    private void initView(int index, final String url) {
        rl_root = findViewById(R.id.rl_browser_root);
        progressBar = findViewById(R.id.progressbar);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBar.setProgressDrawable(new ClipDrawable(new ColorDrawable(UIhelper.getColor(R.color.colorPrimary)), Gravity.START, ClipDrawable.HORIZONTAL));

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (url.equals(SetConfig.URL_GREE_VR_HOME)) {
                    curWebView.loadUrl(WebJsUtils.createJSHomeVr_closeSide());
                    curWebView.loadUrl(WebJsUtils.createJSHomeVr_closeProduct());
                } else if (url.equals(SetConfig.URL_GREE_VR_PRODUCT)) {
//                    curWebView.loadUrl(WebJsUtils.createJSGree_closeAward());
//                    curWebView.loadUrl(WebJsUtils.creatJSGree_closeAir());
//                    curWebView.loadUrl(WebJsUtils.countJSGreeSence());
//                    curWebView.loadUrl(WebJsUtils.createJSGreeVr("0"));

//                    curWebView.loadUrl(WebJsUtils.randomJSGreeSence());
                } else if (url.equals(SetConfig.URL_GREE_MALL_PHOTO)) {
                    curWebView.loadUrl(WebJsUtils.createJSphoto());

                } else if (url.equals(SetConfig.URL_GREE_GAME)) {

                } else if (url.equals(SetConfig.URL_GREE_MALL)) {

                }
                curWebView.setVisibility(View.GONE);
                Intent intent = new Intent(BrowserAty2.this, LauncherAty2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        Iterator<BrowserBean> it = browserSet.iterator();
        boolean isExsit = false;
        while (it.hasNext()) {
            BrowserBean bean = it.next();
            if (bean.getLink().equals(url)) {
                isExsit = true;
            }
        }
        if (isExsit) {
            curWebView = viewMap.get(index);
            curWebView.setVisibility(View.VISIBLE);
            curWebView.getSettings().setJavaScriptEnabled(true);
        } else {
            createWebView(index, url);
        }
        GlobalCode.printLog("create=" + isExsit + " " + index + " " + url);
    }

    private void createWebView(int index, String url) {
        X5WebView webView = new X5WebView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        //该界面打开更多链接
        webView.setWebViewClient(new WebViewClient() {
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
        webView.setWebChromeClient(new WebChromeClient() {
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
        GlobalCode.printLog("load_url-" + url);
        if (QbSdk.canLoadX5(this) && QbSdk.isTbsCoreInited()) {
            webView.loadUrl(url);
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
        //本地缓存webview
        viewMap.put(index, webView);
        BrowserBean browserBean = new BrowserBean();
        browserBean.setLink(url);
        browserBean.setIndex(index);
        browserSet.add(browserBean);
        curWebView = webView;
        rl_root.addView(webView, lp);
        curWebView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent.getIntExtra("index", -1),
                intent.getStringExtra("" + intent.getIntExtra("index", -1)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curWebView != null) {
            curWebView.onResume();
            curWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (curWebView != null) {
            curWebView.onPause();
            curWebView.getSettings().setLightTouchEnabled(false);
        }

    }

    @Override
    protected void onDestroy() {
        for (int key : viewMap.keySet()) {
            viewMap.get(key).destroy();
        }
        curWebView = null;
        ActivityStackUtil.getScreenManager().popActivity(this);
        super.onDestroy();
    }
}
