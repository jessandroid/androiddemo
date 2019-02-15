package com.example.dengquan.demo.Choice;

/**
 * 首页可以选择的分类的实体类
 * Created by dengquan on 2018/3/1.
 */

public class KidLiveClassificationBean {
    private String classificationName;
    private boolean isChecked;

    public KidLiveClassificationBean(String classificationName, boolean isChecked) {
        this.classificationName = classificationName;
        this.isChecked = isChecked;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
