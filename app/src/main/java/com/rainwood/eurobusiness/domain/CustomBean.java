package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 客户管理
 */
public class CustomBean implements Serializable {

    private int uiType;                 // 加载UI类型   ---  0: 门店端； 1：批发商端
    private String logoPath;            // logo图片地址
    private String name;                // 名称
    private String type;                // 客户类型
    private String gather;              // 应收款

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGather() {
        return gather;
    }

    public void setGather(String gather) {
        this.gather = gather;
    }
}
