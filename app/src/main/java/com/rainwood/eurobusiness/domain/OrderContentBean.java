package com.rainwood.eurobusiness.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单内容
 */
public class OrderContentBean implements Serializable {

    private String method;                  // 订单方式
    private String num;                     // 订单号
    private String status;                  // 订单状态 --- 待发货、待收款、已收款、已完成

    private List<CommonUIBean> managerList; // 订单管理页面数据


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommonUIBean> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<CommonUIBean> managerList) {
        this.managerList = managerList;
    }
}
