package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/31 9:59
 * @Desc: 订单详情
 */
public final class OrderDetailBean implements Serializable {

    private String id;                  // 订单号
    private String khName;              // 客户名称
    private String goodsName;           // 商品名称
    private String ico;                 // 商品图片
    private String workFlow;            // 商品状态
    private String model;               // 商品型号
    private String isTax;               // 是否含税
    private String freight;             // 运费
    private String text;                // 备注
    private String discount;            // 折扣
    private String taxRate;             // 税率
    private String kehuDecMoney;        // VIP优惠
    private String invoiceCompany;      // 发票公司
    private String invoiceTax;          // 发票税号
    private String invoiceEmail;        // 邮箱
    private String region;              // 发货地区
    private String addressMx;           // 详细地址
    private String contactName;         // 收货人
    private String companyName;         // 公司
    private String money;               // 总价
    private String type;                // 线上订单、线下订单

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKhName() {
        return khName;
    }

    public void setKhName(String khName) {
        this.khName = khName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIsTax() {
        return isTax;
    }

    public void setIsTax(String isTax) {
        this.isTax = isTax;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getKehuDecMoney() {
        return kehuDecMoney;
    }

    public void setKehuDecMoney(String kehuDecMoney) {
        this.kehuDecMoney = kehuDecMoney;
    }

    public String getInvoiceCompany() {
        return invoiceCompany;
    }

    public void setInvoiceCompany(String invoiceCompany) {
        this.invoiceCompany = invoiceCompany;
    }

    public String getInvoiceTax() {
        return invoiceTax;
    }

    public void setInvoiceTax(String invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddressMx() {
        return addressMx;
    }

    public void setAddressMx(String addressMx) {
        this.addressMx = addressMx;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
