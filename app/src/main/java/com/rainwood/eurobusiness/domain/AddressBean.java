package com.rainwood.eurobusiness.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/24 15:59
 * @Desc: 地址bean
 */
public class AddressBean implements Serializable {

    private String id;              // 地址id
    private String name;            // 地址名称
    private String address;         // 地址
    private boolean checked;        // 是否是默认地址

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
