package com.example.dengquan.demo.Choice;

/**
 * Created by dengquan on 2018/3/1.
 */

public class KidLiveSortBean {

    public KidLiveSortBean(String sortName, int sortType) {
        this.sortName = sortName;
        this.sortType = sortType;
    }

    private String sortName;
    private int sortType;

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }
}
