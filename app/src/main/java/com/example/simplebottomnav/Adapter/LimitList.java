package com.example.simplebottomnav.Adapter;

import java.util.ArrayList;
import java.util.List;

public class LimitList {
    private List<String> list = new ArrayList<String>();
    private int LENGTH = 10;

    public LimitList() {
    }

    public LimitList(int length) {
        this.LENGTH = length;
    }

    public int getLength() {
        return list.size();
    }

    public void add(String s) {
        if ( getLength() < 10 ) {
            list.add(s);
        } else {
            list.remove(0);
            list.add(s);
        }
    }

    public String getIndex(int i) {
        return list.get(LENGTH - i);
    }
}
