package com.rainwood.eurobusiness.domain;

import com.rainwood.eurobusiness.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/2 15:02
 * @Desc: 采购订单退货
 */
public final class PurchaseRefundsBean implements Serializable {

    private String id;              // 采购单ID
    private String mxId;            // 规格ID
    private String ico;             // 图片
    private String goodsName;       // 商品名称
    private String model;           // 型号
    private String goodsSkuName;        // 规格名称
    private String taxRate;         // 税率
    private String discount;        // 折扣
    private String num;             // 可退数量
    private String isSku;           // 是否是混装
    // 退货规格
    private List<RefundsSepBean> skulist;       // 退货规格

    public String getIsSku() {
        return isSku;
    }

    public void setIsSku(String isSku) {
        this.isSku = isSku;
    }

    public List<RefundsSepBean> getSkulist() {
        return skulist;
    }

    public void setSkulist(List<RefundsSepBean> skulist) {
        this.skulist = skulist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMxId() {
        return mxId;
    }

    public void setMxId(String mxId) {
        this.mxId = mxId;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
