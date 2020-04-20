package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/15 13:27
 * @Desc: 客户详情
 */
public final class CustomDetailBean implements Serializable {

    private String id;          // 客户id
    private String tel;         // 联系电话
    private String name;        // 客户联系人
    private String email;       // 邮箱
    private String companyName; // 公司
    private String kehuTypeName;// 客户分类
    private String payType;     // 支付方式
    private String payTerm;     // 支付期限
    private String text;        // 备注

    public String getKehuTypeName() {
        return kehuTypeName;
    }

    public void setKehuTypeName(String kehuTypeName) {
        this.kehuTypeName = kehuTypeName;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTerm() {
        return payTerm;
    }

    public void setPayTerm(String payTerm) {
        this.payTerm = payTerm;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
