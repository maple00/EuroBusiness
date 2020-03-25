package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/24 18:50
 * @Desc: 客户分类
 */
public class CustomTypeBean implements Serializable {

    private String id;                 // 分类id
    private String order;           // 排序
    private String name;            // 分类名称
    private String discount;         // 占比
    // 详情
    private String list;            // 排序号

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
