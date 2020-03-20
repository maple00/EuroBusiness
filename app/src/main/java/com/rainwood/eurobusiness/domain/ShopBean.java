package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc: 商品 bean
 */
public class ShopBean implements Serializable {

    private int imgPath;            // 图片路径
    private String name;            // 商品名称
    private String status;          // 商品状态
    private String wholesalePrice;  // 批发价格
    private String retailPrice;     // 零售价格
    private String source;          // 商品来源
    private String invenNum;        // 库存件数
    private String model;           // 商品型号

    public String getInvenNum() {
        return invenNum;
    }

    public void setInvenNum(String invenNum) {
        this.invenNum = invenNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
