package com.example.simplebottomnav.bean;

public class Tag {
    private int t_id;
    private String tagName;

    public Tag(int t_id, String tagName) {
        this.t_id = t_id;
        this.tagName = tagName;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
