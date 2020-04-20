package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/16 16:57
 * @Desc: 门店入库明细规格
 */
public final class SpecialDetailBean implements Serializable {

    private String goodsColor;          // 商品颜色
    private String goodsSize;           // 商品尺寸
    private String price;               // 商品价格
    private String num;                 // 商品数量
    private String inStockNum;          // 入库数量
    private String refundNum;           // 退货数量

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getInStockNum() {
        return inStockNum;
    }

    public void setInStockNum(String inStockNum) {
        this.inStockNum = inStockNum;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }
}
