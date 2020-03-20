package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/19 17:34
 * @Desc: 退货详情
 */
public final class ReturnDetailBean implements Serializable {

    private String id;          // 商品id
    private String workFlow;        // 订单状态
    private String text;        // 退货原因
    private String ico;         // 商品图片
    private String goodsName;       // 商品名称
    private String discount;        // 折扣
    private String taxRate;         // 税率
    private String skuName;         // 规格
    private String auditText;       // 审批原因
    private List<CommonUIBean> mList;   // 列表

    public String getAuditText() {
        return auditText;
    }

    public void setAuditText(String auditText) {
        this.auditText = auditText;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public List<CommonUIBean> getList() {
        return mList;
    }

    public void setList(List<CommonUIBean> list) {
        mList = list;
    }
}
