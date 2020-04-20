package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 16:42
 * @Desc: 开票信息
 */
public final class InvoiceDetailBean implements Serializable {

    private String id;                  // 发票地址ID
    private String name;                // 公司名称
    private String paragraph;           // 税号
    private String email;               // 邮箱
    private String consignee;           // 联系人
    private String consigneeTel;        // 手机号
    private String region;              // 所在地区
    private String addressMx;           // 详细地址

    @Override
    public String toString() {
        return "InvoiceDetailBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", paragraph='" + paragraph + '\'' +
                ", email='" + email + '\'' +
                ", consigneeTel='" + consigneeTel + '\'' +
                ", region='" + region + '\'' +
                ", addressMx='" + addressMx + '\'' +
                '}';
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
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
}
