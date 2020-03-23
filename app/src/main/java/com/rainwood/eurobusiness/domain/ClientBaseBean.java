package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 16:25
 * @Desc: 客户基本资料
 */
public final class ClientBaseBean implements Serializable {

    private String id;              // 客户id
    private String company;         // 公司名称
    private String name;            // 联系人
    private String tel;             // 手机号
    private String email;           // 邮箱
    // 支付信息
    private String kehuTypeName;            // 客户类型
    private String payType;         // 支付方式
    private String payTerm;         // 支付年限
    private String text;            // 备注

    @Override
    public String toString() {
        return "ClientBaseBean{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", kehuTypeName='" + kehuTypeName + '\'' +
                ", payType='" + payType + '\'' +
                ", payTerm='" + payTerm + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getKehuTypeName() {
        return kehuTypeName;
    }

    public void setKehuTypeName(String kehuTypeName) {
        this.kehuTypeName = kehuTypeName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
