package com.rxutils.jason.ui.test;

import com.rxutils.jason.global.GlobalCode;

/**
 * @author by jason-何伟杰，2020/5/20
 * des:向web注入js -一般是在页面加载完成前注入功能代码，但是我们只是添加模拟点击可以不需要
 */
public class WebJsUtils {

    public static String createJSphoto() {
        StringBuilder sb = new StringBuilder();

        sb.append("javascript:")
                .append("try{ ")
                .append("document.querySelector(\".detail-dialog .close\").click()")
                .append(" }catch(e){}");
        return sb.toString();
    }

    //获取所有的场景模式数量
    public static String countJSGreeSence() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ document.querySelectorAll(\"div[currentitem]\").length }catch(e){}");
        return sb.toString();
    }

    //try{var items= document.querySelectorAll("div[currentitem]");
    //console.info(items.length)
    //if(items.length>0){
    //    items[parseInt(Math.random()*items.length)].click()
    //}}catch(e){}
    //随机定位到某个场景
    public static String randomJSGreeSence() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ ")
                .append("var items= document.querySelectorAll(\"div[currentitem]\");")
                .append("alert(item.length);")
                .append("if(items.length>0){ ")
                .append("items[parseInt(Math.random()*items.length)].click()}")
                .append(" }catch(e){}");

       /* sb.append("javascript:")
                .append("try{ ")
                .append("var items= document.querySelectorAll(\".Panos_item_1mjB5K\");")
                .append("alert(items.length);")
                .append("if(items.length>0){ ")
                .append("items[parseInt(Math.random()*items.length)].click()}")
                .append(" }catch(e){}");*/
        return sb.toString();
    }

    public static String alert() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("alert(\"ll\")");
        return sb.toString();
    }

    //定位到某个情景
    public static String createJSGreeVr(String index) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ document.querySelectorAll(\"div[currentitem]\")")
                .append("[" + index + "]")
                .append(".click() }catch(e){}");
        return sb.toString();
    }

    //删除空调/其他产品弹窗
    public static String creatJSGree_closeAir() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ ")
                .append("document.querySelector(\".UrlModal_close_3EymD8\").click()")
                .append(" }catch(e){}");
        return sb.toString();
    }

    //删除获奖弹窗
    public static String createJSGree_closeAward() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ ")
                .append("document.querySelector(\".HotspotModal_close_7afvjD\").click()")
                .append(" }catch(e){}");
        return sb.toString();
    }

    public static String createJSHomeVr_closeProduct() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ ")
                .append("document.querySelector(\".el-dialog_wrapper .el-dialog_close\").click()")
                .append(" }catch(e){}");
        return sb.toString();
    }

    //循环关闭所有该样式下的所有弹窗
    public static String createJSHomeVr_closeSide() {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:")
                .append("try{ ")
                .append("document.querySelectorAll(\".el-icon-close\").forEach(ele=>{ele.click()})")
                .append(" }catch(e){}");
        return sb.toString();
    }


}
