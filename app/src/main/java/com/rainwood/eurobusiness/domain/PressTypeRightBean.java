package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 选中实体类
 */
public class PressTypeRightBean implements Serializable {

    private boolean choose;             // 是否选中
    private String name;               // 选中的title
    private String imgPath;             // 选中的图片的路径
    private String goodsTypeTwoId;      // id

    @Override
    public String toString() {
        return "PressTypeRightBean{" +
                "choose=" + choose +
                ", name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", goodsTypeTwoId='" + goodsTypeTwoId + '\'' +
                '}';
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGoodsTypeTwoId() {
        return goodsTypeTwoId;
    }

    public void setGoodsTypeTwoId(String goodsTypeTwoId) {
        this.goodsTypeTwoId = goodsTypeTwoId;
    }
}
