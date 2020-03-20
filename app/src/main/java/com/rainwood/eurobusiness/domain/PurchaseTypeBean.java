package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc:
 */
public class PurchaseTypeBean implements Serializable {

    private boolean selected;              // 是否被选中
    private boolean bulkSelect;             // 是否批量选中
    private String imgPath;                 // 商品图片
    private String paramSize;               // 规格参数
    private String price;                   // 单价
    private String purchase;                // 采购数
    private String inStorage;               // 入库数
    private String returnNum;               // 退货数量
    private String allMoney;                // 合计金额

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBulkSelect() {
        return bulkSelect;
    }

    public void setBulkSelect(boolean bulkSelect) {
        this.bulkSelect = bulkSelect;
    }

    public void setSerlected(boolean selected) {
        this.selected = selected;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getParamSize() {
        return paramSize;
    }

    public void setParamSize(String paramSize) {
        this.paramSize = paramSize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getInStorage() {
        return inStorage;
    }

    public void setInStorage(String inStorage) {
        this.inStorage = inStorage;
    }

    public String getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(String returnNum) {
        this.returnNum = returnNum;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }
}
