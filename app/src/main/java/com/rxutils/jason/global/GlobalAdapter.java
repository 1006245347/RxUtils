package com.rxutils.jason.global;

import android.view.View;

import com.rxutils.jason.widget.Gloading;


public class GlobalAdapter implements Gloading.Adapter {

    @Override
    public View getView(Gloading.Holder holder, View convertView, int status) {
        GlobalLoadingStatusView loadingStatusView = null;
        //reuse the old view, if possible
        if (convertView != null && convertView instanceof GlobalLoadingStatusView) {
            loadingStatusView = (GlobalLoadingStatusView) convertView;
        }
        if (loadingStatusView == null) {
            loadingStatusView = new GlobalLoadingStatusView(holder.getContext(), holder.getRetryTask());
        }
        loadingStatusView.setStatus(status);
        Object data = holder.getData();
        //show or not show msg view
        boolean hideMsgView = "hide_loading_status_msg".equals(data);
//        loadingStatusView.setMsgViewVisibility(!hideMsgView);
        loadingStatusView.setMsgViewVisibility(true);
        return loadingStatusView;
    }

}