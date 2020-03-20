package com.rainwood.eurobusiness.domain;

/**
 * @Author: a797s
 * @Date: 2019/12/4 15:55
 * @Desc: 图片 + description
 */
public class ItemGridBean {

    private int imgId;          // 图片的路径
    private String itemName;    // 图片的名字/描述

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
