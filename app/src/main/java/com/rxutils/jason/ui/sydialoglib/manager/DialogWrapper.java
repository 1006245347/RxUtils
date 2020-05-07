package com.rxutils.jason.ui.sydialoglib.manager;

import com.rxutils.jason.ui.sydialoglib.SYDialog;

/**
 * @author by jason-何伟杰，2020/5/7
 * des:管理多个dialog 按照dialog的优先级依次弹出
 */
public class DialogWrapper {

    private SYDialog.Builder dialog;//统一管理dialog的弹出顺序

    public DialogWrapper(SYDialog.Builder dialog) {
        this.dialog = dialog;
    }

    public SYDialog.Builder getDialog() {
        return dialog;
    }

    public void setDialog(SYDialog.Builder dialog) {
        this.dialog = dialog;
    }

}
