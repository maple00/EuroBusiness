package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/20 13:40
 * @Desc: 退货商品
 */
public final class RefundGoodBean implements Serializable {

    private String id;          // 采购单id
    private String mxId;            // 规格id
    private String ico;         // 商品图片地址
    private String goodsName;       // 商品名称
    private String model;           /// 型号

    private String goodsSkuName;     // 规格名称
    private String taxRate;         // 税率
    private String discount;        // 折扣
    private String num;             // 最多可退数量
    private String isSku;           // 是否需要选择规格退货 1：是 0：不是



}
