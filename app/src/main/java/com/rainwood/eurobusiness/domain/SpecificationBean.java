package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/24 10:06
 * @Desc: 规格
 */
public final class SpecificationBean implements Serializable {

    private String id;
    private String goodsColor;          // 颜色
    private String goodsSize;           // 尺码
    private String tradePrice;          // 批发价
    private String stock;               // 库存
    private String iowerLimit;          // 库存下限

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsColor() {
        return goodsColor;
    }

    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }

    public String getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIowerLimit() {
        return iowerLimit;
    }

    public void setIowerLimit(String iowerLimit) {
        this.iowerLimit = iowerLimit;
    }
}
