package com.rxutils.jason.ui.sydialoglib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
/**
 * @author by jason-何伟杰，2020/5/7
 * des:
 */
public class AnimTextView extends androidx.appcompat.widget.AppCompatTextView {
//    private static final String showText = "努力加载中";
    private static final String showText = "loading...";

    private int number;
    private StringBuilder builder = new StringBuilder();

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            builder.delete(0, builder.length());
            builder.append(showText);
            int position = number % 4;
            if (number == Integer.MAX_VALUE) {
                //避免越界
                number = 0;
            }
            number++;
            switch (position) {
                case 0:
                    break;
                case 1:
                    builder.append(".");
                    break;
                case 2:
                    builder.append("..");
                    break;
                case 3:
                    builder.append("...");
                    break;
            }
            setText(builder.toString());
            handler.sendEmptyMessageDelayed(0, 500);
        }
    };

    public AnimTextView(Context context) {
        this(context, null);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开始动画
     */
    public void startAnim() {
        handler.sendEmptyMessage(0);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //去掉队列中的消息  防止内存泄漏
        handler.removeCallbacksAndMessages(null);
    }
}
