package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/25 16:00
 * @Desc: 参数、规格
 */
public final class SpecialBean implements Serializable {

    private String id;          // ID
    private String goodsSkuId;      // 规格id
    private String goodsColor;      // 颜色
    private String goodsSize;       // 尺码
    private String tradePrice;      // 批发价
    private String stock;           // 库存
    private String iowerLimit;      // 库存下限
    // 订单管理详情
    private String price;           // 价格
    private String num;             // 数量
    private String godosSize;       // 尺码
    private String refundText;      // 退货说明，为空表示可退货
    private String refundState;     // 退货的状态 为空 可退货 complete 退货完成 refunding 退货中

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getGodosSize() {
        return godosSize;
    }

    public void setGodosSize(String godosSize) {
        this.godosSize = godosSize;
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

    public String getRefundText() {
        return refundText;
    }

    public void setRefundText(String refundText) {
        this.refundText = refundText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
