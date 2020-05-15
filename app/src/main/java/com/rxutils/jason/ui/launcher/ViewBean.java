package com.rxutils.jason.ui.launcher;

/**
 * @author by jason-何伟杰，2020/5/13
 * des:自定义组件
 */
public class ViewBean {
    public static final int ViewType_Button = 0x11;
    public static final int ViewType_Image = 0x12;
    public static final int ViewType_TextView = 0x13;
    public static final int ViewType_VideoView = 0x14;

    private int viewType;
    private int index;
    private float startX;
    private float endY;
    private float width;
    private float height;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private int gravity;
    private String value;
    private float fontSize;
    private int fontColor;
    private int backgroundRes;
    private String backgroundUrl;
    private Runnable runnable;
    private String link;

    //Button  TextView
    public ViewBean(int viewType, int index, float startX, float endY, float width,
                    float height, int marginLeft, int marginRight, int marginTop, int marginBottom,
                    int paddingLeft, int paddingRight, int paddingTop, int paddingBottom,
                    int gravity, String value, float fontSize, int fontColor, int backgroundRes, String backgroundUrl,
                    String link) {
        this.viewType = viewType;
        this.index = index;
        this.startX = startX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.gravity = gravity;
        this.value = value;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundRes = backgroundRes;
        this.backgroundUrl = backgroundUrl;
        this.link = link;
    }

    //ImageView
    public ViewBean(int viewType, int index, float startX, float endY, float width,
                    float height, int marginLeft, int marginRight, int marginTop, int marginBottom,
                    int paddingLeft, int paddingRight, int paddingTop, int paddingBottom,
                    int gravity, int backgroundRes, String backgroundUrl,
                    String link) {
        this.viewType = viewType;
        this.index = index;
        this.startX = startX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.gravity = gravity;
        this.backgroundRes = backgroundRes;
        this.backgroundUrl = backgroundUrl;
        this.link = link;
    }

    //videoView
    public ViewBean(int viewType, int index, float startX, float endY, float width,
                    float height, int marginLeft, int marginRight, int marginTop, int marginBottom,
                    int paddingLeft, int paddingRight, int paddingTop, int paddingBottom,
                    int gravity, int backgroundRes, String backgroundUrl,
                    String value, String link) {
        this.viewType = viewType;
        this.index = index;
        this.startX = startX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.gravity = gravity;
        this.backgroundRes = backgroundRes;
        this.backgroundUrl = backgroundUrl;
        this.value = value;//放titile
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public static int getViewType_Button() {
        return ViewType_Button;
    }

    public static int getViewType_Image() {
        return ViewType_Image;
    }

    public static int getViewType_TextView() {
        return ViewType_TextView;
    }

    public static int getViewType_VideoView() {
        return ViewType_VideoView;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getBackgroundRes() {
        return backgroundRes;
    }

    public void setBackgroundRes(int backgroundRes) {
        this.backgroundRes = backgroundRes;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
