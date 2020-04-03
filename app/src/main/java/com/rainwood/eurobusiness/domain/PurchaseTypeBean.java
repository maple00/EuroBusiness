package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 采购订单详情
 */
public class PurchaseTypeBean implements Serializable {

    private boolean selected;              // 是否被选中
    private boolean bulkSelect;             // 是否批量选中
    private String mxId;                    // 规格id
    private String ico;                     // 商品图片
    private String goodsSkuNAme;            // 规格参数
    private String isSku;                   // 是否是混装
    private String price;                   // 单价
    private String num;                     // 采购数
    private String inStoreNum;             // 入库数
    private String refundNum;               // 退货数量
    private String totalMoney;               // 合计金额

    @Override
    public String toString() {
        return "PurchaseTypeBean{" +
                "selected=" + selected +
                ", bulkSelect=" + bulkSelect +
                ", ico='" + ico + '\'' +
                ", goodsSkuNAme='" + goodsSkuNAme + '\'' +
                ", isSku='" + isSku + '\'' +
                ", price='" + price + '\'' +
                ", num='" + num + '\'' +
                ", inStoreNum='" + inStoreNum + '\'' +
                ", refundNum='" + refundNum + '\'' +
                ", totalMoney='" + totalMoney + '\'' +
                '}';
    }

    public String getMxId() {
        return mxId;
    }

    public void setMxId(String mxId) {
        this.mxId = mxId;
    }

    public String getIsSku() {
        return isSku;
    }

    public void setIsSku(String isSku) {
        this.isSku = isSku;
    }

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

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getGoodsSkuNAme() {
        return goodsSkuNAme;
    }

    public void setGoodsSkuNAme(String goodsSkuNAme) {
        this.goodsSkuNAme = goodsSkuNAme;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getInStoreNum() {
        return inStoreNum;
    }

    public void setInStoreNum(String inStoreNum) {
        this.inStoreNum = inStoreNum;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}
