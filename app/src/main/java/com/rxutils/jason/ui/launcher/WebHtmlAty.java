package com.rxutils.jason.ui.launcher;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.rxutils.jason.R;
import com.rxutils.jason.base.BaseActivity;
import com.rxutils.jason.base.BasePresenter;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.utils.ToastUtil;
import com.rxutils.jason.widget.TinyWebView;

public class WebHtmlAty extends BaseActivity {

    private TinyWebView webView;

    public static void launchWebAty(Context context, String url) {
        Intent intent = new Intent(context, WebHtmlAty.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @Override
    protected void initAty() {
//        String value = jsonObject.getJSONObject("data").getString("value");
//        webView.loadDataWithBaseURL(null, getHtmlData(value), "text/html", "utf-8", null);
        clickFun(R.id.btn_back, new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
        webView = findViewById(R.id.webview);
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
//            webView.loadDataWithBaseURL(url,"","text/html","utf-8",null);
            GlobalCode.printLog("url="+url);
            webView.loadUrl(url);
        } else {
            ToastUtil.showToast("no link!");
        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    protected void onDestroy() {
        if (null != webView) {
//            ((ViewGroup)webView.getParent().getParent()).removeView(webView);
//            webView.release();
        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_html_aty;
    }

    @Override
    protected BasePresenter onCreatePresenter() {
        return null;
    }
}
