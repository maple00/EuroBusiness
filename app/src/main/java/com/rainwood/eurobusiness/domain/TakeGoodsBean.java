package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 16:45
 * @Desc: 收货信息
 */
public final class TakeGoodsBean implements Serializable {

    private String id;          // 收货地址ID
    private String companyName;     // 公司名称
    private String contactTel;      // 手机号
    private String region;          // 所在地区
    private String addressMx;       // 详细地址

    @Override
    public String toString() {
        return "TakeGoodsBean{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", contactTel='" + contactTel + '\'' +
                ", region='" + region + '\'' +
                ", addressMx='" + addressMx + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
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
