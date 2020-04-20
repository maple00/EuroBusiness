package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/15 19:49
 * @Desc: 商品详情
 */
public final class GoodsDetailsBean implements Serializable {

    private String id;              // 商品id
    private String name;            // 商品名称
    private String model;           // 商品型号
    private String price;           // 商品进价
    private String retailPrice ;    // 商品零售价
    private String tradePrice;      // 商品批发价
    private String barCode;         // 商品条码
    private String isTax;           // 是否含有增值税
    private String goodsUnit;       // 商品规格
    private String taxRate;         // 税率
    private String startNum;        // 最小起订量
    private String goodsTypeOne;        // 商品一级分类
    private String goodsTypeTwo;        // 商品二级分类
    private String goodsTypeTwoId;      // 商品二级分类id

    public String getGoodsTypeTwoId() {
        return goodsTypeTwoId;
    }

    public void setGoodsTypeTwoId(String goodsTypeTwoId) {
        this.goodsTypeTwoId = goodsTypeTwoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getIsTax() {
        return isTax;
    }

    public void setIsTax(String isTax) {
        this.isTax = isTax;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getGoodsTypeOne() {
        return goodsTypeOne;
    }

    public void setGoodsTypeOne(String goodsTypeOne) {
        this.goodsTypeOne = goodsTypeOne;
    }

    public String getGoodsTypeTwo() {
        return goodsTypeTwo;
    }

    public void setGoodsTypeTwo(String goodsTypeTwo) {
        this.goodsTypeTwo = goodsTypeTwo;
    }
}
