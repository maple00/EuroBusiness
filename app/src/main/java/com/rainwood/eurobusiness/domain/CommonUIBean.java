package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: title + label 形式的界面
 */
public class CommonUIBean implements Serializable {

    /**
     * 类型
     * 1: 可输入
     * 0： 不可输入
     */
    private int type = 1;            // 类型
    private String title;           // 标题
    private String label;           // hint 提示
    private String showText;        // 显示文字
    /**
     * 箭头是否显示、或者箭头显示类型
     * 0: 箭头不显示
     * 1: 箭头显示
     * 2：箭头显示类型
     */
    private int arrowType;

    /**
     * 是否是必填项
     */
    private boolean fillIn;

    @Override
    public String toString() {
        return "CommonUIBean{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", label='" + label + '\'' +
                ", showText='" + showText + '\'' +
                ", arrowType=" + arrowType +
                ", fillIn=" + fillIn +
                '}';
    }

    public boolean isFillIn() {
        return fillIn;
    }

    public void setFillIn(boolean fillIn) {
        this.fillIn = fillIn;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
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
