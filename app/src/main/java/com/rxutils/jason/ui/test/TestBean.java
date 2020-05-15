package com.rxutils.jason.ui.test;

public class TestBean {

    public static final int LATYOUT_BUTTON = 0X02;
    public static final int LATYOUT_TXT = 0X03;
    public int layout_type;
    public String title;
    public int id;

    public TestBean(int type, String title, int id) {
        this.layout_type = type;
        this.title = title;
        this.id = id;
    }

    public int getLayout_type() {
        return layout_type;
    }

    public void setLayout_type(int layout_type) {
        this.layout_type = layout_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
