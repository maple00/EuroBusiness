package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/2 15:21
 * @Desc: 退货规格
 */
public final class RefundsSepBean implements Serializable {

    private String skuId;           // 规格id
    private String name;            // 规格

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
