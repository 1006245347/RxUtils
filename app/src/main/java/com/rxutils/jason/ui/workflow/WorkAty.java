package com.rxutils.jason.ui.workflow;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.rxutils.jason.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

/**
 * @author by jason-何伟杰，2020-05-06
 * des:   每个节点只关心自己的工作，每个节点按顺序执行
 */
public class WorkAty extends AppCompatActivity {
    private static final int REQUEST_CODE_H5 = 1;

    /**
     * 初次广告弹框
     */
    private static final int NODE_FIRST_AD = 10;

    /**
     * 初次进入h5页
     */
    private static final int NODE_CHECK_H5 = 30;

    /**
     * 初次进入的注册协议
     */
    private static final int NODE_REGISTER_AGREEMENT = 20;  //草，sparseArray内部是数组实现，按key的大小升序排列，key值大小决定执行顺序

    private WorkFlow workFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_work_aty);
        startWorkFlow();
//        startWorkResert();
    }

    private void showNotify() {
        Intent intent=null;
//        intent= new Intent(getBaseContext(), TestH5Aty.class);
        int randomnum = (int) (Math.random() * 100);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //判断是否是8.0上设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(randomnum + "", "type", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("type");
            notificationChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId(randomnum + "");
        }
        notificationBuilder.setContentTitle("消息提醒")
                .setContentText("商城有新的訂單")
                .setTicker("盡快處理")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_ALL);
//                .setSmallIcon(R.mipmap.push_small);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        long[] vibrates = {0, 300, 300, 300};
        notification.vibrate = vibrates;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(randomnum, notification);
    }

    public void createNotificationChannel(Context context, int notifactionId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = String.valueOf(notifactionId);
            CharSequence channelName = "channelName";
            String channelDescription = "channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            // 设置描述 最长30字符
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
//            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi), null);
            notificationChannel.setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null);


            notificationManager.createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(context);
            notification.setChannelId(channelId);
            notification.setContentTitle("活动");
            notification.setContentText("您有一项新活动");
//            notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi));
            notification.setSmallIcon(R.mipmap.ic_launcher_round).build();

        } else {
            notification = new Notification.Builder(context);
            notification.setAutoCancel(true)
                    .setContentText("自定义推送声音111")
                    .setContentTitle("111")
//                    .setSmallIcon(R.mipmap.push_small)
                    .setDefaults(Notification.DEFAULT_ALL);
//            notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.order_tishi));
        }
        notificationManager.notify(1024, notification.getNotification());

    }

    private void startWorkFlow() {
        workFlow = new WorkFlow.Builder()
                .withNode(getFirstNode())
//                .withNode(getShowRegisterAgreementNode())
//                .withNode(getShowH5Node())
                .create();
        workFlow.start();

//        workFlow.dispose(); //中断工作流
    }

    private void startWorkResert() {
        workFlow = new WorkFlow.Builder()
                .withNode(Node1())
                .create();
        workFlow.addNode(Node2());
        workFlow.addNode(Node3());
        workFlow.start();
    }

    private WorkNode Node1() {
        return WorkNode.build(2, new Worker() {
            @Override
            public void doWork(Node curNote) {
                System.out.println("dowork>>1");
                curNote.onCompleted();
            }
        });
    }

    private WorkNode Node2() {
        return WorkNode.build(3, new Worker() {
            @Override
            public void doWork(Node curNote) {
                System.out.println("dowork>>2");
//                curNote.onCompleted();
            }
        });
    }

    private WorkNode Node3() {
        return WorkNode.build(4, new Worker() {
            @Override
            public void doWork(Node curNote) {
                System.out.println("dowork>>3");
                curNote.onCompleted();
            }
        });
    }

    private WorkNode getFirstNode() {
        return WorkNode.build(NODE_FIRST_AD, new Worker() {
            @Override
            public void doWork(final Node curNote) {
                Utils.fakeRequest("url1", new HttpCallBack() {          //延迟500ms执行，
                    @Override
                    public void onOk() {
                        System.out.println("dowork>>>1");
                        new AlertDialog.Builder(WorkAty.this)
                                .setTitle("这是一条有态度的广告!")
                                .setPositiveButton("我看完了", null)
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        //仅仅只需关心自己是否完成，下一个节点会自动执行
//                                        showNotify();
                                        createNotificationChannel(WorkAty.this, 3);
                                        curNote.onCompleted();
                                    }
                                }).create().show();
                    }

                    @Override
                    public void onErr() {
                        //仅仅只需关心自己是否完成，下一个节点会自动执行
                        curNote.onCompleted();
                    }
                });
            }
        });
    }

    private WorkNode getShowRegisterAgreementNode() {
        return WorkNode.build(NODE_REGISTER_AGREEMENT, new Worker() {
            @Override
            public void doWork(final Node curNote) {
                System.out.println("dowork>>>>2");
                Utils.fakeRequest("url2", new HttpCallBack() {
                    @Override
                    public void onOk() {
                        new AlertDialog.Builder(WorkAty.this)
                                .setTitle("注册协议")
                                .setPositiveButton("我看完了", null)
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        curNote.onCompleted();
                                    }
                                })
                                .create().show();
                    }

                    @Override
                    public void onErr() {
                        curNote.onCompleted();
                    }
                });
            }
        });
    }

    private WorkNode getShowH5Node() {
        return WorkNode.build(NODE_CHECK_H5, new Worker() {
            @Override
            public void doWork(final Node curNote) {
                System.out.println("dowork>>>3");
                Utils.fakeRequest("url3", new HttpCallBack() {
                    @Override
                    public void onOk() {
//                        startActivityForResult(new Intent(WorkAty.this, TestH5Aty.class), REQUEST_CODE_H5);
//                        curNote.onCompleted();
                    }

                    @Override
                    public void onErr() {
                        curNote.onCompleted();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_H5:
                System.out.println("work_continuework()");
                //节点以外希望让流程继续执行下去
                workFlow.continueWork();    //把工作权交给下一个节点
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        workFlow.dispose();
    }
}
