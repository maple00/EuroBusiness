package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/1 14:34
 * @Desc: 门店详情
 */
public final class StoreDetailBean implements Serializable {

    private String id;              // 门店ID
    private String name;              // 	门店名称
    private String adDutyId;              // 门店权限ID
    private String adDutyName;          // 权限名称
    private String adLoginName;              // 登录名称
    private String loginTel;              // 登录人电话
    private String password;              // 密码
    private String email;              // 邮箱
    private String tel;              // 电话
    private String region;              // 地区
    private String address;              // 详细地址
    private String comapnyTaxNum;              // 税号 PIVA
    private String taxNum;              // 税号CF
    private String contactName;              // 联系人
    private String text;              // 门店描述

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

    public String getAdDutyId() {
        return adDutyId;
    }

    public void setAdDutyId(String adDutyId) {
        this.adDutyId = adDutyId;
    }

    public String getAdDutyName() {
        return adDutyName;
    }

    public void setAdDutyName(String adDutyName) {
        this.adDutyName = adDutyName;
    }

    public String getAdLoginName() {
        return adLoginName;
    }

    public void setAdLoginName(String adLoginName) {
        this.adLoginName = adLoginName;
    }

    public String getLoginTel() {
        return loginTel;
    }

    public void setLoginTel(String loginTel) {
        this.loginTel = loginTel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComapnyTaxNum() {
        return comapnyTaxNum;
    }

    public void setComapnyTaxNum(String comapnyTaxNum) {
        this.comapnyTaxNum = comapnyTaxNum;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
