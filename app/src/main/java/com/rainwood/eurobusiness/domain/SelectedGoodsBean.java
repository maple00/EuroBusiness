package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/12
 * @Desc: 选择商品分类 -
 */
public class SelectedGoodsBean implements Serializable {

    private String type;                // 类型
    private boolean choose;             // 是否选中
    private List<PressBean> subList;

    @Override
    public String toString() {
        return "SelectedGoodsBean{" +
                "type='" + type + '\'' +
                ", choose=" + choose +
                ", subList=" + subList +
                '}';
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PressBean> getSubList() {
        return subList;
    }

    public void setSubList(List<PressBean> subList) {
        this.subList = subList;
    }
}
