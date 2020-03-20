package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 发票Bean
 */
public class InvoiceBean implements Serializable {

    private String company;             // 公司
    private String taxP;                // 公司税号
    private String location;            // 位置
    private String address;             // 详细地址
    private String tel;                 // 电话

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaxP() {
        return taxP;
    }

    public void setTaxP(String taxP) {
        this.taxP = taxP;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
