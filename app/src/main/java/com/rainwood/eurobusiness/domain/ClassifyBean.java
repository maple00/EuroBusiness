package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:56
 * @Desc: 批发商  ---- 商品管理(商品分类)
 */
public class ClassifyBean implements Serializable {

    private String goodsTypeOneId;      // 父类分类id
    private String imgPath;             // 图片地址
    private int selected = 0;               // 点击记录
    private String goodsTypeOne;                // 分类名称
    private boolean choose;
    private List<ClassifySubBean> goodsTypeTwolist;      // 子项分类

    @Override
    public String toString() {
        return "ClassifyBean{" +
                "goodsTypeOneId='" + goodsTypeOneId + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", selected=" + selected +
                ", goodsTypeOne='" + goodsTypeOne + '\'' +
                ", goodsTypeTwolist=" + goodsTypeTwolist +
                '}';
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
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

    public String getGoodsTypeOne() {
        return goodsTypeOne;
    }

    public void setGoodsTypeOne(String goodsTypeOne) {
        this.goodsTypeOne = goodsTypeOne;
    }

    public List<ClassifySubBean> getGoodsTypeTwolist() {
        return goodsTypeTwolist;
    }

    public void setGoodsTypeTwolist(List<ClassifySubBean> goodsTypeTwolist) {
        this.goodsTypeTwolist = goodsTypeTwolist;
    }
}
