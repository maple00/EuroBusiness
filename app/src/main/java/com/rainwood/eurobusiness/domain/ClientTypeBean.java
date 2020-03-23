package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 15:28
 * @Desc: 客户类别
 */
public final class ClientTypeBean implements Serializable {

    private String id;
    private String name;

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
