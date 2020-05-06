package com.rxutils.jason.ui.launcher;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rxutils.jason.base.BaseView;
import com.rxutils.jason.ui.videoview.MyJzvdStd;

public class LauncherContract {

    public interface ILauncherView extends BaseView {


        LinearLayout $line_bottom();

        LinearLayout $line_right();

        MyJzvdStd $jzvdStd();
    }

}
