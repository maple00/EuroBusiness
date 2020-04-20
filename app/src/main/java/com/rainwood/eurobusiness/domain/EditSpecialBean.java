package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/16 14:41
 * @Desc: 编辑商品中的规格参数
 */
public final class EditSpecialBean implements Serializable {

    private String goodsSkuId;          // 规格id
    private String goodsColor;          // 颜色
    private String goodsSize;           // 尺寸
    private String iowerLimit;          // 库存下限
    private String stock;               // 库存量
    private String tradePrice;          // 批发价

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
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

    public String getIowerLimit() {
        return iowerLimit;
    }

    public void setIowerLimit(String iowerLimit) {
        this.iowerLimit = iowerLimit;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }
}
