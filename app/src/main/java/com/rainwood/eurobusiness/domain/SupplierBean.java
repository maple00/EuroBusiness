package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/8 10:07
 * @Desc: 供应商
 */
public final class SupplierBean implements Serializable {

    private String id;              // id
    private String name;            // 供应商

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
}
