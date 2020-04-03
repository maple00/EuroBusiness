package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/26 16:52
 * @Desc: 商品详情
 */
public final class GoodBean implements Serializable {

    private String id;          // 商品ID
    private String name;        // 商品名称
    private String model;       // 商品型号
    private String barCode;     // 条码
    private String goodsTypeOne;        // 一级分类
    private String goodsTypeTwo;        // 二级分类
    private String tradePrice;      // 批发价
    private String retailPrice;     // 零售价
    private String isTax;           // 是否含有增值税
    // 规格参数
    private String goodsOption;     // 商品尺码
    private String goodsUnit;       // 商品规格
    private String startNum;        // 最小起订量

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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getIsTax() {
        return isTax;
    }

    public void setIsTax(String isTax) {
        this.isTax = isTax;
    }

    public String getGoodsOption() {
        return goodsOption;
    }

    public void setGoodsOption(String goodsOption) {
        this.goodsOption = goodsOption;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }
}
