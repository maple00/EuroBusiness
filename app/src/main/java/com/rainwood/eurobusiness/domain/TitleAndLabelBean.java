package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/14 13:38
 * @Desc: 常见实体类
 */
public final class TitleAndLabelBean implements Serializable {

    private String title;
    private String label;
    private int fontSize;
    private int labelColorSize;

    public int getLabelColorSize() {
        return labelColorSize;
    }

    public void setLabelColorSize(int labelColorSize) {
        this.labelColorSize = labelColorSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
