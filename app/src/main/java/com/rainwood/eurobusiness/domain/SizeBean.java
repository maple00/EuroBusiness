package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/15
 * @Desc: 规格参数
 */
public class SizeBean implements Serializable {

    private String color;           // 颜色
    private String size;            // 尺码
    private String repertoryBelow;  // 库存下限
    private String wholsePrice;     // 批发价
    private String repertory;       // 库存

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRepertoryBelow() {
        return repertoryBelow;
    }

    public void setRepertoryBelow(String repertoryBelow) {
        this.repertoryBelow = repertoryBelow;
    }

    public String getWholsePrice() {
        return wholsePrice;
    }

    public void setWholsePrice(String wholsePrice) {
        this.wholsePrice = wholsePrice;
    }

    public String getRepertory() {
        return repertory;
    }

    public void setRepertory(String repertory) {
        this.repertory = repertory;
    }
}
