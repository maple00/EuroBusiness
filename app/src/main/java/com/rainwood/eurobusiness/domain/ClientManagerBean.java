package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/23 9:08
 * @Desc: 客户管理列表
 */
public final class ClientManagerBean implements Serializable {

    private String id;
    private String name;
    private String type;
    private String ico;
    private String money;

    @Override
    public String toString() {
        return "ClientManagerBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ico='" + ico + '\'' +
                ", money='" + money + '\'' +
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
