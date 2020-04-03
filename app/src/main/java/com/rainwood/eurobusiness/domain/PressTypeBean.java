package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 选中实体类
 */
public class PressTypeBean implements Serializable {

    private boolean choose;             // 是否选中
    private String goodsTypeOne;               // 选中的title
    private String imgPath;             // 选中的图片的路径
    private String goodsTypeOneId;      // id

    @Override
    public String toString() {
        return "PressTypeBean{" +
                "choose=" + choose +
                ", goodsTypeOne='" + goodsTypeOne + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", goodsTypeOneId='" + goodsTypeOneId + '\'' +
                '}';
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getGoodsTypeOne() {
        return goodsTypeOne;
    }

    public void setGoodsTypeOne(String goodsTypeOne) {
        this.goodsTypeOne = goodsTypeOne;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGoodsTypeOneId() {
        return goodsTypeOneId;
    }

    public void setGoodsTypeOneId(String goodsTypeOneId) {
        this.goodsTypeOneId = goodsTypeOneId;
    }
}
