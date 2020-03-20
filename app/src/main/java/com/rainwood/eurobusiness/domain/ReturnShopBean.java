package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/19 15:57
 * @Desc: 退货列表
 */
public final class ReturnShopBean implements Serializable {

    private String id;          // 商品id
    private String ico;         // 图片地址
    private String goodsName;       // 商品名称
    private String skuName;     // 商品规格
    private String refundNum;       // 退货数量
    private String refundMoney;     // 退货金额
    private String workFlow;        // 退货状态

    @Override
    public String toString() {
        return "ReturnShopBean{" +
                "id='" + id + '\'' +
                ", ico='" + ico + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", refundNum='" + refundNum + '\'' +
                ", refundMoney='" + refundMoney + '\'' +
                ", workFlow='" + workFlow + '\'' +
                '}';
    }

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

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }
}
