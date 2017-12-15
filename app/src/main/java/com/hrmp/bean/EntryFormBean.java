package com.hrmp.bean;

import java.io.Serializable;

/**
 * Created by Ly on 2017/5/12.
 */

public class EntryFormBean implements Serializable{
    String name;
    String date;
    String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
