package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/24 9:02
 * @Desc: 预警列表商品
 */
public final class WarnStockBean implements Serializable {

    private String id;
    private String ico;         // 商品图片
    private String name;        // 商品名字
    private String skuName;     // 商品规格
    private String stock;       // 库存量
    private String chargeNum;   // 补货量
    private String model;       // 型号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(String chargeNum) {
        this.chargeNum = chargeNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
