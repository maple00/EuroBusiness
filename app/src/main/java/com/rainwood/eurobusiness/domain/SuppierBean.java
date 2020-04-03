package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/24 22:00
 * @Desc: 供应商
 */
public class SuppierBean implements Serializable {

    private String id;              // 供应商ID
    private String name;            // 名称
    private String num;             // 编号
    private String money;           // 累计应付款
    // 供应商详情
    private String tel;             // 联系电话
    private String chargeName;      // 负责人名称
    private String address;         // 详细地址
    private String regionId;        // 国家/地区
    private String payType;         // 支付方式
    private String bankNum;         // 银行账号
    private String email;           // 邮箱

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
