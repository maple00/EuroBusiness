package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/8 19:54
 * @Desc: 采购订单规格接口 数据组合bean
 */
public final class ISpecialBean implements Serializable {

    private String storeId;         // 门店id
    private String skuId;           // 库存id
    private String num;             // 数量

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
