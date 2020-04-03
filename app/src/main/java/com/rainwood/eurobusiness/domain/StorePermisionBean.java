package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/1 14:33
 * @Desc: 门店权限
 */
public final class StorePermisionBean implements Serializable {

    private String id;          // 门店权限选项ID
    private String name;            // 权限名称

    @Override
    public String toString() {
        return "StorePermisionBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
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
}
