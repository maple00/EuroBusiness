package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 盘点记录
 */
public class StockBean implements Serializable {

    private String imgPath;         // 图片地址
    private String name;            // 名称
    private String model;           // 型号
    private String params;          // 规格参数
    private String venNum;          // 库存
    private String stockNum;        // 盘点
    private String status;          // 状态

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getVenNum() {
        return venNum;
    }

    public void setVenNum(String venNum) {
        this.venNum = venNum;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
