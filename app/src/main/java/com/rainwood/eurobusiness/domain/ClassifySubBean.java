package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:57
 * @Desc: 批发商  ---- 商品管理(商品分类) -- 子级分类
 */
public class ClassifySubBean implements Serializable {

    private String goodsTypeTwoId;              // 分类id
    private String name;                // 分类名称
    private String state;                       // 分类状态 -- 是否停用
    private boolean choose;                     // 选中

    @Override
    public String toString() {
        return "ClassifySubBean{" +
                "goodsTypeTwoId='" + goodsTypeTwoId + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", choose=" + choose +
                '}';
    }

    public String getGoodsTypeTwoId() {
        return goodsTypeTwoId;
    }

    public void setGoodsTypeTwoId(String goodsTypeTwoId) {
        this.goodsTypeTwoId = goodsTypeTwoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }
}
