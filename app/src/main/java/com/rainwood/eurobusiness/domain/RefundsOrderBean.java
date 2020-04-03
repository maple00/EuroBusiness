package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/3 20:08
 * @Desc: 退货订单
 */
public final class RefundsOrderBean implements Serializable {

    private String orderNo;             // 订单号
    private String buyCarMxId;          // 订单明细id
    private String ico;                 // 图片
    private String goodsName;           // 商品名称
    private String skuName;             // 规格名称
    private String model;               // 型号
    private String discount;            // 折扣
    private String taxRate;             // 税率

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBuyCarMxId() {
        return buyCarMxId;
    }

    public void setBuyCarMxId(String buyCarMxId) {
        this.buyCarMxId = buyCarMxId;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
