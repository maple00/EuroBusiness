package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:57
 * @Desc: 批发商  ---- 商品管理(商品分类) -- 子级分类
 */
public class ClassifySubBean implements Serializable {

    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
