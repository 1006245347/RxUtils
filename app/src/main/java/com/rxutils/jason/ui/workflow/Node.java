package com.rxutils.jason.ui.workflow;

/**
 * Created by jason-何伟杰，19/11/26
 * des:
 */
public interface Node {

    //节点id
    int getId();

    //任务完成时触发
    void onCompleted();
}
