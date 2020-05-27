package com.rxutils.jason;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rxutils.jason.common.AppLanguageUtils;
import com.rxutils.jason.common.SetConfig;
import com.rxutils.jason.common.UIhelper;
import com.rxutils.jason.global.GlobalCode;
import com.rxutils.jason.ui.launcher.LauncherAty2;
import com.rxutils.jason.ui.sydialoglib.IDialog;
import com.rxutils.jason.ui.sydialoglib.SYDialog;
import com.rxutils.jason.ui.test.BrowserAty;
import com.rxutils.jason.ui.test.DarkTestAty;
import com.rxutils.jason.ui.test.TestBean;
import com.rxutils.jason.ui.test.TestListAdatper;
import com.rxutils.jason.utils.ApkUtils;
import com.rxutils.jason.utils.GlideUtils;
import com.rxutils.jason.utils.MMKVUtil;
import com.rxutils.jason.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_CHANGE_LANGUAGE = 0x09;
    private ImageView iv_pic;
//    private TextView tv_des;

    private TestListAdatper mTestListAdapter;
    private List<TestBean> mDataList = new ArrayList<>();

    private void initData() {
        mDataList.clear();
        int index = 0;
        mDataList.add(new TestBean(TestBean.LATYOUT_TXT, UIhelper.getString(R.string.str_app_test), index));
        mDataList.add(new TestBean(TestBean.LATYOUT_TXT, "version: " + UIhelper.getVersionName(), ++index));
        mDataList.add(new TestBean(TestBean.LATYOUT_BUTTON, UIhelper.getString(R.string.str_change_language), ++index));
        mDataList.add(new TestBean(TestBean.LATYOUT_BUTTON, UIhelper.getString(R.string.str_go_browser), ++index));
        mDataList.add(new TestBean(TestBean.LATYOUT_BUTTON, UIhelper.getString(R.string.hot_update), ++index));
        mDataList.add(new TestBean(TestBean.LATYOUT_BUTTON, getString(R.string.custom_canvas), ++index));
        mDataList.add(new TestBean(TestBean.LATYOUT_BUTTON, getString(R.string.dev_config), ++index));
        mTestListAdapter.notifyDataSetChanged();
    }

    /**
     * 测试用例
     */
    public void emitEvent(int position) {
        if (position == 2) {
            Intent intent = new Intent(this, DarkTestAty.class);
            startActivityForResult(intent, MainActivity.CODE_CHANGE_LANGUAGE);
        } else if (position == 3) {
            BrowserAty.launchBrowser(this, SetConfig.URL_GREE_VR_PRODUCT);
        } else if (position == 4) {
            ToastUtil.showToast("热更新补丁下载，成功更新效果图？");
            handleFixedHot();
        } else if (position == 5) {
            UIhelper.switch2Aty(this, LauncherAty2.class);
        } else if (position == 6) {
            showDialog();
        }
    }


    //补丁代码测试
    private void handleFixedHot() {
        GlideUtils.loadImageView(this, "newpath", iv_pic);
//        tv_des.setText();
        GlobalCode.printLog("pathch>>>>>handle");
    }

    private void showDialog() {
        new SYDialog.Builder(this)
                .setDialogView(R.layout.dialog_dev_config)
                .setWindowBackgroundP(0.7f)
                .setAnimStyle(0)
                .setBuildChildListener(new IDialog.OnBuildListener() {
                    @Override
                    public void onBuildChildView(final IDialog dialog, View view, int layoutRes) {
                        TextView tv_content = view.findViewById(R.id.tv_content);
                        TextView tv_close = view.findViewById(R.id.tv_close);
                        tv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        StringBuilder sb = new StringBuilder();
                        sb.append("" + new Date() + "\n")
                                .append("Model:" + Build.MODEL + "\n")
                                .append("Version:" + Build.VERSION.RELEASE + "\n")
                                .append("Board:" + Build.BRAND + "\n")
                                .append("Cpu:" + ApkUtils.getCpuName() + "\n")
                                .append("Cpu-max:" + ApkUtils.getMaxCpuFreq() + "\n")
                                .append("Cpu-min:" + ApkUtils.getMinCpuFreq() + "\n")
                                .append("Cpu-cur:" + ApkUtils.getCurCpuFreq() + "\n")
                                .append("Sd-total:" + ApkUtils.getSDTotalSize() + "\n")
                                .append("Sd-avalid:" + ApkUtils.getSDAvailableSize() + "\n")
                                .append("Memory-total:" + ApkUtils.getSysteTotalMemorySize() + "\n")
                                .append("Memory-avalid:" + ApkUtils.getSystemAvaialbeMemorySize());
                        tv_content.setText(sb.toString());
                    }
                }).show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        AppLanguageUtils.changeAppLanguage(this, MMKVUtil.getStr(SetConfig.CODE_LANGUAGE_SET, SetConfig.CODE_LANGUAGE_CHINESE));
        setContentView(R.layout.activity_test);
        iv_pic = findViewById(R.id.iv_pic);
//        tv_des = findViewById(R.id.tv_des);
//        GlideUtils.loadImageViewDynamicGif(this, R.mipmap.ic_dance_cat, iv_pic);
        Glide.with(this).load(R.mipmap.ic_dance_cat).asGif()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<Integer, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GifDrawable> target, boolean isFirstResource) {
//                GlobalCode.printLog("model:" + model + " " + isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Integer model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                GlobalCode.printLog("model:" + model + " " + isFromMemoryCache + " " + isFirstResource);
                        return false;
                    }
                }).into(iv_pic);
        RecyclerView rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new GridLayoutManager(this, 2));
        mTestListAdapter = new TestListAdatper(this, mDataList);
        rv_list.setAdapter(mTestListAdapter);
        initData();
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
    }
}
