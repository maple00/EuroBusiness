package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 采购商品
 */
public class PurchasesBean implements Serializable {

    private String id;              // 订单id
    private String orderNo;         // 订单号
    private String ico;             // 图片地址
    private String name;            // 商品名称
    private String buyTotal;        // 采购数
    private String inTotal;         // 入库数
    private String workFlow;        // 状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getBuyTotal() {
        return buyTotal;
    }

    public void setBuyTotal(String buyTotal) {
        this.buyTotal = buyTotal;
    }

    public String getInTotal() {
        return inTotal;
    }

    public void setInTotal(String inTotal) {
        this.inTotal = inTotal;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }
}
