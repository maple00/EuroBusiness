package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 退货管理
 */
public class ReturnGoodsBean implements Serializable {

    private String imgPath;             // 图片
    private String name;                // 名称
    private String params;              // 规格参数
    private String returnNum;           // 退货数量
    private String money;               // 退款金额
    private String status;              // 状态
    // 详情数据 -- 多加
    private String discount;            // 折扣
    private String rate;                // 税率
    private String model;               // 型号
    private List<CommonUIBean> commonUIList;    // 列表
    // 退货申请
    private String returnReason;        // 退货原因
    private String fee;                 // 退货运费
    private String payMethod;           // 付款方

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<CommonUIBean> getCommonUIList() {
        return commonUIList;
    }

    public void setCommonUIList(List<CommonUIBean> commonUIList) {
        this.commonUIList = commonUIList;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(String returnNum) {
        this.returnNum = returnNum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
