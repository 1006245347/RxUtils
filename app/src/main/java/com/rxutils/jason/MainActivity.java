package com.rxutils.jason;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.ui.test.BrowserAty;
import com.rxutils.jason.ui.test.DarkTestAty;

public class MainActivity extends AppCompatActivity {

    private final int CODE_CHANGE_LANGUAGE = 0x09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testChangeLanguage();
            }
        });
        findViewById(R.id.btn_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBrowserAty();
            }
        });
    }

    private void testChangeLanguage() {
        Intent intent = new Intent(this, DarkTestAty.class);
        startActivityForResult(intent, CODE_CHANGE_LANGUAGE);
    }

    private void testBrowserAty() {
        UIhelper.switch2Aty(this, BrowserAty.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CHANGE_LANGUAGE && resultCode == SetConfig.CODE_COMMON_BACK) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalCode.printLog("cur_lang1:"+getResources().getConfiguration().getLocales());
    }
}
