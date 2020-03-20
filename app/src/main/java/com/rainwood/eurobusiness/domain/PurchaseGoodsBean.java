package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc:
 */
public class PurchaseGoodsBean implements Serializable {

    private String name;                    // 商品名称
    private String model;                   // 商品型号
    private String discount;                // 折扣
    private String rate;                    // 税率
    // 按照类别区分
    private List<PurchaseTypeBean> typeList;    // 商品类型列表

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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<PurchaseTypeBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<PurchaseTypeBean> typeList) {
        this.typeList = typeList;
    }
}
