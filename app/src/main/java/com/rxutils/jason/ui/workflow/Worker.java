package com.rxutils.jason.ui.workflow;

/**
 * Created by jason-何伟杰，19/11/26
 * des:
 */
public interface Worker {


    /**执行任务
     * @param curNote   当前节点
     */
    void doWork(Node curNote);
}
