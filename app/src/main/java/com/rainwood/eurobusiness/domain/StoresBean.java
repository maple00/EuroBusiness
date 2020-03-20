package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 门店信息
 */
public class StoresBean implements Serializable {

    private String id;              // 门店编号
    private String name;            // 门店名称
    private String telName;         // 联系人
    private String email;           // 邮箱
    private String tel;             // 电话
    private String location;        // 所在地区
    private String address;         // 详细地址
    private String taxP;            // 税号(P.IVA)
    private String taxCF;           // 税号(C.F)
    private String note;            // 备注
    private boolean delete;         // 是否删除

    @Override
    public String toString() {
        return "StoresBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", telName='" + telName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", taxP='" + taxP + '\'' +
                ", taxCF='" + taxCF + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
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

    public String getTelName() {
        return telName;
    }

    public void setTelName(String telName) {
        this.telName = telName;
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

    public String getTaxP() {
        return taxP;
    }

    public void setTaxP(String taxP) {
        this.taxP = taxP;
    }

    public String getTaxCF() {
        return taxCF;
    }

    public void setTaxCF(String taxCF) {
        this.taxCF = taxCF;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
